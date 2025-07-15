-- Create trainee_trainer table for many-to-many relationship between trainees and trainers
CREATE TABLE trainee_trainer (
    trainee_id UUID NOT NULL REFERENCES trainees(id),
    trainer_id UUID NOT NULL REFERENCES trainers(id),
    PRIMARY KEY (trainee_id, trainer_id)
);