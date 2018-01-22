#!/bin/sh

du -sh .git
git reflog expire --all --expire-unreachable=now --rewrite --updateref
git fsck
git prune
git gc --aggressive --prune=now --force
du -sh .git
