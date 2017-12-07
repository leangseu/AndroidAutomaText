package com.leangseu.automatext;

/**
 * Created by tj on 11/9/17.
 */

//Simple class for the list of tasks on the main screen
public class Task {
    public String phoneNumber;
    public String message;
    public String date;
    public String time;

    public Task(String phoneNumber, String message, String date, String time) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.date = date;
        this.time = time;
    }

}
