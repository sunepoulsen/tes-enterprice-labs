#!/bin/bash

echo "Building TEL TestData Module"
./gradlew :tel-testdata:tel-testdata-module:build --stacktrace || exit
