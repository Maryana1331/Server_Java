package com.example.Advanced_server_Ostrogotskaya.repositories;

import com.example.Advanced_server_Ostrogotskaya.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByEmail(String email);

    Optional<UserEntity> findById(UUID userId);

    Optional<UserEntity> findByEmail(String email);

}
