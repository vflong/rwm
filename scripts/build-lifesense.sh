#!/usr/bin/env bash

# =============== Please do not modify the following content =============== #
# source
. $(dirname $0)/image.sh
# go to script directory
cd $(dirname $0)

cd ..

echo "==== starting to build rwm ===="

mvn clean package -DskipTests

echo "==== building rwm finished ===="

buildImage rwm
