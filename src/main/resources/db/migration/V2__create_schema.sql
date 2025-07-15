-- Create training_types table
CREATE TABLE training_types (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create users table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create trainers table
CREATE TABLE trainers (
    id UUID PRIMARY KEY REFERENCES users(id),
    specialization_id UUID NOT NULL REFERENCES training_types(id)
);

-- Create trainees table
CREATE TABLE trainees (
    id UUID PRIMARY KEY REFERENCES users(id),
    date_of_birth DATE,
    address VARCHAR(255)
);

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

-- Create trainee_trainer join table
CREATE TABLE trainee_trainer (
    trainee_id UUID NOT NULL REFERENCES trainees(id),
    trainer_id UUID NOT NULL REFERENCES trainers(id),
    PRIMARY KEY (trainee_id, trainer_id)
);
