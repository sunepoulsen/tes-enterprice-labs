#!/bin/bash

echo "Building TEL TestData Module"
./gradlew :tel-testdata:tel-testdata-integrations:build --stacktrace || exit
./gradlew :tel-testdata:tel-testdata-integrations:JavaDoc --stacktrace || exit
./gradlew :tel-testdata:tel-testdata-module:build --stacktrace || exit
./gradlew :tel-testdata:tel-testdata-module:JavaDoc --stacktrace || exit
