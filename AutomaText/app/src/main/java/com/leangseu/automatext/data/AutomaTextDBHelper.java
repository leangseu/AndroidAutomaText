package com.leangseu.automatext.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leangseu.automatext.data.AutomaTextContract.AutomaTextEntry;

/**
 * Created by leangseu on 11/30/17.
 */

public class AutomaTextDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "automatext.db";
    private static final int DATABASE_VERSION = 3;




    public AutomaTextDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS automatext;");
        String SQL_CREATE_TABLE =  "CREATE TABLE " + AutomaTextEntry.TABLE_NAME + " ("
                + AutomaTextEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AutomaTextEntry.COLUMN_NUMBER + " INTEGER NOT NULL, "
                + AutomaTextEntry.COLUMN_MESSAGE + " TEXT NOT NULL, "
                + AutomaTextEntry.COLUMN_TIME + " TEXT NOT NULL, "
                + AutomaTextEntry.COLUMN_DATE + " TEXT NOT NULL, "
                + AutomaTextEntry.COLUMN_FLAG + " INTEGER DEFAULT 0,"
                + AutomaTextEntry.ONLINE_ID + " INTEGER DEFAULT 0);";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS automatext;");
        onCreate(sqLiteDatabase);
    }
}
