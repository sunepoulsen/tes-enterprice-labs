#!/bin/bash

echo "Building the TEL Web Image"

./gradlew :tel-web:tel-web-docker-image:clean --stacktrace || exit
./gradlew :tel-web:tel-web-docker-image:buildImage --stacktrace || exit
