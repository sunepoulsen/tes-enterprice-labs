#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

cd "$SCRIPT_DIR" || exit

echo "Build docker image"

rm -rf build
mkdir build
cp -r ../dist build/dist

docker buildx build -t "$1":"$2" . || exit

cd - || exit
