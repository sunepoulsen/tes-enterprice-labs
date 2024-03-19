#!/bin/bash

echo "Running System Tests"
./gradlew :system-tests:check -Psystem-tests --stacktrace || exit
