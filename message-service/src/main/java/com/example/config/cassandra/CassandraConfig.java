package com.example.config.cassandra;

import com.example.entity.Message;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.List;

@Getter
@Setter
@Configuration
@EnableCassandraRepositories
@EnableConfigurationProperties
@ConfigurationProperties("spring.cassandra")
public class CassandraConfig extends AbstractCassandraConfiguration {

    private String contactPoints;
    private int port;
    private String username;
    private String password;
    private String keyspaceName;
    private String localDataCenter;
    private String schemaAction;

    @NotNull
    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification createKeyspaceSpecification = CreateKeyspaceSpecification.createKeyspace(keyspaceName)
                .ifNotExists()
                .withSimpleReplication()
                .with(KeyspaceOption.DURABLE_WRITES, true);
        return List.of(createKeyspaceSpecification);
    }

    @NotNull
    @Override
    public String[] getEntityBasePackages() {
        return new String[]{Message.class.getPackageName()};
    }

    @NotNull
    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.valueOf(schemaAction);
    }


}
