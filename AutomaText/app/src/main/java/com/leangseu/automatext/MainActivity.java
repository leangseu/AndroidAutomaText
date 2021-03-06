package com.leangseu.automatext;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
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
import android.widget.TextView;
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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

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
////                Task item = taskListAdapter.getItem(i);
////                taskListAdapter.remove(item);
//                //TODO delete from database
//
//                Toast.makeText(getApplicationContext(),"Deleted message", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete_all:
                getContentResolver().delete(AutomaTextEntry.CONTENT_URI, null, null);
                return true;
//            case R.id.action_edit:
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
