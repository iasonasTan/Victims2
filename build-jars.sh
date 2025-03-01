#!/bin/bash

cd src
javac main/Main.java
cd ..

#mkdir classes;
#find . -name "*.class" -type f -exec mv {} classes/ \;

jar cfe outputs/jars/Victims2.jar main/Main -C src . -C resources . -C classes .

find . -name "*.class" -type f -delete;
#//rmdir classes;