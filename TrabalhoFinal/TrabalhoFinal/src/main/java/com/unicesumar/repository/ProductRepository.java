package com.unicesumar.repository;

import com.unicesumar.entities.Product;

import java.sql.*;
import java.util.*;

public class ProductRepository implements EntityRepository<Product> {
    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    public void resetarTabelaProdutos() {
        try {
            Statement stmt = connection.createStatement();

            // Drop
            stmt.executeUpdate("DROP TABLE IF EXISTS products");
            System.out.println("Tabela antiga deletada.");

            // Create
            String createSQL = "CREATE TABLE products (" +
                    "id INTEGER PRIMARY KEY," +
                    "uuid TEXT NOT NULL," +
                    "name TEXT," +
                    "price REAL" +
                    ")";
            stmt.executeUpdate(createSQL);

            System.out.println("Nova tabela criada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    @Override
    public void save(Product entity) {
        String query = "INSERT INTO products VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setInt(1, entity.getId());
            stmt.setString(2, entity.getUuid().toString());
            stmt.setString(3, entity.getName());
            stmt.setDouble(4, entity.getPrice());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Double findPriceById(int id) {
        String query = "SELECT price FROM products WHERE id = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, String.valueOf(id));
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                double price =
                        resultSet.getDouble("price");
                return price;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0.0;
    }
    public Optional<Product> findById2(int id) {
        String query = "SELECT * FROM products WHERE id = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, String.valueOf(id));
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Product product = new Product(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("id")
                );
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    @Override
    public Optional<Product> findById(UUID id) {
        String query = "SELECT * FROM products WHERE uuid = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Product product = new Product(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("id")
                );
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }

    @Override
    public List<Product> findAll() {
        String query = "SELECT * FROM products";
        List<Product> products = new LinkedList<>();
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Product product = new Product(
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("id")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;

    }


    @Override
    public void deleteById(UUID id) {
        String query = "DELETE FROM products WHERE uuid = ?";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}