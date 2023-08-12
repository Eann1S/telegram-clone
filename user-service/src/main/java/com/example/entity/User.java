package com.example.entity;

import com.example.generator.UserIdGenerator;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Getter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(generatorClass = UserIdGenerator.class)
    private String id;
    @Setter
    private String username;
    @Setter
    private String email;
    @Setter
    private String phoneNumber;
}
