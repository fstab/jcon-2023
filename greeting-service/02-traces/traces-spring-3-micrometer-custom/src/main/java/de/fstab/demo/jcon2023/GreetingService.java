package de.fstab.demo.jcon2023;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
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
    private final Random random = new Random(0);
    private final ObservationRegistry registry;

    public GreetingService(ObservationRegistry registry) {
        this.registry = registry;
    }

    public static void main(String[] args) {
        SpringApplication.run(GreetingService.class, args);
    }

    @GetMapping("/greeting")
    public String sayHello() throws InterruptedException {
        logger.info("Fabian says: The /greeting endpoint was called.");

        Observation obs = Observation
                .createNotStarted("fabians.observation", registry)
                .contextualName("fabian's test observation");
        try {
            obs.start();
            Thread.sleep((long) (Math.abs((random.nextGaussian() + 1.0) * 100.0)));
            obs.event(Observation.Event.of("fabians.test.event"));
            Thread.sleep((long) (Math.abs((random.nextGaussian() + 1.0) * 100.0)));
        } finally {
            obs.stop();
        }

        if (random.nextInt(10) < 3) {
            throw new RuntimeException("simulating an error");
        }
        return "Hello, World!\n";
    }
}
