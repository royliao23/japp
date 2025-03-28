package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DisplayPersonsfile {

    private static final String JSON_FILE = "person.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nChoose an operation: 1-Create, 2-Read, 3-Update, 4-Delete, 5-Exit");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        createPerson(scanner);
                        break;
                    case 2:
                        readPersons();
                        break;
                    case 3:
                        updatePerson(scanner);
                        break;
                    case 4:
                        deletePerson(scanner);
                        break;
                    case 5:
                        System.out.println("Exiting program...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (1-5).");
            }
        }
    }

    private static void createPerson(Scanner scanner) {
        try {
            List<Person> persons = readPersonsFromFile();

            System.out.println("Enter name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return;
            }

            System.out.println("Enter age: ");
            int age;
            try {
                age = Integer.parseInt(scanner.nextLine());
                if (age <= 0) {
                    System.out.println("Age must be positive.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number for age.");
                return;
            }

            System.out.println("Enter city: ");
            String city = scanner.nextLine().trim();
            if (city.isEmpty()) {
                System.out.println("City cannot be empty.");
                return;
            }

            persons.add(new Person(name, age, city));
            writePersonsToFile(persons);
            System.out.println("Person added successfully.");

        } catch (IOException e) {
            System.err.println("Error accessing the data file: " + e.getMessage());
        }
    }

    private static void readPersons() {
        try {
            List<Person> persons = readPersonsFromFile();
            if (persons.isEmpty()) {
                System.out.println("No persons found.");
            } else {
                System.out.println("\nCurrent Persons:");
                for (Person person : persons) {
                    System.out.println(person);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading data: " + e.getMessage());
        }
    }

    private static void updatePerson(Scanner scanner) {
        try {
            List<Person> persons = readPersonsFromFile();
            if (persons.isEmpty()) {
                System.out.println("No persons available to update.");
                return;
            }

            System.out.println("Enter name of person to update: ");
            String nameToUpdate = scanner.nextLine().trim();

            boolean found = false;
            for (Person person : persons) {
                if (person.getName().equalsIgnoreCase(nameToUpdate)) {
                    System.out.println("Current details: " + person);
                    
                    System.out.println("Enter new age (press enter to keep current): ");
                    String ageInput = scanner.nextLine().trim();
                    if (!ageInput.isEmpty()) {
                        try {
                            int newAge = Integer.parseInt(ageInput);
                            if (newAge > 0) {
                                person.setAge(newAge);
                            } else {
                                System.out.println("Age must be positive. Keeping current age.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid age format. Keeping current age.");
                        }
                    }

                    System.out.println("Enter new city (press enter to keep current): ");
                    String newCity = scanner.nextLine().trim();
                    if (!newCity.isEmpty()) {
                        person.setCity(newCity);
                    }

                    writePersonsToFile(persons);
                    System.out.println("Person updated successfully.");
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Person not found.");
            }
        } catch (IOException e) {
            System.err.println("Error updating data: " + e.getMessage());
        }
    }

    private static void deletePerson(Scanner scanner) {
        try {
            List<Person> persons = readPersonsFromFile();
            if (persons.isEmpty()) {
                System.out.println("No persons available to delete.");
                return;
            }

            System.out.println("Enter name of person to delete: ");
            String nameToDelete = scanner.nextLine().trim();

            boolean removed = persons.removeIf(person -> 
                person.getName().equalsIgnoreCase(nameToDelete));

            if (removed) {
                writePersonsToFile(persons);
                System.out.println("Person deleted successfully.");
            } else {
                System.out.println("Person not found.");
            }
        } catch (IOException e) {
            System.err.println("Error deleting data: " + e.getMessage());
        }
    }

    private static List<Person> readPersonsFromFile() throws IOException {
        File file = new File(JSON_FILE);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<List<Person>>() {});
    }

    private static void writePersonsToFile(List<Person> persons) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter()
                   .writeValue(new File(JSON_FILE), persons);
    }
}