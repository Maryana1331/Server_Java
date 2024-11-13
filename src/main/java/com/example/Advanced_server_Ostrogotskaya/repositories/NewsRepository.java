package com.example.Advanced_server_Ostrogotskaya.repositories;

import com.example.Advanced_server_Ostrogotskaya.entities.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NewsRepository extends JpaRepository <NewsEntity, Long> {
       List<NewsEntity> findByUserEntityId(UUID id);

       @Query("SELECT DISTINCT n\n" +
               "     FROM NewsEntity n\n" +
               "     LEFT JOIN n.userEntity u\n" +
               "     LEFT JOIN n.tags t\n" +
               "WHERE (:username IS NULL OR u.name ILIKE %:username%)\n" +
               "     AND (:keywords IS NULL\n" +
               "          OR (n.title ILIKE %:keywords% OR " +
               "n.description ILIKE %:keywords% OR t.title ILIKE %:keywords%))\n" +
               "     AND (:tags IS NULL OR t.title IN :tags)\n")
       Page<NewsEntity> findNewsEntityByUsernameOrKeywordsOrTags(@Param("username") String username,
                                                                 @Param("keywords") String keywords,
                                                                 @Param("tags") List<String> tags,
                                                                 Pageable pageable);

}
