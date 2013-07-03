#!/bin/bash

cp src/*.java testbuild/
cd testbuild
cp ../../changeavian/avian/build/linux-x86_64/classpath.jar .
rm *.class
javac -cp classpath.jar:. -bootclasspath classpath.jar JavaZip.java
rm *.java
avian JavaZip
