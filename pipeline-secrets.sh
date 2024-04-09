#!/bin/bash

echo "Creating required secrets"
./gradlew :makeSecrets || exit
