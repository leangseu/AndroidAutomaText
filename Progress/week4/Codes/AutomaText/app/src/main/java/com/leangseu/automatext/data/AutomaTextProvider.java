package com.leangseu.automatext.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.leangseu.automatext.data.AutomaTextContract.AutomaTextEntry;

/**
 * Created by leangseu on 11/30/17.
 */

public class AutomaTextProvider extends ContentProvider {
    private static final int AUTOMATEXT = 1000;
    private static final int AUTOMATEXT_ID = 1001;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AutomaTextContract.CONTENT_AUTHORITY, AutomaTextContract.PATH_AUTOMATEXT, AUTOMATEXT);
        sUriMatcher.addURI(AutomaTextContract.CONTENT_AUTHORITY, AutomaTextContract.PATH_AUTOMATEXT + "/#", AUTOMATEXT_ID);
    }

    private AutomaTextDBHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = new AutomaTextDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor;
        Log.d("AutomaText", uri.toString());
        int match = sUriMatcher.match(uri);
        switch (match) {
            case AUTOMATEXT:
                cursor = database.query(AutomaTextEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case AUTOMATEXT_ID:
                selection = AutomaTextContract.AutomaTextEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(AutomaTextEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case AUTOMATEXT:
                return AutomaTextEntry.CONTENT_LIST_TYPE;
            case AUTOMATEXT_ID:
                return AutomaTextEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case AUTOMATEXT:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    public Uri insertItem(Uri uri, ContentValues values) {
        String name = values.getAsString(AutomaTextEntry.COLUMN_NUMBER);
        if (name == null) {
            throw new IllegalArgumentException("Item requires a phone number");
        }

        String message = values.getAsString(AutomaTextEntry.COLUMN_MESSAGE);
        String time = values.getAsString(AutomaTextEntry.COLUMN_TIME);
        String date = values.getAsString(AutomaTextEntry.COLUMN_DATE);
        String flag = values.getAsString(AutomaTextEntry.COLUMN_FLAG);


        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long id = database.insert(AutomaTextEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.d("Inventory provider", "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
