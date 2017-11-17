package com.leangseu.automatext;


import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    ArrayList<Task> taskList;
    TaskAdapter taskListAdapter;

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
        taskList = new ArrayList<Task>();
        Task newTask = new Task("9999999999", "hi", "12/12/18", "10:00pm");
        taskListAdapter = new TaskAdapter(this, taskList);
        ListView taskListView = (ListView) findViewById(R.id.tasks_list);
        taskListView.setAdapter(taskListAdapter);
        taskListAdapter.addAll(newTask);
        taskListAdapter.add(new Task("9782341234", "long string for testing. duh duh duh", "12/12/18", "10:00pm"));

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("On item click :", "position " + i  +" ID " + l);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.d("return", "onActivityResult: " + data.getStringExtra("phone_number"));
            Task task = new Task(data.getStringExtra("phone_number"), data.getStringExtra("message"), data.getStringExtra("date"), data.getStringExtra("time"));
            taskListAdapter.add(task);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
