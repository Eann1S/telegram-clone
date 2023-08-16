package integration_tests.annotation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
public @interface DisableKafkaAutoConfiguration {
}
