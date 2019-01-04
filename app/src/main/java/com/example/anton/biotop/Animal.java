package com.example.anton.biotop;

public class Animal {
    String id, type;

    public Animal(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getID() {
        return id;
    }

    public String getType() {
        return type;
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