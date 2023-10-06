package test_util.starter;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers(parallel = true)
@Transactional
public interface DatabaseStarter {

    @Container
    @SuppressWarnings("resource")
    Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>(DockerImageName.parse("neo4j:community"))
            .withoutAuthentication();

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
    }
}

