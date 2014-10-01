package com.odong.pomodoro.models;

import java.util.Date;

/**
 * Created by flamen on 14-10-1.
 */
public class Task {
    public enum Flag {
        COMMIT, PROGRESSING, DONE
    }

    private int id;
    private String title;
    private String content;
    private Flag flag;
    private Date todo;
    private Date created;

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTodo() {
        return todo;
    }

    public void setTodo(Date todo) {
        this.todo = todo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
