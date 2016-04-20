package asd.org.ahorcado.dal.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asd.org.ahorcado.dal.DataBaseHelper;
import asd.org.ahorcado.models.Word;

/**
 * Created by Huber on 17/04/2016.
 */
public class WordDAO {

    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private int WORD_LIMIT = 30;
    private String TABLE_WORD_NAME = "words";

    public WordDAO(Context context) {
        try {
            dbHelper = new DataBaseHelper(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Map<Word, Integer> GetWords() {
        Map<Word, Integer> result = new HashMap<>();
        String query = "SELECT * FROM " + TABLE_WORD_NAME + " ORDER BY RANDOM() LIMIT " + WORD_LIMIT;
        Cursor cursor = database.rawQuery(query, null);

        while (!cursor.isAfterLast()) {
            Word word = new Word();
            word.setOriginalWord(cursor.getString(1));
            word.setSize(cursor.getInt(3));
            result.put(word,0);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return result;
    }
}
