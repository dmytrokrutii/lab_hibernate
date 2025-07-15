-- Create trainings table
CREATE TABLE trainings (
    id UUID PRIMARY KEY,
    trainee_id UUID NOT NULL REFERENCES trainees(id),
    trainer_id UUID NOT NULL REFERENCES trainers(id),
    training_name VARCHAR(255) NOT NULL,
    training_type_id UUID NOT NULL REFERENCES training_types(id),
    training_date TIMESTAMP NOT NULL,
    training_duration INTEGER NOT NULL
);