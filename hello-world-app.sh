#!/bin/bash

export OTEL_SERVICE_NAME=hello-world-app
export OTEL_LOGS_EXPORTER=otlp

if [ ! -f opentelemetry-javaagent.jar ] ; then
    curl -OL https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.27.0/opentelemetry-javaagent.jar
fi

java -javaagent:$(pwd)/opentelemetry-javaagent.jar -jar ./hello-world-app/target/hello-world-app-1.0.0-SNAPSHOT.jar "$@"
