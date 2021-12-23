package com.infilos.mantou.views.material.model;

public class SimplePerson {
    private final String name;

    public SimplePerson(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

