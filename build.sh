#!/bin/bash

cp src/*.java build/
cd build
rm *.class
javac -cp classpath.jar:. -bootclasspath classpath.jar JavaZip.java
rm *.java
avian JavaZip
