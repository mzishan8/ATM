#!/bin/bash

./gradlew clean build test
echo "Starting ATM cli...."
 java -jar build/libs/ATM-1.0-SNAPSHOT.jar
