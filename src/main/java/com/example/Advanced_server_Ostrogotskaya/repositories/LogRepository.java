package com.example.Advanced_server_Ostrogotskaya.repositories;

import com.example.Advanced_server_Ostrogotskaya.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {

}
