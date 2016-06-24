#!/bin/sh

IFS=$'\n';

echo "All sizes are in kB's. The pack column is the size of the object, compressed inside the pack file."
echo

xxx=$1
if [ ! $1 ]; then
	xxx=10
fi
echo $xxxx

objects=`git verify-pack -v .git/objects/pack/pack-*idx | grep -v chain | sort -k3nr | head -n $xxx`

header="size,pack,SHA,location"
output="$header"
i=0
for y in $objects; do
	size=$((`echo $y | cut -f 5 -d ' '` / 1024))
	pack=$((`echo $y | cut -f 6 -d ' '` / 1024))
	sha=`echo $y | cut -f 1 -d ' '`
	location=`git rev-list --all --objects | grep $sha`
	output="$output\n$size,$pack,$location"
done

echo -e $output | column -t -s ', '

# Only performe the following commands if you have a very good reason for it!!!

# Reclaim space
# rm -rf .git/refs/original
# git reflog expire --expire=now --all
# git gc --prune=now
# git gc --aggressive --prune=now

# Push the cleaned repository
# git push origin --force --all
# git push origin --force --tags

# Tell your teammates
