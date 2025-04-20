package com.unicesumar.entities;

public class NotaFiscal {
    private int id;
    private String nomeCliente;
    private String produtos;
    private double valorTotal;
    private String metodoPagamento;

    public NotaFiscal(String nomeCliente, String produtos, double valorTotal, String metodoPagamento) {
        this.nomeCliente = nomeCliente;
        this.produtos = produtos;
        this.valorTotal = valorTotal;
        this.metodoPagamento = metodoPagamento;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getProdutos() {
        return produtos;
    }

    public void setProdutos(String produtos) {
        this.produtos = produtos;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}