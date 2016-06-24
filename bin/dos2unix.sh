#!/bin/sh

git filter-branch --tree-filter 'git ls-files -z | xargs -0 dos2unix' -- $@
