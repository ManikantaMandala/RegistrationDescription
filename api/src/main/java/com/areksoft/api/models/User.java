package com.areksoft.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "mobile", nullable = false, unique = true)
    private String mobile;

    public User(){ }

    public User(String username, String password, String email, String mobile){
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
    }

}
