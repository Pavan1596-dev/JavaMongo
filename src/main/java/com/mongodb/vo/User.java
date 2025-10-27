package com.mongodb.vo;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private boolean active;

    public User(int id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

}
