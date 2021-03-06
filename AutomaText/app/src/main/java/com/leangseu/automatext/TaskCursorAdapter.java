package com.leangseu.automatext;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leangseu.automatext.data.AutomaTextContract.AutomaTextEntry;

/**
 * Created by leangseu on 12/1/17.
 */

public class TaskCursorAdapter extends CursorAdapter {

    public TaskCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_task, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView phoneNumberTV = (TextView) view.findViewById(R.id.phone_number_li);
        TextView messageTV = (TextView) view.findViewById(R.id.message_li);
        TextView dateTV = (TextView) view.findViewById(R.id.date_li);
        TextView timeTV = (TextView) view.findViewById(R.id.time_li);
        ImageView syncIndicatior = (ImageView) view.findViewById(R.id.sync_indicator);

        int numberIndex = cursor.getColumnIndex(AutomaTextEntry.COLUMN_NUMBER);
        int messageIndex = cursor.getColumnIndex(AutomaTextEntry.COLUMN_MESSAGE);
        int timeIndex = cursor.getColumnIndex(AutomaTextEntry.COLUMN_TIME);
        int dateIndex = cursor.getColumnIndex(AutomaTextEntry.COLUMN_DATE);
        int flagIndex = cursor.getColumnIndex(AutomaTextEntry.COLUMN_FLAG);

        String number = cursor.getString(numberIndex);
        String message = cursor.getString(messageIndex);
        String time = cursor.getString(timeIndex);
        String date = cursor.getString(dateIndex);
        int flag = cursor.getInt(flagIndex);

        phoneNumberTV.setText(number);
        messageTV.setText(message);
        dateTV.setText(date);
        timeTV.setText(time);

        if (flag != 0) {
            syncIndicatior.setImageResource(R.drawable.success_indicator);
            Toast.makeText(context, "SUCCESS SYNC", Toast.LENGTH_SHORT).show();
        }
        else {
            syncIndicatior.setImageResource(R.drawable.failed_indicator);
            Toast.makeText(context, "FAILED SYNC", Toast.LENGTH_SHORT).show();
        }
    }
}
