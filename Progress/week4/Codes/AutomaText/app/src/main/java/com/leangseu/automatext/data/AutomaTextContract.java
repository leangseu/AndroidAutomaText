package com.leangseu.automatext.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by leangseu on 11/30/17.
 */

public class AutomaTextContract {
    private AutomaTextContract() {}

    public static final String CONTENT_AUTHORITY = "com.leangseu.automatext";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_AUTOMATEXT = "automatext";

    public static final class AutomaTextEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_AUTOMATEXT);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AUTOMATEXT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_AUTOMATEXT;

        public final static String TABLE_NAME = "automatext";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_NUMBER = "number";
        public final static String COLUMN_MESSAGE = "message";
        public final static String COLUMN_TIME = "time";
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_FLAG = "flag";

        // TODO : validation if needed
    }

}
