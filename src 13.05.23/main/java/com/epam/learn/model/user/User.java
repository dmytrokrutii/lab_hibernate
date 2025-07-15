package com.epam.learn.model.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ToString.Exclude
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @ToString.Exclude
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ToString.Exclude
    @Column(nullable = false, unique = true)
    private String username;

    @ToString.Exclude
    @Column(nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
