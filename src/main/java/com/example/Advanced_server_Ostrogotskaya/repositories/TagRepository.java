package com.example.Advanced_server_Ostrogotskaya.repositories;

import com.example.Advanced_server_Ostrogotskaya.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
     TagEntity findByTitle(String title);
}
