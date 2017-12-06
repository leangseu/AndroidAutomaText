package com.leangseu.automatext;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.leangseu.automatext.data.AutomaTextContract.AutomaTextEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    EditText numberET;
    EditText messageET;
    Button timeBtn;
    Button dateBtn;
    Button submitBtn;
    Button deleteBtn;

    private static final int EXISTING_LOADER = 0;
    private Uri currentUri;

    public void setTime(int hour, int minute) {
        updateTime(hour, minute);
    }

    public void setDate(int year, int month, int day) {
        updateDate(year, month, day);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        numberET = (EditText) findViewById(R.id.phone_number_ET);
        messageET = (EditText) findViewById(R.id.message_ET);
        timeBtn = (Button) findViewById(R.id.time_TV);
        dateBtn = (Button) findViewById(R.id.date_TV);
        submitBtn = (Button) findViewById(R.id.submit_TV);
        deleteBtn = (Button) findViewById(R.id.delete_tv);

        currentUri = getIntent().getData();

        if (currentUri == null) {
            setTitle("New Text");
            Calendar c = Calendar.getInstance();

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            updateTime(hour, minute);
            updateDate(year, month, day);
        } else {
            setTitle("Edit Text");

            getLoaderManager().initLoader(0, null, this);
        }




        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                Bundle times = new Bundle();
                times.putInt("hour", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                times.putInt("minute", Calendar.getInstance().get(Calendar.MINUTE));
                newFragment.setArguments(times);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                EditText phoneNumberET = findViewById(R.id.phone_number_ET);
                EditText messageET = findViewById(R.id.message_ET);
                TextView timeTV = findViewById(R.id.time_TV);
                TextView dateTV = findViewById(R.id.date_TV);

                resultIntent.putExtra("phone_number", phoneNumberET.getText().toString());
                resultIntent.putExtra("message", messageET.getText().toString());
                resultIntent.putExtra("time", timeTV.getText().toString());
                resultIntent.putExtra("date", dateTV.getText().toString());
                // TODO Add extras or a data URI to this intent as appropriate.
                ContentValues values = new ContentValues();
                values.put(AutomaTextEntry.COLUMN_NUMBER, phoneNumberET.getText().toString());
                values.put(AutomaTextEntry.COLUMN_MESSAGE, messageET.getText().toString());
                values.put(AutomaTextEntry.COLUMN_TIME, timeTV.getText().toString());
                values.put(AutomaTextEntry.COLUMN_DATE, dateTV.getText().toString());

                updateDatabase(values);

//                resultIntent.putExtra("some_key", "String data");
//                setResult(1, resultIntent);
                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentResolver().delete(currentUri, null, null);
                finish();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AutomaTextEntry._ID,
                AutomaTextEntry.COLUMN_NUMBER,
                AutomaTextEntry.COLUMN_MESSAGE,
                AutomaTextEntry.COLUMN_TIME,
                AutomaTextEntry.COLUMN_DATE,
                AutomaTextEntry.COLUMN_FLAG,
                AutomaTextEntry.ONLINE_ID};
        return new CursorLoader(this,
                currentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        deleteBtn.setVisibility(View.VISIBLE);

        if (cursor.moveToFirst()) {
            int numberIndex = cursor.getColumnIndex(AutomaTextEntry.COLUMN_NUMBER);
            int messageIndex = cursor.getColumnIndex(AutomaTextEntry.COLUMN_MESSAGE);
            int timeIndex = cursor.getColumnIndex(AutomaTextEntry.COLUMN_TIME);
            int dateIndex = cursor.getColumnIndex(AutomaTextEntry.COLUMN_DATE);
            int flagIndex = cursor.getColumnIndex(AutomaTextEntry.COLUMN_FLAG);

            numberET.setText(cursor.getString(numberIndex));
            messageET.setText(cursor.getString(messageIndex));
            timeBtn.setText(cursor.getString(timeIndex));
            dateBtn.setText(cursor.getString(dateIndex));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        numberET.setText("");
        messageET.setText("");

        Calendar c = Calendar.getInstance();

        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        updateTime(hour, minute);
        updateDate(year, month, day);
    }

    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment

        implements TimePickerDialog.OnTimeSetListener {

            public interface OnDataPass {
                public void onDataPass(String data);

            }

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the current time as the default values for the picker

                int hour = getArguments().getInt("hour");
                int minute = getArguments().getInt("minute");

                // Create a new instance of TimePickerDialog and return it
                return new TimePickerDialog(getActivity(), this, hour, minute,
                        DateFormat.is24HourFormat(getActivity()));
            }

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Do something with the time chosen by the user
                ((EditorActivity) getActivity()).setTime(hourOfDay,minute);

            }
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            //updateDate(year, month, day);
            ((EditorActivity) getActivity()).setDate(year, month, day);
        }
    }

    public void updateTime(int hour, int minute) {
        String time = "";
        boolean pm = false;

        if (hour > 12) {
            hour -= 12;
            pm = true;
        } else if (hour == 0) {
            hour = 12;
            pm = false;
        }

        if (hour < 10) {
            time += "0" + hour;
        } else {
            time += hour;
        }

        time += ":";

        if (minute < 10) {
            time += "0" + minute;
        } else {
            time += minute;
        }

        if (pm) {
            time += "pm";
        } else {
            time += "am";
        }

        timeBtn.setText(time);
    }

    public  void updateDate(int year, int month, int day) {
        String date = "";
        month = month + 1;
        if (month < 10) {
            date += "0" + month;
        } else {
            date += month;
        }

        date += "/";

        if (day < 10) {
            date += "0" + day;
        } else {
            date += day;
        }

        date += "/" + year;
        dateBtn.setText(date);
    }

    public void updateDatabase(final ContentValues values) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("phoneNumber", values.getAsString(AutomaTextEntry.COLUMN_NUMBER))
                .add("message", values.getAsString(AutomaTextEntry.COLUMN_MESSAGE))
                .add("time", values.getAsString(AutomaTextEntry.COLUMN_TIME))
                .add("date", values.getAsString(AutomaTextEntry.COLUMN_DATE))
                .build();

        final Request request = new Request.Builder()
                .url("https://text-me-later.herokuapp.com/saveText")
                .post(requestBody)
                .build();

//        updateLocalDatabase(values);

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Error", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d("http:", "onResponse: ID" + res.substring(29, res.length()-1));
                values.put(AutomaTextEntry.COLUMN_FLAG, 1);
                values.put(AutomaTextEntry.ONLINE_ID, Integer.parseInt(res.substring(29, res.length()-1)));
                updateLocalDatabase(values);
                Log.d("success", values.getAsString(AutomaTextEntry.COLUMN_FLAG));
            }
        });
    }

    public void updateLocalDatabase(ContentValues values) {
        if (currentUri == null) {
            Uri newUri = getContentResolver().insert(AutomaTextEntry.CONTENT_URI, values);
            if (newUri == null) {
                Log.d("Insert ", "failed");
            } else {
                Log.d("Insert ", "success");
            }
        } else {
            int rowsAffected = getContentResolver().update(currentUri, values, null, null);

            if (rowsAffected == 0) {
                Log.d("update ", "failed");
            } else {
                Log.d("update ", "success");
            }
        }
    }
}
