package integration_tests.annotation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.neo4j.Neo4jAutoConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EnableAutoConfiguration(exclude = {Neo4jAutoConfiguration.class, Neo4jRepositoriesAutoConfiguration.class, Neo4jDataAutoConfiguration.class})
public @interface DisableDatabaseAutoConfiguration {
}
