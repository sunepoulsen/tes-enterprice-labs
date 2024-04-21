#!/bin/bash

echo "Deploy local with docker compose"
./gradlew :deployment:docker:deploy --stacktrace || exit

echo "Execute deployment tests"
./gradlew :deployment:deployment-tests:clean  --stacktrace
./gradlew :deployment:deployment-tests:check -Pdeployment-tests -Ddeployment.test.profile=local --stacktrace

echo "Undeploy local with docker compose"
./gradlew :deployment:docker:undeploy --stacktrace || exit
