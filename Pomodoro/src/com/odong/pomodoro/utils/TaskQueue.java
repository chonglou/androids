package com.odong.pomodoro.utils;

import com.odong.pomodoro.models.Task;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by flamen on 14-10-1.
 */
public class TaskQueue {
    private static synchronized void set(int size, int timer, int shortBreak, int longerBreak) {
        instance.size = size;
        instance.timer = timer;
        instance.shortBreak = shortBreak;
        instance.longerBreak = longerBreak;
    }

    private static synchronized TaskQueue getInstance() {
        return instance;
    }

    private final static TaskQueue instance = new TaskQueue();

    private TaskQueue() {
        tasks = new LinkedList<Task>();
    }


    public synchronized void add(Task task) {
        tasks.offer(task);
    }

    public synchronized void next() {
        tasks.poll();
    }


    private final Queue<Task> tasks;
    private int shortBreak;
    private int longerBreak;
    private int timer;
    private int size;


}
