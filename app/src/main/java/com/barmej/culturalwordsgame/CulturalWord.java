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

    public int getName() {
        return name;
    }
}
