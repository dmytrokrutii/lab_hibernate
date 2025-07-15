-- Create trainers table
CREATE TABLE trainers (
    id UUID PRIMARY KEY REFERENCES users(id),
    specialization_id UUID NOT NULL REFERENCES training_types(id)
);