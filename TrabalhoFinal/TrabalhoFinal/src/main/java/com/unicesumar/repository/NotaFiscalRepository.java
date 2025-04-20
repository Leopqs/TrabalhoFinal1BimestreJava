package com.unicesumar.repository;

import com.unicesumar.entities.NotaFiscal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class NotaFiscalRepository {
    private final Connection connection;

    public NotaFiscalRepository(Connection connection) {
        this.connection = connection;
    }

    public void resetarTabelaNotasFiscais() {
        try {
            Statement stmt = connection.createStatement();

            // Drop
            stmt.executeUpdate("DROP TABLE IF EXISTS notas_fiscais");
            System.out.println("Tabela antiga de notas fiscais deletada.");

            // Create
            String createSQL = "CREATE TABLE notas_fiscais (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome_cliente TEXT NOT NULL," +
                    "produtos TEXT NOT NULL," +
                    "valor_total REAL NOT NULL," +
                    "metodo_pagamento TEXT NOT NULL" +
                    ")";
            stmt.executeUpdate(createSQL);

            System.out.println("Nova tabela de notas fiscais criada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void save(NotaFiscal notaFiscal) {
        String query = "INSERT INTO notas_fiscais (nome_cliente, produtos, valor_total, metodo_pagamento) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, notaFiscal.getNomeCliente());
            stmt.setString(2, notaFiscal.getProdutos());
            stmt.setDouble(3, notaFiscal.getValorTotal());
            stmt.setString(4, notaFiscal.getMetodoPagamento());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}