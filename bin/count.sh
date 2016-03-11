#!/bin/sh

#
# Count the source code lines of a project.
#
# For each module/directory a line count and average per file is shown.
# The first three columns show the line cound and the last tree columns
# shows the averages. The first number contains the count/average of the
# main source code, the second numer contains the test source code and
# the thirth column contains the subtotal.
#
# Usage count.sh <directories>
#

max_title=44
max_lines=6
max_avg=3
total="Grand total..."

#
# $1 - text
# $2 - space
# $3 - fill
#
printFillSpace() {
	i=`echo -n "$1" | awk "{print length}"`
	while [ $i -lt $2 ]; do
		echo -n "$3"
		i=$(($i + 1))
	done
}

#
# $1 - title
# $2 - space
#
printTitle() {
	echo -n "$1"
	printFillSpace "$1" $2 '.'
}

#
# $1 - count
# $2 - space
#
printLines() {
	printFillSpace "$1" $2 ' '
	echo -n "$1"
}

#
# $1 - lines
# $2 - files
# $3 - space
#
printAverage() {
	if [ $2 -gt 0 ]; then
		avg=$(($1 / $2))
	else
		avg=0
	fi
	printFillSpace "$avg" $3 ' '
	echo -n "$avg"
}

#
# $1 - title
# $2 - main line count
# $3 - test line count
#
printLine() {
	sum=$(($2 + $3))
	files=$(($4 + $5))
	printTitle "$1" $max_title
	printLines "$2" $max_lines
	printLines "$3" $max_lines
	printLines " $sum" $max_lines
	echo -n "  -  "
	printAverage $2 $4 $max_avg
	echo -n " "
	printAverage $3 $5 $max_avg
	echo -n " "
	printAverage $sum $files $max_avg
	echo ""
}

#
# $1 - Module/directory
#
getLinesCount() {
	lines=`find $1 -name "*.java" 2> /dev/null | xargs wc | tail -n 1 | awk '{print $1}'`
	if [ ! $lines ]; then
		lines=0
	fi
}

#
# $1 - Module/directory
#
getFilesCount() {
	files=`find $1 -name "*.java" 2> /dev/null | wc | tail -n 1 | awk '{print $1}'`
	if [ ! $files ]; then
		files=0
	fi
}

printTable() {
	main_lines_sum=0
	main_files_sum=0
	test_lines_sum=0
	test_files_sum=0
	n=0

	while [ $# -ne 0 ] ; do
#		mkdir -p "$1/src/main/java" "$1/src/test/java" > /dev/null

		getLinesCount "$1/src/main/java"; main_lines=$lines
		getLinesCount "$1/src/test/java"; test_lines=$lines
		getFilesCount "$1/src/main/java"; main_files=$files
		getFilesCount "$1/src/test/java"; test_files=$files

		main_lines_sum=$(($main_lines_sum + $main_lines))
		main_files_sum=$(($main_files_sum + $main_files))
		test_lines_sum=$(($test_lines_sum + $test_lines))
		test_files_sum=$(($test_files_sum + $test_files))
		
		printLine "$1" $main_lines $test_lines $main_files $test_files

		n=$(($n + 1))
#		rmdir "$1/src/main/java" "$1/src/test/java" > /dev/null
#		rmdir "$1/src/main" "$1/src/test" "$1/src" > /dev/null
		shift
	done

	printLine "$total" $main_lines_sum $test_lines_sum $main_files_sum $test_files_sum
}

# Go!
printTable $@
echo "Modules: $#"

