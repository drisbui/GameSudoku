package com.example.gamesudoku.model;

public class Achievements {

    private int id;
    private String name;
    private String time;
    private String date;

    public Achievements(int id, String name, String time, String date) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.date = date;
    }
    public Achievements(){

    }

    @Override
    public String toString() {
        return  id + "\t "+name + "\t " + time+ "\t "+ date;
    }

    public Achievements(String name, String time, String date) {
        this.name = name;
        this.time = time;
        this.date = date;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
