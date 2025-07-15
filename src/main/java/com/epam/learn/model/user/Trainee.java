package com.epam.learn.model.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainees")
@EqualsAndHashCode(callSuper = true)
public class Trainee extends User {

    @ToString.Exclude
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ToString.Exclude
    private String address;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
        name = "trainee_trainer",
        joinColumns = @JoinColumn(name = "trainee_id"),
        inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private Set<Trainer> trainers = new HashSet<>();
}
