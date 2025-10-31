#!/bin/sh

echo "Building lib-a" && (cd lib-a; ./mvnw install) && \
echo "Building lib-b" && (cd lib-b; ./mvnw install) && \
echo "Staring example-application" && (cd example-application; ./mvnw verify)