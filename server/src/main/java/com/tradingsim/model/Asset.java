package com.tradingsim.model;

import com.tradingsim.repository.base.Identifiable;

public class Asset implements Identifiable {

    private int id;
    private String name;

    private byte[] picture;

    public Asset() {}

    public Asset(int id, String name, byte[] picture) {
        this.id = id;
        this.name = name;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
