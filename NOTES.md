# Notes

For the talk I didn't want to start and stop services between demos. So I ran multiple services at once:

```
./greeting-service.sh metrics-spring-2-otel-custom --server.port=8081
./greeting-service.sh metrics-spring-2-micrometer-custom --server.port=8082
./greeting-service.sh traces-spring-2-otel-custom --server.port=8083
./greeting-service.sh traces-spring-3-micrometer-custom --server.port=8084
./hello-world-app.sh --greeting.service.port=8081,8082,8083,8084
watch -n 0.5 curl -s http://localhost:8080
```
