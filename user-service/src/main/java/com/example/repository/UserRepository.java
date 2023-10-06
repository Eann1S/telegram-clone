package com.example.repository;

import com.example.entity.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
