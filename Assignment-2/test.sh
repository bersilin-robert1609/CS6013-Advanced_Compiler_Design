#!/bin/bash

# Compile the code
javac P1.java

SAMPLE_FOLDER="./samples"
TEST_FOLDER="./testcases/inputs"

# Iterate through all the files in the SAMPLE_FOLDER
for file in $SAMPLE_FOLDER/*; 
do
    echo "Running test on file: $file"

    java P1 < $file
done

# Iterate through all the files in the TEST_FOLDER
for file in $TEST_FOLDER/*; 
do
    echo "Running test on file: $file"

    java P1 < $file
done

# Delete the class files
find . -name "*.class" -type f -delete