#!/bin/bash

green='\033[0;32m' # use 35 by default
red='\033[0;31m' # use 32 by default
clear='\033[0m'

if [[ $# == 0 ]]; then
    echo -e "Use -h flag for help"
    exit 1
fi

if [[ $# == 1 ]] && [[ $1 == "-h" ]]; then
    echo "Usage: ./run.sh [flags] [<files>...]"
    echo ""
    echo "flags:"
    echo "-h                show this help message"
    echo "-d <dir>          specify base directory containing input and output folders"
    echo "-clear            delete output and .class files"
    echo "-notest           only runs the files and prints output"
    echo "-debug            prints your output to file and error on terminal,"
    echo "                  it stops as soon as it finds one incorrect output"
    echo ""
    echo "files:"
    echo ""
    echo "all               tests all files in test/input/pos and test/input/neg folders" 
    echo "pos               tests all files in test/input/pos folder" 
    echo "neg               tests all files in test/input/neg folder" 
    echo "<file>            tests the file"
    echo ""
    exit 1
fi

all=0
pos=0
neg=0
debug=0
notest=0
clear_f=0
set_dir=0
dir="../../CS6013_TestCases/Assignment2"
outputdir="test"
files=()

for i in $(seq 1 $#)
do
    if [[ $set_dir == 1 ]]; then
        dir=${!i}
        set_dir=0
    elif [[ ${!i} == "all" ]]; then
        all=1
    elif [[ ${!i} == "pos" ]]; then
        pos=1
    elif [[ ${!i} == "neg" ]]; then
        neg=1
    elif [[ ${!i} == "-debug" ]]; then
        debug=1
    elif [[ ${!i} == "-notest" ]]; then
        notest=1
    elif [[ ${!i} == "-clear" ]]; then
        clear_f=1
    elif [[ ${!i} == "-d" ]]; then
        set_dir=1
    else
        files+=("${!i}")
    fi
done

total=0
pass=0
stop=0

function testBuritoJava {
    total=$((total+1))
    echo "-----------------------------------------------------"
    echo "Processing $1"

    IFS='/' read -a name <<< "$1"

    type=${name[-2]}
    file=${name[-1]}
    file_name=$(basename $file .java)

    correct=""
    if [[ $type == "pos" || $type == "public" ]]
    then
        correct="No uninitialized variables."
    else
        correct="Uninitialized variable found."
    fi

    if [[ -f $dir/input/$type/$file_name.java ]]
    then
        if [[ $debug = 0 ]]
        then
            java P1 < $dir/input/$type/$file_name.java 1> $outputdir/output/$type/$file_name 2>/dev/null
        else
            if [[ $notest == 1 ]]
            then
                java P1 < $dir/input/$type/$file_name.java &> $outputdir/output/$type/$file_name
            else
                java P1 < $dir/input/$type/$file_name.java 1> $outputdir/output/$type/$file_name
            fi
        fi

        if [[ $notest == 0 ]]
        then
            if [[ $(cat $dir/output/$type/$file_name) == $correct ]]
            then
                pass=$((pass+1))
                echo -e "----->" "${green}Noice${clear}"
            else
                echo -e "----->" "${red}sed${clear}"
                if [[ $debug == 1 ]]; then
                    stop=1
                    echo "--------------"
                    echo "correct:" 
                    echo $correct
                    echo "--------------"
                    echo "mine:"
                    cat $outputdir/output/$type/$file_name
                    code -g $dir/input/$type/$file_name.java
                    code -g $outputdir/output/$type/$file_name
                fi
            fi
        else
            echo "Output: " 
            cat $outputdir/output/$type/$file_name
        fi
    else
        echo -e "File $dir/input/$type/$file_name.java" "${red}not found${clear}"
    fi
}

find . -name "*.class" | xargs rm 2>/dev/null
javac P1.java

if [[ $all == 1 ]]
then
    for i in $dir/input/*/*
    do
        if [[ $stop == 1 ]]; then
            exit 1
        fi
        testBuritoJava $i
    done
else
    if [[ $neg == 1 ]]
    then
        for i in $dir/input/neg/*
        do
            if [[ $stop == 1 ]]; then
                exit 1
            fi
            testBuritoJava $i
        done
    fi

    if [[ $pos == 1 ]]
    then
        for i in $dir/input/pos/*
        do
            if [[ $stop == 1 ]]; then
                exit 1
            fi
            testBuritoJava $i
        done
    fi

    for i in ${files[@]}
    do
        if [[ $stop == 1 ]]; then
            exit 1
        fi

        IFS='/' read -a name <<< "$i"
        type=${name[-2]}

        if [[ $type == "pos" && $pos == 0 ]]
        then
            testBuritoJava $i
        elif [[ $type == "neg" && $neg == 0 ]]
        then
            testBuritoJava $i
        elif [[ $type != "pos" && $type != "neg" ]]
        then
            testBuritoJava $i
        fi
    done
fi

echo "-----------------------------------------------------"

if [[ $notest == 0 ]]
then
    echo "Processing done " $pass "/" $total " passed"
fi

if [[ $clear_f == 1 ]]
then
    cd ..
    find . -name "*.class" | xargs rm 2>/dev/null
    cd P1
    rm -rf $dir/output/pos/* $dir/output/neg/*
fi

find . -name "*.class" | xargs rm 2>/dev/null
