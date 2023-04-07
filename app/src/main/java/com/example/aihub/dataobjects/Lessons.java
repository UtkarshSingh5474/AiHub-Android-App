package com.example.aihub.dataobjects;

import java.util.List;

public class Lessons {

    public List<SubLessons> subLessons;
    public String index;
    public String name;


    public Lessons(List<SubLessons> subLessons, String index, String name) {
        this.subLessons = subLessons;
        this.index = index;
        this.name = name;
    }
    public Lessons(String index, String name) {
        this.subLessons = subLessons;
        this.index = index;
        this.name = name;
    }
    public Lessons(){}

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


}
