package com.example.anton.biotop;

public class Animal {
    String id, health;

    public Animal(String id, String health) {
        this.id = id;
        this.health = health;
    }

    public String getID() {
        return id;
    }

    public String getHealth() {
        return health;
    }
}
