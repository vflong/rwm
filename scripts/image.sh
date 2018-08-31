#!/bin/bash

# feilong <weifeilong2013@gmail.com>

buildImage(){
    echo "==== starting to build ${project} image ===="
    cd target
    cp ../src/main/docker/Dockerfile .
    docker build -t 10.10.185.222/lifesense/${project} .
    docker push 10.10.185.222/lifesense/${project}
    echo "==== building ${project} image finished ===="
}
