package com.example.Advanced_server_Ostrogotskaya.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "log_entity", schema = "schema")
public class LogEntity {
    @Id
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    private String method;

    private String uri;

    private String status;

    @Column(name = "userName")
    private String  userName;

    @Column(name = "errorMessage")
    private String errorMessage;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}
