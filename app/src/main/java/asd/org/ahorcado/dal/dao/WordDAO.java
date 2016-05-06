/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.dal.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asd.org.ahorcado.dal.DataBaseHelper;
import asd.org.ahorcado.models.Word;

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

    public Map<Word, Integer> getWords(List<Long> wordIdList) {
        Map<Word, Integer> result = new HashMap<>();
        try {
            this.open();
            String notInIds = wordIdList.size() > 0 ? " AND _ID NOT IN (" + convertList(wordIdList) + ")" : "";
            String query = "SELECT * FROM " + TABLE_WORD_NAME + " WHERE SIZE < 11" + notInIds + " ORDER BY RANDOM() LIMIT " + WORD_LIMIT;
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Word word = new Word(cursor.getString(1));
                word.setId(cursor.getLong(0));
                word.setSize(cursor.getInt(3));
                result.put(word, 0);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String convertList(List<Long> list) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Long parameter : list) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append(parameter);
        }
        return sb.toString();
    }
}