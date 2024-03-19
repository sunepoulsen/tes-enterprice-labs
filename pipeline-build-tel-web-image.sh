#!/bin/bash

echo "Building the TEL Web Image"
./gradlew :tel-web:tel-web-module:buildImage --stacktrace || exit
