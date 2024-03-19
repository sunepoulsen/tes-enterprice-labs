#!/bin/bash

dt-deps -n | grep =\>

cd tel-web/tel-web-module || exit
ncu | grep → || exit
cd - || exit
