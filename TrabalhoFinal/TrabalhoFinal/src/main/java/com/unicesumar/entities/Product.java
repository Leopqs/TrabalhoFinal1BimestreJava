package com.unicesumar.entities;

import java.util.UUID;

public class Product extends Entity {
    private final UUID uuid;
    private final String name;
    private final double price;
    private final int Id;

    public Product(UUID uuid, String name, double price, int id) {
        this.uuid = uuid;
        this.name = name;
        this.price = price;
        this.Id = id;
    }

    public int getId() {
        return this.Id;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }



    /// Deixar esse print do ToString mais bonitinho
    @Override
    public String toString() {
        return
                "Id = " + getId() +
                ", Nome = " + getName() +
                ", Preço = R$" + getPrice();
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }
}

