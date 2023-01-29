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
    private Tracer tracer;

    @PostConstruct
    private void init() {
        tracer = GlobalOpenTelemetry.get()
                .getTracer("my-custom-scope");
    }

    public static void main(String[] args) {
        SpringApplication.run(GreetingService.class, args);
    }

    @GetMapping("/greeting")
    public String sayHello() throws InterruptedException {
        logger.info("Fabian says: The /greeting endpoint was called.");

        Thread.sleep((long) (Math.abs((random.nextGaussian() + 1.0) * 100.0)));

        Span.current().addEvent("Fabian's Event");
        Span.current().setAttribute("fabians.attr.key", "example attribute value");
        Span customSpan = tracer.spanBuilder("fabian's span")
                .setParent(Context.current().with(Span.current()))
                .startSpan();

        Thread.sleep((long) (Math.abs((random.nextGaussian() + 1.0) * 100.0)));

        customSpan.end();

        if (random.nextInt(10) < 3) {
            throw new RuntimeException("simulating an error");
        }
        return "Hello, World!\n";
    }
}
