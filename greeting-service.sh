#!/bin/bash

if [ ! -f opentelemetry-javaagent.jar ] ; then
    curl -OL https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.27.0/opentelemetry-javaagent.jar
fi

run_greeting_service() {
    case $1 in
        metrics-*-otel-*)
            export OTEL_SERVICE_NAME=$1
            export OTEL_LOGS_EXPORTER=otlp
            java \
                -Dotel.metric.export.interval=500 \
                -Dotel.bsp.schedule.delay=500 \
                "-javaagent:$(pwd)/opentelemetry-javaagent.jar" \
                -jar "./greeting-service/01-metrics/$1/target/$1-1.0.0-SNAPSHOT.jar" "${@:2}"
            ;;
        metrics-*-micrometer-*)
            # Use otel.instrumentation.micrometer.base-time-unit=ms because currently OTel's http_server_duration is also still in milliseconds.
            export OTEL_SERVICE_NAME=$1
            export OTEL_LOGS_EXPORTER=otlp
            java \
                -Dotel.metric.export.interval=500 \
                -Dotel.bsp.schedule.delay=500 \
                -Dotel.instrumentation.micrometer.base-time-unit=ms \
                "-javaagent:$(pwd)/opentelemetry-javaagent.jar" \
                -jar "./greeting-service/01-metrics/$1/target/$1-1.0.0-SNAPSHOT.jar" "${@:2}"
            ;;
        traces-*-otel-*)
            export OTEL_SERVICE_NAME=$1
            export OTEL_LOGS_EXPORTER=otlp
            java \
                -Dotel.metric.export.interval=500 \
                -Dotel.bsp.schedule.delay=500 \
                "-javaagent:$(pwd)/opentelemetry-javaagent.jar" \
                -jar "./greeting-service/02-traces/$1/target/$1-1.0.0-SNAPSHOT.jar" "${@:2}"
            ;;
        traces-*-micrometer-*)
            java -jar "./greeting-service/02-traces/$1/target/$1-1.0.0-SNAPSHOT.jar" "${@:2}"
            ;;
        logs-*-otel)
            # Use otel.instrumentation.micrometer.base-time-unit=ms because currently OTel's http_server_duration is also still in milliseconds.
            export OTEL_SERVICE_NAME=$1
            export OTEL_LOGS_EXPORTER=otlp
            java \
                -Dotel.metric.export.interval=500 \
                -Dotel.bsp.schedule.delay=500 \
                -Dotel.instrumentation.micrometer.base-time-unit=ms \
                "-javaagent:$(pwd)/opentelemetry-javaagent.jar" \
                -jar "./greeting-service/03-logs/$1/target/$1-1.0.0-SNAPSHOT.jar" "${@:2}"
            ;;
        logs-*-micrometer)
            java -jar "./greeting-service/03-logs/$1/target/$1-1.0.0-SNAPSHOT.jar" "${@:2}"
            ;;
        "Quit")
            break
            ;;
        *) echo "invalid option";;
    esac
}

if [ $# -eq 0 ] ; then
    PS3='Please select the example: '
    options=(
      "metrics-spring-2-otel-out-of-the-box"
      "metrics-spring-2-micrometer-out-of-the-box"
      "metrics-spring-2-otel-custom"
      "metrics-spring-2-micrometer-custom"
      "traces-spring-2-otel-out-of-the-box"
      "traces-spring-3-micrometer-out-of-the-box"
      "traces-spring-2-otel-custom"
      "traces-spring-3-micrometer-custom"
      "logs-spring-2-otel"
      "logs-spring-3-micrometer"
      "Quit"
    )
    select opt in "${options[@]}"
    do
      run_greeting_service "$opt" "$@"
    done
else
    run_greeting_service "$@"
fi
