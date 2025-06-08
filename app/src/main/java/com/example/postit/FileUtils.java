package com.example.postit;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


public class FileUtils {
    public static String getFilePath(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        String s = cursor.getString(column_index);

        cursor.close();
        return s;
    }
}