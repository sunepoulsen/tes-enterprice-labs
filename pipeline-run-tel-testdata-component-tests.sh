#!/bin/bash

echo "Running TEL TestData Module Component Tests"
./gradlew :tel-testdata:tel-testdata-component-tests:check -Ptel-testdata-component-tests --stacktrace || exit
