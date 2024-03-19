#!/bin/bash

echo "Analyze code in repository"
./gradlew dependencyCheckAggregate || exit
./gradlew jacocoTestReport || exit
./gradlew sonar || exit
