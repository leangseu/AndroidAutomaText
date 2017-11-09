package com.leangseu.automatext;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by tj on 11/9/17.
 */
// task adaptor for the taskList on the main screen of the app
public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    public View getView(int position, View view, ViewGroup parentView) {
        Task task = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parentView, false);
        }

        TextView phoneNumberTV = (TextView) view.findViewById(R.id.phone_number_li);
        TextView messageTV = (TextView) view.findViewById(R.id.message_li);
        TextView dateTV = (TextView) view.findViewById(R.id.date_li);
        TextView timeTV = (TextView) view.findViewById(R.id.time_li);

        phoneNumberTV.setText(task.phoneNumber);
        messageTV.setText(task.message);
        dateTV.setText(task.date);
        timeTV.setText(task.time);

        return view;
    }
}



