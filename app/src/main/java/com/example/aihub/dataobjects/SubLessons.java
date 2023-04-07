package com.example.aihub.dataobjects;

import java.io.Serializable;

public class SubLessons implements Serializable {

    public String index;
    public String name;
    public String cards;

    public SubLessons(String index, String name, String cards) {
        this.index = index;
        this.name = name;
        this.cards = cards;
    }
    public SubLessons(){}

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }
}
