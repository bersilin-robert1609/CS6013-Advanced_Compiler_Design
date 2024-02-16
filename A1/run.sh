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
    echo "public            tests all files in test/input/public folder" 
    echo "<file>            tests the file"
    echo "-re <regex>       tests all files in input/ matching regex"
    echo ""
    exit 1
fi

all=0
pos=0
neg=0
public=0
debug=0
notest=0
clear_f=1
set_dir=0
dir="../../../Test/Assignment2"
files=()
set_regex=0
regexes=()

for i in $(seq 1 $#)
do
    if [[ $set_dir == 1 ]]; then
        dir=${!i}
        set_dir=0
    elif [[ $set_regex == 1 ]]; then
        regexes+=("${!i}")
        set_regex=0
    elif [[ ${!i} == "all" ]]; then
        all=1
    elif [[ ${!i} == "pos" ]]; then
        pos=1
    elif [[ ${!i} == "neg" ]]; then
        neg=1
    elif [[ ${!i} == "public" ]]; then
        public=1
    elif [[ ${!i} == "-debug" ]]; then
        debug=1
    elif [[ ${!i} == "-notest" ]]; then
        notest=1
    elif [[ ${!i} == "-clear" ]]; then
        clear_f=1
    elif [[ ${!i} == "-d" ]]; then
        set_dir=1
    elif [[ ${!i} == "-re" ]]; then
        set_regex=1
    else
        files+=("${!i}")
    fi
done

total=0
pass=0
stop=0
cnt=0
progress_len=40
progress_symbol=█
time_taken=0
INPUT_DIR="$dir/input"
OUTPUT_DIR="$dir/mine"

function formatTime {
    seconds=$1
    minutes=$(($seconds/60))
    seconds=$(($seconds%60))

    ans=""
    if [ $minutes -lt 10 ]; then
        ans="0"
    fi
    ans="$ans$minutes"

    ans="$ans:"
    if [ $seconds -lt 10 ]; then
        ans=$ans"0"
    fi
    ans="$ans$seconds"

    echo $ans
}

function printProgress {
    len=$((($1*$3)/$2))
    
    time_left=$((($time_taken+80000000)/$cnt))
    time_left=$(($time_left*($total-$cnt)))
    time_left=$(($time_left/100000000))
    time_left=$((($time_left+6)/10))

    formattedTime=$(formatTime $time_left)
    str="$formattedTime ["
    for i in $(seq 1 $len); do
        str="$str$progress_symbol"
    done
    for i in $(seq $len $(($3-1))); do
        str="$str-"
    done
    str="$str] "

    percentage=$((($1*100)/$2))
    str="$str$percentage%"

    echo "$str"
}

function clearAboveLine {
    echo -e "\e[A\e[K\e[A"
}

function testBuritoJava {

    start_time=$(date +%s%N)

    if [[ $stop == 1 ]]; then
        exit 1
    fi

    cnt=$((cnt+1))
    echo "-----------------------------------------------------"

    IFS='/' read -a name <<< "$1"

    type=${name[-2]}
    file=${name[-1]}
    file_name=$(basename $file .java)

    echo "Processing $type/$file"
    printProgress $cnt $total $progress_len

    correct=""
    if [[ $type == "pos" || $type == "public" ]]
    then
        correct="No uninitialized variables."
    else
        correct="Uninitialized variable found."
    fi

    if [[ -f $INPUT_DIR/$type/$file_name.java ]]
    then
        if [[ $debug == 0 ]]
        then
            java P1 < $INPUT_DIR/$type/$file_name.java 1>$OUTPUT_DIR/$type/$file_name 2>/dev/null
        else
            if [[ $notest == 1 ]]
            then
                java P1 < $INPUT_DIR/$type/$file_name.java &>$OUTPUT_DIR/$type/$file_name
            else
                java P1 < $INPUT_DIR/$type/$file_name.java 1>$OUTPUT_DIR/$type/$file_name 2>$OUTPUT_DIR/$type/Error_$file_name
            fi
        fi

        clearAboveLine
        if [[ $debug == 1 && $notest == 0 ]]; then
            cat $OUTPUT_DIR/$type/Error_$file_name
            rm $OUTPUT_DIR/$type/Error_$file_name
        fi

        if [[ $notest == 0 ]]
        then
            if [[ $(cat $OUTPUT_DIR/$type/$file_name) == $correct ]]
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
                    cat $OUTPUT_DIR/$type/$file_name
                    code -g $INPUT_DIR/$type/$file_name.java
                    code -g $OUTPUT_DIR/$type/$file_name
                fi
            fi
        else
            echo "Output: " 
            cat $OUTPUT_DIR/$type/$file_name
        fi
    else
        clearAboveLine
        echo -e "File $INPUT_DIR/$type/$file_name.java" "${red}not found${clear}"
    fi

    end_time=$(date +%s%N)
    cur_time_per_file=$(expr $end_time-$start_time)

    time_taken=$(($time_taken+$cur_time_per_file))
}

function runOverArray {
    for i in $(seq 2 $#)
    do
        $1 ${!i}
    done
}

function runOverAll {

    if [[ $all == 1 ]]; then
        runOverArray $1 $INPUT_DIR/*/*
    else
        if [[ $neg == 1 ]]; then
            runOverArray $1 $INPUT_DIR/neg/*
        fi

        if [[ $pos == 1 ]]; then
            runOverArray $1 $INPUT_DIR/pos/*
        fi

        if [[ $public == 1 ]]; then
            runOverArray $1 $INPUT_DIR/public/*
        fi

        for file in ${files[@]}
        do
            IFS='/' read -a name <<< "$file"
            type=${name[-2]}

            if [[ $type == "pos" && $pos == 0 ]]
            then
                $1 $file
            elif [[ $type == "neg" && $neg == 0 ]]
            then
                $1 $file
            elif [[ $type == "public" && $public == 0 ]]
            then
                $1 $file
            elif [[ $type != "pos" && $type != "neg" && $type != "public" ]]
            then
                $1 $file
            fi
        done

        for regex in ${regexes[@]}
        do
            for file in $INPUT_DIR/*/*
            do
                if [[ $file =~ $regex ]]
                then
                    $1 $file
                fi
            done
        done
    fi
}

function displayCompile {
    spin='|/-\'
    i=0
    while [[ 1 ]]
    do
      i=$(( (i+1) %4 ))
      printf "\r[${spin:$i:1}] Compiling..."
      sleep .1
    done
}

# display compile animation
displayCompile &
compile_pid=$!
cd P1

# all preprocess before testing
find . -name "*.class" | xargs rm 2>/dev/null
javac P1.java 2>/dev/null

function cntOne {
    total=$((total+1))
}
runOverAll cntOne 

# stop the displayCompile process
kill -9 $compile_pid 2>/dev/null
wait $compile_pid 2>/dev/null
echo -e "\r\e[K\e[A"

if [[ $total == 0 ]]
then
    echo "No files found to run"
else
    # testing
    runOverAll testBuritoJava

    echo "-----------------------------------------------------"
    printProgress $cnt $total $progress_len

    if [[ $notest == 0 ]]
    then
        echo ""
        echo "Processing done $pass / $total passed"
    fi

    if [[ $clear_f == 1 ]]
    then
        # cd ..
        find . -name "*.class" | xargs rm 2>/dev/null
        # cd P1
        # rm -rf $dir/mine/pos/* $dir/mine/neg/*
    fi
fi