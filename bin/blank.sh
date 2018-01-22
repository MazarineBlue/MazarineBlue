#!/bin/sh

find . -name "*.java" -or -name "pom.xml" -exec egrep -l " +$" {} +
