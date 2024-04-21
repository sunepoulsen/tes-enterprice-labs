#!/bin/bash

echo "Running TEL TestData Module Stress Tests"
./gradlew :tel-testdata:tel-testdata-stress-tests:gatlingRun -Dstress.tests.profile=local --stacktrace || exit
