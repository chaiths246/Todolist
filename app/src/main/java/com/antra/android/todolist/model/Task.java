package com.antra.android.todolist.model;

public class Task {
    String name;
    private int id;

    public Task() {}

    public Task(String name) {
        this.name = name;
    }

    public Task(int id, String name) {
        this.name = name;
        this.id = id;
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

}
