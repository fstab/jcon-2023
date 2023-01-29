package de.fstab.demo.jcon2023;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Random;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

@SpringBootApplication
@RestController
public class GreetingService {

    private final Logger logger = LoggerFactory.getLogger(GreetingService.class);
    private final Random random = new Random(1);

    private LongCounter counter;

    @PostConstruct
    private void init() {
        counter = GlobalOpenTelemetry.get()
                .meterBuilder("my-custom-instrumentation")
                .setInstrumentationVersion("1.0.0")
                .build()
                .counterBuilder("my.custom.counter")
                .setDescription("Custom Counter")
                .setUnit("1")
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GreetingService.class, args);
    }

    @GetMapping("/greeting")
    public String sayHello() throws InterruptedException {

        logger.info("Fabian says: The /greeting endpoint was called.");
        Thread.sleep((long) (Math.abs((random.nextGaussian() + 1.0) * 100.0)));

        counter.add(1, Attributes.of(stringKey("fabians.custom.key"), "example value"));

        Thread.sleep((long) (Math.abs((random.nextGaussian() + 1.0) * 100.0)));

        if (random.nextInt(10) < 3) {
            throw new RuntimeException("simulating an error");
        }
        return "Hello, World!\n";
    }
}
