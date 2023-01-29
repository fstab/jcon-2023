package de.fstab.demo.jcon2023;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@SpringBootApplication
@RestController
public class GreetingService {

    private final Logger logger = LoggerFactory.getLogger(GreetingService.class);
    private final Random random = new Random(1);
    private final MeterRegistry registry;

    public GreetingService(MeterRegistry registry) {
        this.registry = registry;
    }

    public static void main(String[] args) {
        SpringApplication.run(GreetingService.class, args);
    }

    @GetMapping("/greeting")
    public String sayHello() throws InterruptedException {
        logger.info("Fabian says: The /greeting endpoint was called.");

        registry.counter("greetings.total").increment();

        Thread.sleep((long) (Math.abs((random.nextGaussian() + 1.0) * 100.0)));
        Thread.sleep((long) (Math.abs((random.nextGaussian() + 1.0) * 100.0)));
        if (random.nextInt(10) < 3) {
            throw new RuntimeException("simulating an error");
        }
        return "Hello, World!\n";
    }
}
