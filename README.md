# OpenTelemetry for Java Developers

This is the demo for my talk [OpenTelemetry for Java Developers](https://sched.co/1K40E) at [JCON Europe 2023](https://jcon.one/).

# Slides

[Google Slides](https://docs.google.com/presentation/d/1WFq8JPQhGluY518sjikA_OCmfsdxk5QrjcYJkGHUyP0/edit?usp=sharing).

# Demo

Compile the Java code (requires Java 17):

```sh
./mvnw clean package
```

Run the monitoring backend:

```sh
cd docker-compose/
docker-compose up
```

Run the hello world application:

```sh
./hello-world-app.sh
```

Run the greeting service:

```sh
# The script lets you interactively choose the example
./greeting-service.sh
```

Generate traffic:

```sh
watch curl -s http://localhost:8080
```

Open Grafana on [http://localhost:3000](http://localhost:3000). Default user is _admin_ with password _admin_.
