package com.example.anton.biotop;

public class Animal {
    String id, health;

    public Animal(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public String getHealth() {
        if (id.equals("001"))
            return "bra";
        else if (id.equals("002"))
            return "ok";
        else
            return "ush";
    }
}