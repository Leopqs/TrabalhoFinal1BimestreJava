package com.unicesumar.repository;
import com.unicesumar.entities.Product;
import com.unicesumar.entities.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class UserRepository implements EntityRepository<User>{
    private final Connection connection;
    public UserRepository(Connection connection) {
        this.connection = connection;
    }


    public void drop(){
        String query = "DROP TABLE users;";
    }

    public void resetarTabelaUsers() {
        try {
            Statement stmt = connection.createStatement();

            stmt.executeUpdate("DROP TABLE IF EXISTS users");
            System.out.println("Tabela antiga deletada.");

            String createSQL = "CREATE TABLE users (" +
                    "uuid TEXT NOT NULL," +
                    "name TEXT," +
                    "email TEXT," +
                    "password TEXT" +
                    ")";
            stmt.executeUpdate(createSQL);

            System.out.println("Nova tabela criada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void create(){
        String query = "DROP TABLE IF EXISTS users"+
                "CREATE TABLE users (\n" +
                "    uuid CHAR(36) NOT NULL,\n" +
                "    name VARCHAR(255),\n" +
                "    email CHAR(36) NOT NULL\n" +
                "    senha CHAR(36) NOT NULL\n" +
                ");";

    }

    @Override
    public void save(User entity) {
        String query = "INSERT INTO users VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, entity.getUuid().toString());
            stmt.setString(2, entity.getName().toString());
            stmt.setString(3, entity.getEmail().toString());;
            stmt.setString(4, entity.getPassword().toString());;
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional findById(UUID id) {
        String query = "SELECT * FROM users WHERE uuid = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                User user = new User(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                User user = new User(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM users";
        List<User> users = new LinkedList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void deleteById(UUID id) {
        String query = "DELETE FROM users WHERE uuid = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}