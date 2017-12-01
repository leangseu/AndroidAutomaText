package com.leangseu.automatext;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.leangseu.automatext.data.AutomaTextContract.AutomaTextEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

//    ArrayList<Task> taskList;
//    TaskAdapter taskListAdapter;
    TaskCursorAdapter taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditorActivity.class);
                startActivityForResult(i, 1);
            }
        });

        //attach the Adapter for the list of tasks
//        taskList = new ArrayList<Task>();
//        Task newTask = new Task("9999999999", "hi", "12/12/18", "10:00pm");
//        taskListAdapter = new TaskAdapter(this, taskList);
//        final ListView taskListView = (ListView) findViewById(R.id.tasks_list);
//        taskListView.setAdapter(taskListAdapter);
//        taskListAdapter.addAll(newTask);
//        taskListAdapter.add(new Task("9782341234", "long string for testing. duh duh duh", "12/12/18", "10:00pm"));

        ListView taskListView = (ListView) findViewById(R.id.tasks_list);

        taskListAdapter = new TaskCursorAdapter(this, null);
        taskListView.setAdapter(taskListAdapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                Uri currentUri = ContentUris.withAppendedId(AutomaTextEntry.CONTENT_URI, l);

                intent.setData(currentUri);

                startActivity(intent);
            }
        });

//        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                // Delete from list
//                Task item = taskListAdapter.getItem(i);
//                taskListAdapter.remove(item);
//                Toast.makeText(getApplicationContext(),"Deleted message to " + item.phoneNumber, Toast.LENGTH_SHORT).show();
//                //TODO delete from database
//                return true;
//            }
//        });

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.d("return", "onActivityResult: " + data.getStringExtra("phone_number"));
            Task task = new Task(data.getStringExtra("phone_number"), data.getStringExtra("message"), data.getStringExtra("date"), data.getStringExtra("time"));
//            taskListAdapter.add(task);

            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new FormBody.Builder()
                    .add("phoneNumber", task.phoneNumber)
                    .add("message", task.message)
                    .add("time", task.time)
                    .add("date", task.date)
                    .build();

            Request request = new Request.Builder()
                    .url("https://text-me-later.herokuapp.com/saveText")
                    .post(requestBody)
                    .build();


            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Error", e.toString());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("http:", "onResponse: " + response.body().string());
                }

            });
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AutomaTextEntry._ID,
                AutomaTextEntry.COLUMN_NUMBER,
                AutomaTextEntry.COLUMN_MESSAGE,
                AutomaTextEntry.COLUMN_TIME,
                AutomaTextEntry.COLUMN_DATE,
                AutomaTextEntry.COLUMN_FLAG};
        return new CursorLoader(this,
                AutomaTextEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        taskListAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        taskListAdapter.swapCursor(null);
    }
}
