package com.example.airport.model;

public class Tariff {
    public int id;
    public String direction;
    public double price;
    public String cargoType;
    public int maxWeight;

    public Tariff(int id, String direction, double price,
                  String cargoType, int maxWeight) {
        this.id = id;
        this.direction = direction;
        this.price = price;
        this.cargoType = cargoType;
        this.maxWeight = maxWeight;
    }
}
