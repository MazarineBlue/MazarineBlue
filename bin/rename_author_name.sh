#!/bin/sh

old="$1"; new="$2"
shift; shift

git filter-branch --commit-filter '
    if [ "$GIT_AUTHOR_NAME" = "$old" ]; then
        GIT_AUTHOR_NAME="$new";
        git commit-tree "$@";
    else
        git commit-tree "$@";
    fi' -- $@

