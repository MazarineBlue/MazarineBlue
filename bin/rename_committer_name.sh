#!/bin/sh

old="$1"; new="$2"
shift; shift

git filter-branch --commit-filter '
    if [ "$GIT_COMMITTER_NAME" = "$old" ]; then
        GIT_COMMITTER_NAME="$new";
        git commit-tree "$@";
    else
        git commit-tree "$@";
    fi' -- $@

