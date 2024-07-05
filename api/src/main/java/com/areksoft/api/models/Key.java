package com.areksoft.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "keys")
public class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "privateKey", nullable = false, unique = true)
    private String privateKey;

    @Column(name = "publicKey", nullable = false, unique = true)
    private String publicKey;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false )
    private User user;
}
