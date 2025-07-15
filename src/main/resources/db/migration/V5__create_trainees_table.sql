-- Create trainees table
CREATE TABLE trainees (
    id UUID PRIMARY KEY REFERENCES users(id),
    date_of_birth DATE,
    address VARCHAR(255)
);