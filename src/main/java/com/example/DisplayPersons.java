package com.example;
import java.util.List;
import java.util.Scanner;

public class DisplayPersons {

    private static SQLServerPersonDAO personDAO = new SQLServerPersonDAO();

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
            System.out.println("Starting create operation...");
    
            System.out.println("Enter name: ");
            String name = scanner.nextLine();
            System.out.println("Enter age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.println("Enter city: ");
            String city = scanner.nextLine();
    
            System.out.println("Creating Person: " + name + ", Age: " + age + ", City: " + city);
    
            Person person = new Person(name, age, city);
            
            // Adding this to debug if it reaches this point
            System.out.println("Attempting to create person in database...");
            personDAO.createPerson(person);
    
            System.out.println("Person added successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred while creating person.");
            e.printStackTrace();
        }
    }

    // Read and display all Persons
    private static void readPersons() {
        List<Person> persons = personDAO.getAllPersons();
        if (persons.isEmpty()) {
            System.out.println("No persons found.");
        } else {
            for (Person person : persons) {
                System.out.println(person);
            }
        }
    }

    // Update an existing Person
    private static void updatePerson(Scanner scanner) {
        System.out.println("Enter name of person to update: ");
        String oldName = scanner.nextLine();

        System.out.println("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.println("Enter new age: ");
        int newAge = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.println("Enter new city: ");
        String newCity = scanner.nextLine();

        Person newPerson = new Person(newName, newAge, newCity);
        personDAO.updatePerson(oldName, newPerson);
    }

    // Delete a Person
    private static void deletePerson(Scanner scanner) {
        System.out.println("Enter name of person to delete: ");
        String name = scanner.nextLine();

        personDAO.deletePerson(name);
    }
}
