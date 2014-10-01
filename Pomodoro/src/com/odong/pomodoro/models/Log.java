package com.odong.pomodoro.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 14-10-1.
 */
public class Log implements Serializable {
    private int id;
    private String message;
    private Date created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
