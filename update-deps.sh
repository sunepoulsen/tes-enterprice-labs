#!/bin/bash

dt-deps -u || exit

cd tel-web/tel-web-module || exit
ncu -u || exit
cd - || exit

./pipeline.sh
