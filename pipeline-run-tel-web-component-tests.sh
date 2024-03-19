#!/bin/bash

echo "Running TEL Web Module Component Tests"
./gradlew :tel-web:tel-web-component-tests:check -Ptel-web-component-tests --stacktrace || exit
