#!/bin/sh

echo
echo "Test01:"
javac -cp ast-transform-study-1.0-SNAPSHOT.jar Test01.java

echo "-----------------------"

echo
echo "Test02:"
javac -cp ast-transform-study-1.0-SNAPSHOT.jar Test02.java

echo

rm -rf *.class

