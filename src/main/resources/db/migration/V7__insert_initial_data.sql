-- Insert training types
INSERT INTO training_types (id, name) VALUES
('11111111-1111-1111-1111-111111111111', 'Cardio'),
('22222222-2222-2222-2222-222222222222', 'Strength'),
('33333333-3333-3333-3333-333333333333', 'Flexibility'),
('44444444-4444-4444-4444-444444444444', 'Balance');

-- Insert users for trainees
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES 
('55555555-5555-5555-5555-555555555555', 'John', 'Doe', 'john.doe', 'password123', true),
('66666666-6666-6666-6666-666666666666', 'Jane', 'Smith', 'jane.smith', 'password456', true);

-- Insert users for trainers
INSERT INTO users (id, first_name, last_name, username, password, is_active) VALUES 
('77777777-7777-7777-7777-777777777777', 'Alice', 'Brown', 'alice.brown', 'password789', true),
('88888888-8888-8888-8888-888888888888', 'Bob', 'Johnson', 'bob.johnson', 'passwordabc', true);

-- Insert trainees
INSERT INTO trainees (id, date_of_birth, address) VALUES 
('55555555-5555-5555-5555-555555555555', '1990-05-15', '123 Main St'),
('66666666-6666-6666-6666-666666666666', '1992-08-21', '456 Oak Ave');

-- Insert trainers
INSERT INTO trainers (id, specialization_id) VALUES 
('77777777-7777-7777-7777-777777777777', '11111111-1111-1111-1111-111111111111'),
('88888888-8888-8888-8888-888888888888', '22222222-2222-2222-2222-222222222222');

-- Insert trainings
INSERT INTO trainings (id, trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration) VALUES 
('99999999-9999-9999-9999-999999999999', '55555555-5555-5555-5555-555555555555', '77777777-7777-7777-7777-777777777777', 'Morning Cardio', '11111111-1111-1111-1111-111111111111', '2023-01-15 08:00:00', 60),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '66666666-6666-6666-6666-666666666666', '88888888-8888-8888-8888-888888888888', 'Evening Strength', '22222222-2222-2222-2222-222222222222', '2023-01-16 18:00:00', 45);