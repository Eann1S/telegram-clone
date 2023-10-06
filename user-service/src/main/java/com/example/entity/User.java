package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private Long id;
    private String email;
    private String username;
}
