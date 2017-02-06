package com.essam.todos.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by essam on 06/02/17.
 */

public class TodoItem extends RealmObject {
    @PrimaryKey
    private int id;

    private String todo;
    private String isFinished;

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
}
