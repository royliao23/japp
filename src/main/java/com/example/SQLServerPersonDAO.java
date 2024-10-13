package com.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLServerPersonDAO {
    private static final String JDBC_URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=MyDatabase1;encrypt=true;trustServerCertificate=true;";
    private static final String JDBC_USER = "newuser";  // Replace with your SQL Server user (e.g., sa)
    private static final String JDBC_PASSWORD = "your_password";  // Replace with your SQL Server password

    // Create a new Person in the database
    public void createPerson(Person person) {
        String query = "INSERT INTO Persons (name, age, city) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, person.getName());
            statement.setInt(2, person.getAge());
            statement.setString(3, person.getCity());
            statement.executeUpdate();
            System.out.println("Person added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read all Persons from the database
    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<>();
        String query = "SELECT * FROM Persons";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Person person = new Person();
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setCity(resultSet.getString("city"));
                persons.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }

    // Update a Person in the database
    public void updatePerson(String oldName, Person newPerson) {
        String query = "UPDATE Persons SET name = ?, age = ?, city = ? WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newPerson.getName());
            statement.setInt(2, newPerson.getAge());
            statement.setString(3, newPerson.getCity());
            statement.setString(4, oldName);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Person updated successfully.");
            } else {
                System.out.println("Person not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a Person from the database
    public void deletePerson(String name) {
        String query = "DELETE FROM Persons WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Person deleted successfully.");
            } else {
                System.out.println("Person not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

