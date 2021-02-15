package com.android.example.WordListSearch;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordListOpenHelper extends SQLiteOpenHelper {
    public static final int db_version = 1;
    public static final String wordListTable = "word_entries";
    public static final String nameDB = "wordlist";
    public static final String keyID = "_id";
    public static final String keyWord = "word";
    private static final String createTableList = "CREATE TABLE " + wordListTable + " (" +
            keyID + " INTEGER PRIMARY KEY, " + keyWord +" TEXT );";

    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;

    public WordListOpenHelper(Context context) {
        super(context, nameDB, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableList);
        fillDatabaseWithData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + wordListTable);
        onCreate(db);
    }

    public void fillDatabaseWithData(SQLiteDatabase db) {
        String[] words = {"Android", "Adapter", "ListView", "AsyncTask", "Android Studio", "SQLiteDatabase", "SQLOpenHelper", "Data model", "ViewHolder", "Android Performance", "OnClickListener"};
        ContentValues values = new ContentValues();
        for (int i = 0; i < words.length; i++) {
            values.put(keyWord, words[i]);
            db.insert(wordListTable, null, values);
        }
    }

    @SuppressLint("Recycle")
    public WordItem query(int position) {
        String query = "SELECT * FROM " + wordListTable + " ORDER BY " + keyWord + " ASC " + "LIMIT " + position + ",1";
        Cursor cursor = null;
        WordItem entry = new WordItem();
        try {
            if (readDB == null)
                readDB = getReadableDatabase();
            cursor = readDB.rawQuery(query, null);
            cursor.moveToFirst();
            entry.setId(cursor.getInt(cursor.getColumnIndex(keyID)));
            entry.setWord(cursor.getString(cursor.getColumnIndex(keyWord)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            return entry;
        }
    }

    public long count() {
        if (readDB == null)
            readDB = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(readDB, wordListTable);
    }

    public long insert(String word) {
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(keyWord, word);
        try {
            if (writeDB == null)
                writeDB = getWritableDatabase();
            newId = writeDB.insert(wordListTable, null, values);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return newId;
    }

    public int delete(int id) {
        int deleted = 0;
        try {
            if (writeDB == null)
                writeDB = getWritableDatabase();
            deleted = writeDB.delete(wordListTable, keyID + " = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
           e.printStackTrace();
        }
        return deleted;
    }

    public int update(int id, String word) {
        int mNumberOfRowsUpdated = -1;
        ContentValues values = new ContentValues();
        values.put(keyWord, word);
        try {
            if (writeDB == null)
                writeDB = getWritableDatabase();
            mNumberOfRowsUpdated = writeDB.update(wordListTable, values, keyID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mNumberOfRowsUpdated;
    }

    public Cursor search(String word) {
        String[] columns = new String[]{keyWord};
        String searchString = "%" + word + "%";
        String where = keyWord + " LIKE ?";
        String[] whereArgs = new String[]{searchString};
        Cursor cursor = null;
        try {
            if (readDB == null)
                readDB = getReadableDatabase();
            cursor = readDB.query(wordListTable, columns, where, whereArgs, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }
}
