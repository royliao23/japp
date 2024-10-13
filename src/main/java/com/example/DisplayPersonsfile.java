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
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose an operation: 1-Create, 2-Read, 3-Update, 4-Delete, 5-Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            
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
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Create a new Person
    private static void createPerson(Scanner scanner) {
        try {
            List<Person> persons = readPersonsFromFile();

            System.out.println("Enter name: ");
            String name = scanner.nextLine();
            System.out.println("Enter age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.println("Enter city: ");
            String city = scanner.nextLine();

            persons.add(new Person(name, age, city));
            writePersonsToFile(persons);

            System.out.println("Person added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read and display all Persons
    private static void readPersons() {
        try {
            List<Person> persons = readPersonsFromFile();
            if (persons.isEmpty()) {
                System.out.println("No persons found.");
            } else {
                for (Person person : persons) {
                    System.out.println(person);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update an existing Person
    private static void updatePerson(Scanner scanner) {
        try {
            List<Person> persons = readPersonsFromFile();

            System.out.println("Enter name of person to update: ");
            String nameToUpdate = scanner.nextLine();

            for (Person person : persons) {
                if (person.getName().equalsIgnoreCase(nameToUpdate)) {
                    System.out.println("Enter new age: ");
                    int newAge = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    System.out.println("Enter new city: ");
                    String newCity = scanner.nextLine();

                    person.setAge(newAge);
                    person.setCity(newCity);
                    writePersonsToFile(persons);

                    System.out.println("Person updated successfully.");
                    return;
                }
            }
            System.out.println("Person not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Delete a Person
    private static void deletePerson(Scanner scanner) {
        try {
            List<Person> persons = readPersonsFromFile();

            System.out.println("Enter name of person to delete: ");
            String nameToDelete = scanner.nextLine();

            Person personToDelete = null;
            for (Person person : persons) {
                if (person.getName().equalsIgnoreCase(nameToDelete)) {
                    personToDelete = person;
                    break;
                }
            }

            if (personToDelete != null) {
                persons.remove(personToDelete);
                writePersonsToFile(persons);
                System.out.println("Person deleted successfully.");
            } else {
                System.out.println("Person not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to read persons from JSON file
    private static List<Person> readPersonsFromFile() throws IOException {
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<List<Person>>() {});
    }

    // Helper method to write persons to JSON file
    private static void writePersonsToFile(List<Person> persons) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE), persons);
    }
}
