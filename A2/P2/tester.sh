#!/bin/bash

green='\033[0;32m' # use 35 by default
red='\033[0;31m' # use 32 by default
clear='\033[0m'

if [[ $# == 0 ]]; then
    echo -e "Use -h flag for help"
    exit 1
fi

if [[ $# == 1 ]] && [[ $1 == "-h" || $1 == "--help" ]]; then
    echo "Usage: ./run.sh [flags] [<files>...]"
    echo ""
    echo "flags:"
    echo "-h,--help                 show this help message"
    echo "-d <dir>                  specify base directory containing input and output folders"
    echo "-e <exec>                 specify main executable name"
    echo "-clear                    delete output and .class files"
    echo "-nt,--no-test             only generates TacoJava files and does not test"
    echo "-nc,--no-compile          does not convert to TacoJava if the file exists"
    echo "-dgo,--dont-gen-output    does not generate correct output again if it exists"
    echo "-go,--grammar-only        only checks if generated TacoJava file follows grammar"
    echo "--debug                    prints your output to file and error on terminal,"
    echo "                          it stops as soon as it finds one incorrect output"
    echo ""
    echo "files:"
    echo ""
    echo "all               tests all files in test/input folder" 
    echo "public            tests all files in test/input/public folder" 
    echo "custom            tests all files in test/input/custom folder" 
    echo "<file>            tests the file"
    echo "-re <regex>       tests all files in input/ matching regex"
    echo ""
    exit 1
fi

all=0
custom=0
public=0
debug=0
notest=0
nocompile=0
gen_output=1
grammar_only=0
clear_f=0
set_dir=0
dir="../../../Test/A2"
set_exec=0
exec="P2"
files=()
set_regex=0
regexes=()

for i in $(seq 1 $#)
do
    if [[ $set_dir == 1 ]]; then
        dir=${!i}
        set_dir=0
    elif [[ $set_exec == 1 ]]; then
        exec=${!i}
        set_exec=0
    elif [[ $set_regex == 1 ]]; then
        regexes+=("${!i}")
        set_regex=0
    elif [[ ${!i} == "all" ]]; then
        all=1
    elif [[ ${!i} == "custom" ]]; then
        custom=1
    elif [[ ${!i} == "public" ]]; then
        public=1
    elif [[ ${!i} == "--debug" ]]; then
        debug=1
    elif [[ ${!i} == "-notest" || ${!i} == "-nt" ]]; then
        notest=1
    elif [[ ${!i} == "-nocompile" || ${!i} == "-nc" ]]; then
        nocompile=1
    elif [[ ${!i} == "--dont-gen-output" || ${!i} == "-dgo" ]]; then
        gen_output=0
    elif [[ ${!i} == "--grammar-only" || ${!i} == "-go" ]]; then
        grammar_only=1
    elif [[ ${!i} == "-clear" ]]; then
        clear_f=1
    elif [[ ${!i} == "-d" ]]; then
        set_dir=1
    elif [[ ${!i} == "-e" ]]; then
        set_exec=1
    elif [[ ${!i} == "-re" ]]; then
        set_regex=1
    else
        files+=("${!i}")
    fi
done

if [[ $debug == 1 && $notest == 1 ]]
then
    echo "Can not use notest and debug flag at the same time"
    exit 1
fi

total=0
pass=0
stop=0
cnt=0
progress_len=40
progress_symbol=█
time_taken=0
INPUT_DIR="$dir/input"
CORRECT_OUTPUT_DIR="$dir/output"
MY_OUTPUT_DIR="$dir/mine"

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

function BuritoToTacoJava {

    start_time=$(date +%s%N)

    if [[ $stop == 1 ]]; then
        exit 1
    fi

    cnt=$((cnt+1))
    echo "-----------------------------------------------------"

    IFS='/' read -a name <<< "$1"

    type=${name[-2]}
    mkdir -p "$MY_OUTPUT_DIR/$type"

    file=${name[-1]}
    file_name=$(basename $file .java)

    echo "Processing $type/$file"
    printProgress $cnt $total $progress_len

    if [[ -f $INPUT_DIR/$type/$file_name.java ]]
    then
        if [[ $nocompile == 0 ]] || [[ ! -f $MY_OUTPUT_DIR/$type/$file_name.java ]]
        then
            checkTacoJava=""
            if [[ $debug == 0 ]]
            then
                java $exec < $INPUT_DIR/$type/$file_name.java 1>$MY_OUTPUT_DIR/$type/$file_name.java 2>/dev/null
                checkTacoJava=$(java --enable-preview -jar $dir/TacoJava.jar < $MY_OUTPUT_DIR/$type/$file_name.java 2>/dev/null)
            else
                java $exec < $INPUT_DIR/$type/$file_name.java 1>$MY_OUTPUT_DIR/$type/$file_name.java 2>$MY_OUTPUT_DIR/$type/$file_name.error
                checkTacoJava=$(java --enable-preview -jar $dir/TacoJava.jar < $MY_OUTPUT_DIR/$type/$file_name.java 2>$MY_OUTPUT_DIR/$type/$file_name.parseError)
            fi

            clearAboveLine
            if [[ $debug == 1 ]]; then
                cat $MY_OUTPUT_DIR/$type/$file_name.error
                cat $MY_OUTPUT_DIR/$type/$file_name.parseError
                rm $MY_OUTPUT_DIR/$type/$file_name.error
                rm $MY_OUTPUT_DIR/$type/$file_name.parseError
            fi
        else
            clearAboveLine
        fi

        if [[ $notest == 1 ]]
        then
            if [[ $grammar_only == 0 ]]; then
                printProgress $cnt $total $progress_len
                java $MY_OUTPUT_DIR/$type/$file_name.java > $MY_OUTPUT_DIR/$type/$file_name.out 2>/dev/null
                clearAboveLine
            fi
        else
            if [[ $checkTacoJava == "Wrong TacoJava" ]]
            then
                echo -e "----->" "${red}Wrong TacoJava${clear}"
                if [[ $debug == 1 ]]; then
                    stop=1
                    code -g $INPUT_DIR/$type/$file_name.java
                    code -g $MY_OUTPUT_DIR/$type/$file_name.java
                fi
            else
                if [[ $grammar_only == 1 ]]
                then
                    pass=$((pass+1))
                    echo -e "----->" "${green}Noice${clear}"
                else
                    printProgress $cnt $total $progress_len

                    if [[ ! -f $CORRECT_OUTPUT_DIR/$type/$file_name.out ]] || [[ $gen_output == 1 ]]
                    then
                        if [[ $debug == 0 ]]
                        then
                            java $INPUT_DIR/$type/$file_name.java > $CORRECT_OUTPUT_DIR/$type/$file_name.out
                        else
                            java $INPUT_DIR/$type/$file_name.java > $CORRECT_OUTPUT_DIR/$type/$file_name.out 2>$MY_OUTPUT_DIR/$type/$file_name.correctRunError
                        fi

                        if [[ $debug == 1 ]] && [[ $(cat $MY_OUTPUT_DIR/$type/$file_name.correctRunError) != "" ]]
                        then
                            stop=1
                            clearAboveLine
                            echo -e "----->" "${red}Error generating correct output${clear}"
                            echo "Error generating correct output:"
                            cat $MY_OUTPUT_DIR/$type/$file_name.correctRunError
                            rm $MY_OUTPUT_DIR/$type/$file_name.correctRunError
                            code -g $INPUT_DIR/$type/$file_name.java
                        fi
                    fi

                    if [[ $stop == 0 ]]
                    then
                        if [[ $debug == 0 ]]
                        then
                            java $MY_OUTPUT_DIR/$type/$file_name.java > $MY_OUTPUT_DIR/$type/$file_name.out 2>/dev/null
                        else
                            java $MY_OUTPUT_DIR/$type/$file_name.java > $MY_OUTPUT_DIR/$type/$file_name.out 2>$MY_OUTPUT_DIR/$type/$file_name.runError
                        fi

                        clearAboveLine
                        if [[ $debug == 1 ]]; then
                            cat $MY_OUTPUT_DIR/$type/$file_name.runError
                            rm $MY_OUTPUT_DIR/$type/$file_name.runError
                        fi

                        if [[ $(diff $MY_OUTPUT_DIR/$type/$file_name.out $CORRECT_OUTPUT_DIR/$type/$file_name.out) == "" ]]
                        then
                            pass=$((pass+1))
                            echo -e "----->" "${green}Noice${clear}"
                        else
                            echo -e "----->" "${red}Wrong output${clear}"
                            if [[ $debug == 1 ]]; then
                                stop=1
                                echo "--------------"
                                echo "correct:" 
                                cat $CORRECT_OUTPUT_DIR/$type/$file_name.out
                                echo "--------------"
                                echo "mine:"
                                cat $MY_OUTPUT_DIR/$type/$file_name.out
                                code -g $INPUT_DIR/$type/$file_name.java
                                code -g $MY_OUTPUT_DIR/$type/$file_name.java
                            fi
                        fi
                    fi
                fi
            fi
        fi
    else
        clearAboveLine
        echo -e "File $INPUT_DIR/$type/$file_name.java" "${red}not found${clear}"
        if [[ $debug == 1 ]]; then
            stop=1
        fi
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
        if [[ $public == 1 ]]; then
            runOverArray $1 $INPUT_DIR/public/*
        fi

        if [[ $custom == 1 ]]; then
            runOverArray $1 $INPUT_DIR/custom/*
        fi

        for file in ${files[@]}
        do
            IFS='/' read -a name <<< "$file"
            type=${name[-2]}

            if [[ $type == "public" && $public == 0 ]]
            then
                $1 $file
            elif [[ $type == "custom" && $custom == 0 ]]
            then
                $1 $file
            elif [[ $type != "public" && $type != "custom" ]]
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

if [[ ! -f $exec.java ]]; then
    echo -e "Executable $exec.java ${red}not found${clear}"
    echo "Use -e flag to specify executable path"
    exit 1
fi

# display compile animation
displayCompile &
compile_pid=$!

# all preprocess before testing
find . -name "*.class" | xargs rm 2>/dev/null
javac $exec.java 2>/dev/null

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
    runOverAll BuritoToTacoJava

    echo "-----------------------------------------------------"
    printProgress $cnt $total $progress_len

    if [[ $notest == 0 ]]
    then
        echo ""
        echo "Processing done $pass / $total passed"
    fi

    if [[ $clear_f == 1 ]]
    then
        cd ..
        find . -name "*.class" | xargs rm 2>/dev/null
        cd P2
        rm -rf $MY_OUTPUT_DIR/public/* $MY_OUTPUT_DIR/custom/*
    fi
fi