#!/bin/sh

mvn versions::display-dependency-updates | grep \\\-\> | sort | uniq
