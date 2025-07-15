package com.epam.learn.console;

import com.epam.learn.model.PagedResponse;
import com.epam.learn.model.training.Training;
import com.epam.learn.model.training.TrainingType;
import com.epam.learn.model.user.Trainee;
import com.epam.learn.model.user.Trainer;
import com.epam.learn.service.TraineeService;
import com.epam.learn.service.TrainerService;
import com.epam.learn.service.TrainingService;
import com.epam.learn.service.TrainingTypeService;
import com.epam.learn.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Console application for interacting with the training system.
 * This class provides a command-line interface for users to perform operations
 * such as creating profiles, authenticating, changing passwords, etc.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConsoleApplication implements CommandLineRunner {

    private final AuthService authService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String currentUsername = null;
    private boolean isAuthenticated = false;

    @Override
    public void run(String... args) {
        LOGGER.info("Starting console application");

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> createTraineeProfile();
                    case 2 -> createTrainerProfile();
                    case 3 -> authenticate();
                    case 4 -> {
                        if (checkAuthentication()) {
                            changePassword();
                        }
                    }
                    case 5 -> {
                        if (checkAuthentication()) {
                            updateTraineeProfile();
                        }
                    }
                    case 6 -> {
                        if (checkAuthentication()) {
                            updateTrainerProfile();
                        }
                    }
                    case 7 -> {
                        if (checkAuthentication()) {
                            toggleTraineeStatus();
                        }
                    }
                    case 8 -> {
                        if (checkAuthentication()) {
                            toggleTrainerStatus();
                        }
                    }
                    case 9 -> {
                        if (checkAuthentication()) {
                            addTraining();
                        }
                    }
                    case 10 -> {
                        if (checkAuthentication()) {
                            updateTraineesTrainersList();
                        }
                    }
                    case 11 -> {
                        if (checkAuthentication()) {
                            getTraineeTrainingsList();
                        }
                    }
                    case 12 -> {
                        if (checkAuthentication()) {
                            getTrainerTrainingsList();
                        }
                    }
                    case 13 -> {
                        if (checkAuthentication()) {
                            getTraineesByTrainerUsername();
                        }
                    }
                    case 0 -> {
                        running = false;
                        LOGGER.info("Exiting application");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                LOGGER.error("Error processing request", e);
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }

        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n===== Training Application =====");
        if (isAuthenticated) {
            System.out.println("Logged in as: " + currentUsername);
        }
        System.out.println("1. Create Trainee Profile");
        System.out.println("2. Create Trainer Profile");
        System.out.println("3. Login");

        if (isAuthenticated) {
            System.out.println("4. Change Password");
            System.out.println("5. Update Trainee Profile");
            System.out.println("6. Update Trainer Profile");
            System.out.println("7. Activate/Deactivate Trainee");
            System.out.println("8. Activate/Deactivate Trainer");
            System.out.println("9. Add Training");
            System.out.println("10. Update Trainee's Trainers List");
            System.out.println("11. Get Trainee Trainings List");
            System.out.println("12. Get Trainer Trainings List");
            System.out.println("13. Get Trainees by Trainer Username");
        }

        System.out.println("0. Exit");
        System.out.println("===============================");
    }

    private boolean checkAuthentication() {
        if (!isAuthenticated) {
            System.out.println("You must be logged in to perform this action.");
            return false;
        }
        return true;
    }

    private void createTraineeProfile() {
        System.out.println("\n----- Create Trainee Profile -----");

        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");

        LocalDate dateOfBirth = null;
        String dateStr = getStringInput("Enter date of birth (yyyy-MM-dd, optional): ");
        if (!dateStr.isEmpty()) {
            try {
                dateOfBirth = LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Date of birth will be set to null.");
            }
        }

        String address = getStringInput("Enter address (optional): ");
        if (address.isEmpty()) {
            address = null;
        }

        Trainee trainee = new Trainee();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);

        traineeService.create(trainee);

        System.out.println("\nTrainee profile created successfully!");
        System.out.println("Username: " + trainee.getUsername());
        System.out.println("Password: " + trainee.getPassword());
    }

    private void createTrainerProfile() {
        System.out.println("\n----- Create Trainer Profile -----");

        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");

        // In a real application, you would fetch available training types from the database
        // and let the user select one. For simplicity, we'll create a new one here.
        String specializationName = getStringInput("Enter specialization: ");
        TrainingType specialization = new TrainingType();
        specialization.setName(specializationName);

        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setSpecialization(specialization);

        trainerService.create(trainer);

        System.out.println("\nTrainer profile created successfully!");
        System.out.println("Username: " + trainer.getUsername());
        System.out.println("Password: " + trainer.getPassword());
    }

    private void authenticate() {
        System.out.println("\n----- Login -----");

        String username = getStringInput("Enter username: ");
        String password = getStringInput("Enter password: ");

        if (authService.authenticate(username, password)) {
            isAuthenticated = true;
            currentUsername = username;
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private void changePassword() {
        System.out.println("\n----- Change Password -----");

        String oldPassword = getStringInput("Enter current password: ");
        String newPassword = getStringInput("Enter new password: ");

        if (!authService.isPasswordStrong(newPassword)) {
            System.out.println("Password is not strong enough. It should have:");
            System.out.println("- At least 8 characters");
            System.out.println("- At least one uppercase letter");
            System.out.println("- At least one lowercase letter");
            System.out.println("- At least one digit");
            System.out.println("- At least one special character");
            return;
        }

        if (authService.changePassword(currentUsername, oldPassword, newPassword)) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Please check your current password.");
        }
    }

    private void updateTraineeProfile() {
        System.out.println("\n----- Update Trainee Profile -----");

        // For simplicity, we'll assume the current user is a trainee
        // In a real application, you would check the user type

        Trainee trainee = traineeService.getTraineeByUsername(currentUsername);
        if (trainee == null) {
            System.out.println("Trainee profile not found.");
            return;
        }

        System.out.println("Current profile:");
        System.out.println("First name: " + trainee.getFirstName());
        System.out.println("Last name: " + trainee.getLastName());
        System.out.println("Date of birth: " + (trainee.getDateOfBirth() != null ? trainee.getDateOfBirth().format(dateFormatter) : "Not set"));
        System.out.println("Address: " + trainee.getAddress());

        String firstName = getStringInput("Enter new first name (or press Enter to keep current): ");
        if (!firstName.isEmpty()) {
            trainee.setFirstName(firstName);
        }

        String lastName = getStringInput("Enter new last name (or press Enter to keep current): ");
        if (!lastName.isEmpty()) {
            trainee.setLastName(lastName);
        }

        String dateStr = getStringInput("Enter new date of birth (yyyy-MM-dd) (or press Enter to keep current, 'clear' to remove): ");
        if (!dateStr.isEmpty()) {
            if (dateStr.equalsIgnoreCase("clear")) {
                trainee.setDateOfBirth(null);
                System.out.println("Date of birth cleared.");
            } else {
                try {
                    LocalDate dateOfBirth = LocalDate.parse(dateStr, dateFormatter);
                    trainee.setDateOfBirth(dateOfBirth);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Date of birth not updated.");
                }
            }
        }

        String address = getStringInput("Enter new address (or press Enter to keep current, 'clear' to remove): ");
        if (!address.isEmpty()) {
            if (address.equalsIgnoreCase("clear")) {
                trainee.setAddress(null);
                System.out.println("Address cleared.");
            } else {
                trainee.setAddress(address);
            }
        }

        traineeService.update(trainee.getId(), trainee);
        System.out.println("Trainee profile updated successfully!");
    }

    private void updateTrainerProfile() {
        System.out.println("\n----- Update Trainer Profile -----");

        // For simplicity, we'll assume the current user is a trainer
        // In a real application, you would check the user type

        Trainer trainer = trainerService.getTrainerByUsername(currentUsername);
        if (trainer == null) {
            System.out.println("Trainer profile not found.");
            return;
        }

        System.out.println("Current profile:");
        System.out.println("First name: " + trainer.getFirstName());
        System.out.println("Last name: " + trainer.getLastName());
        System.out.println("Specialization: " + (trainer.getSpecialization() != null ? trainer.getSpecialization().getName() : "Not set"));

        String firstName = getStringInput("Enter new first name (or press Enter to keep current): ");
        if (!firstName.isEmpty()) {
            trainer.setFirstName(firstName);
        }

        String lastName = getStringInput("Enter new last name (or press Enter to keep current): ");
        if (!lastName.isEmpty()) {
            trainer.setLastName(lastName);
        }

        String specializationName = getStringInput("Enter new specialization (or press Enter to keep current): ");
        if (!specializationName.isEmpty()) {
            // Keep the existing specialization object but update its name
            if (trainer.getSpecialization() != null) {
                trainer.getSpecialization().setName(specializationName);
            } else {
                // If no specialization exists, create a new one with the same ID
                TrainingType specialization = new TrainingType();
                specialization.setName(specializationName);
                trainer.setSpecialization(specialization);
            }
        }

        trainerService.update(trainer.getId(), trainer);
        System.out.println("Trainer profile updated successfully!");
    }

    private void toggleTraineeStatus() {
        System.out.println("\n----- Activate/Deactivate Trainee -----");

        String username = getStringInput("Enter trainee username: ");

        Trainee trainee = traineeService.getTraineeByUsername(username);
        if (trainee == null) {
            System.out.println("Trainee not found.");
            return;
        }

        boolean newStatus = !trainee.isActive();
        traineeService.updateTraineeStatus(username, newStatus);

        System.out.println("Trainee status " + (newStatus ? "activated" : "deactivated") + " successfully!");
    }

    private void toggleTrainerStatus() {
        System.out.println("\n----- Activate/Deactivate Trainer -----");

        String username = getStringInput("Enter trainer username: ");

        Trainer trainer = trainerService.getTrainerByUsername(username);
        if (trainer == null) {
            System.out.println("Trainer not found.");
            return;
        }

        boolean newStatus = !trainer.isActive();
        trainerService.updateTrainerStatus(username, newStatus);

        System.out.println("Trainer status " + (newStatus ? "activated" : "deactivated") + " successfully!");
    }


    private void addTraining() {
        System.out.println("\n----- Add Training -----");

        String traineeName = getStringInput("Enter trainee username: ");
        String trainerName = getStringInput("Enter trainer username: ");
        String trainingName = getStringInput("Enter training name: ");
        String trainingType = getStringInput("Enter training type: ");
        String dateStr = getStringInput("Enter training date (yyyy-MM-dd): ");
        String timeStr = getStringInput("Enter training time (HH:mm): ");
        int duration = getIntInput("Enter training duration (in minutes): ");

        try {
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time = LocalTime.parse(timeStr, timeFormatter);

            LocalDateTime dateTime = date.atTime(time);

            Training training = new Training(
                    UUID.randomUUID(),
                    traineeService.getTraineeByUsername(traineeName),
                    trainerService.getTrainerByUsername(trainerName),
                    trainingName,
                    trainingTypeService.findOrCreate(trainingType),
                    dateTime,
                    duration
            );

            trainingService.create(training);

            System.out.println("Training added successfully!");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date or time format. Please use yyyy-MM-dd for the date and HH:mm for the time.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateTraineesTrainersList() {
        System.out.println("\n----- Update Trainee's Trainers List -----");

        String username = getStringInput("Enter trainee username: ");

        // Get available trainers not assigned to the trainee
        System.out.println("\nAvailable trainers:");
        PagedResponse<Trainer> availableTrainers = trainerService.getTrainersNotAssignedToTrainee(username, 0, 100);

        if (availableTrainers.content().isEmpty()) {
            System.out.println("No available trainers found.");
            return;
        }

        // Display available trainers
        int index = 1;
        for (Trainer trainer : availableTrainers.content()) {
            System.out.println(index + ". " + trainer.getFirstName() + " " + trainer.getLastName() + 
                    " (Specialization: " + trainer.getSpecialization().getName() + ")");
            index++;
        }

        // Get trainer selections
        System.out.println("\nEnter the numbers of trainers to assign (comma-separated, e.g., 1,3,5): ");
        String selection = getStringInput("");

        if (selection.isEmpty()) {
            System.out.println("No trainers selected. Operation cancelled.");
            return;
        }

        // Parse selections and get trainer IDs
        List<UUID> trainerIds = new ArrayList<>();
        try {
            String[] selections = selection.split(",");
            for (String sel : selections) {
                int selectedIndex = Integer.parseInt(sel.trim()) - 1;
                if (selectedIndex >= 0 && selectedIndex < availableTrainers.content().size()) {
                    trainerIds.add(availableTrainers.content().get(selectedIndex).getId());
                } else {
                    System.out.println("Invalid selection: " + sel + ". Ignoring.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please use comma-separated numbers.");
            return;
        }

        if (trainerIds.isEmpty()) {
            System.out.println("No valid trainers selected. Operation cancelled.");
            return;
        }

        // Update trainee's trainers list
        try {
            traineeService.updateTrainersList(username, trainerIds);
            System.out.println("Trainee's trainers list updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating trainee's trainers list: " + e.getMessage());
        }
    }

    private void getTraineeTrainingsList() {
        System.out.println("\n----- Get Trainee Trainings List -----");

        String username = getStringInput("Enter trainee username: ");

        // Get filter criteria
        System.out.println("\nEnter filter criteria (leave empty to skip):");


        LocalDate fromDate = parseDateInput("From date (yyyy-MM-dd): ", dateFormatter);
        LocalDate toDate = parseDateInput("To date (yyyy-MM-dd): ", dateFormatter);

        String trainerName = getStringInput("Trainer name: ");
        if (trainerName.isEmpty()) {
            trainerName = null;
        }

        String trainingType = getStringInput("Training type: ");
        if (trainingType.isEmpty()) {
            trainingType = null;
        }

        // Get trainings
        PagedResponse<Training> trainings = trainingService.getTraineeTrainings(
                username, fromDate, toDate, trainerName, trainingType, 0, 100);

        // Display trainings
        System.out.println("\nTrainings:");
        if (trainings.content().isEmpty()) {
            System.out.println("No trainings found.");
            return;
        }

        for (Training training : trainings.content()) {
            System.out.println("Name: " + training.getTrainingName());
            System.out.println("Type: " + training.getTrainingType().getName());
            System.out.println("Trainer: " + training.getTrainer().getFirstName() + " " + training.getTrainer().getLastName());
            System.out.println("Date: " + training.getTrainingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System.out.println("Duration: " + training.getTrainingDuration() + " minutes");
            System.out.println("-----");
        }
    }

    private void getTrainerTrainingsList() {
        System.out.println("\n----- Get Trainer Trainings List -----");

        String username = getStringInput("Enter trainer username: ");

        // Get filter criteria
        System.out.println("\nEnter filter criteria (leave empty to skip):");

        System.out.println("\nEnter filter criteria (leave empty to skip):");

        LocalDate fromDate = parseDateInput("From date (yyyy-MM-dd): ", dateFormatter);
        LocalDate toDate = parseDateInput("To date (yyyy-MM-dd): ", dateFormatter);


        String traineeName = getStringInput("Trainee name: ");
        if (traineeName.isEmpty()) {
            traineeName = null;
        }

        // Get trainings
        PagedResponse<Training> trainings = trainingService.getTrainerTrainings(
                username, fromDate, toDate, traineeName, 0, 100);

        // Display trainings
        System.out.println("\nTrainings:");
        if (trainings.content().isEmpty()) {
            System.out.println("No trainings found.");
            return;
        }

        for (Training training : trainings.content()) {
            System.out.println("Name: " + training.getTrainingName());
            System.out.println("Type: " + training.getTrainingType().getName());
            System.out.println("Trainee: " + training.getTrainee().getFirstName() + " " + training.getTrainee().getLastName());
            System.out.println("Date: " + training.getTrainingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System.out.println("Duration: " + training.getTrainingDuration() + " minutes");
            System.out.println("-----");
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void getTraineesByTrainerUsername() {
        System.out.println("\n----- Get Trainees by Trainer Username -----");

        String username = getStringInput("Enter trainer username: ");

        try {
            // Get trainees for the trainer
            PagedResponse<Trainee> trainees = trainerService.getTraineesByTrainerUsername(username, 0, 100);

            // Display trainees
            System.out.println("\nTrainees assigned to trainer '" + username + "':");
            if (trainees.content().isEmpty()) {
                System.out.println("No trainees found for this trainer.");
                return;
            }

            for (Trainee trainee : trainees.content()) {
                System.out.println("Username: " + trainee.getUsername());
                System.out.println("Name: " + trainee.getFirstName() + " " + trainee.getLastName());
                System.out.println("Date of Birth: " + (trainee.getDateOfBirth() != null ? trainee.getDateOfBirth().format(dateFormatter) : "Not set"));
                System.out.println("Address: " + (trainee.getAddress() != null ? trainee.getAddress() : "Not set"));
                System.out.println("Active: " + (trainee.isActive() ? "Yes" : "No"));
                System.out.println("-----");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private LocalDate parseDateInput(String prompt, DateTimeFormatter dateFormatter) {
        String dateStr = getStringInput(prompt);
        if (!dateStr.isEmpty()) {
            try {
                return LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Using no date filter.");
            }
        }
        return null;
    }
}
