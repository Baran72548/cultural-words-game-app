package com.barmej.culturalwordsgame;

public class CulturalWord {
    private int picture;
    private int name;

    public CulturalWord(int picture, int name) {
        this.picture = picture;
        this.name = name;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}
