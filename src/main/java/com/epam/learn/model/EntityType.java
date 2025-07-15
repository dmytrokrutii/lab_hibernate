package com.epam.learn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityType {
    TRAINEE("Trainee"),
    TRAINER("Trainer"),
    TRAINING("Training"),
    TRAINING_TYPE("TrainingType");

    private final String name;
}
