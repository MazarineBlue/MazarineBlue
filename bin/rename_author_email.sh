#!/bin/sh

old="$1"; new="$2"
shift; shift

git filter-branch --commit-filter '
    if [ "$GIT_AUTHOR_EMAIL" = "$old" ]; then
        GIT_AUTHOR_EMAIL="$new";
        git commit-tree "$@";
    else
        git commit-tree "$@";
    fi' -- $@

