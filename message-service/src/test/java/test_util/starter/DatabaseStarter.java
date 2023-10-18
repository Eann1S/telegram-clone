package test_util.starter;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers(parallel = true)
public interface DatabaseStarter {

    @Container
    @SuppressWarnings("resource")
    CassandraContainer<?> CASSANDRA_CONTAINER = new CassandraContainer<>(DockerImageName.parse("cassandra:4.1.3"))
            .withExposedPorts(9042);

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cassandra.contact-points", CASSANDRA_CONTAINER::getHost);
        registry.add("spring.cassandra.port", CASSANDRA_CONTAINER::getFirstMappedPort);
    }
}

