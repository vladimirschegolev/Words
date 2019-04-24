package com.gmail.randzjx.words.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.gmail.randzjx.words.database.Word;
import com.gmail.randzjx.words.database.WordsDbSchema.WordsTable.Columns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Adapter {

    public static Word getWord(Cursor cursor) {
        cursor.moveToFirst();
        return createWord(cursor);
    }

    private static Word createWord(Cursor cursor) {
        String sWord = cursor.getString(cursor.getColumnIndex(Columns.WORD_ID));
        String first_description = cursor.getString(cursor.getColumnIndex(Columns.FIRST_DESCRIPTION));
        String second_description = cursor.getString(cursor.getColumnIndex(Columns.SECOND_DESCRIPTION));
        long date = cursor.getLong(cursor.getColumnIndex(Columns.DATE));
        int numTries = cursor.getInt(cursor.getColumnIndex(Columns.TRAIN_NUM));
        int numGuessed = cursor.getInt(cursor.getColumnIndex(Columns.GUESSED));

        Word word = new Word(sWord, new Date(date));
        word.setFirstDescription(first_description);
        word.setSecondDescription(second_description);
        word.setNumTries(numTries);
        word.setNumGuessed(numGuessed);

        return word;
    }

    public static Word[] getWords(Cursor cursor) {
        if (cursor.getCount() == 0) return null;
        cursor.moveToFirst();
        Word[] words = new Word[cursor.getCount()];
        for (int i = 0; i < words.length; i++, cursor.moveToNext()) {
            words[i] = createWord(cursor);
        }
        Log.d("getWords", Arrays.toString(words));
        return words;
    }

    public static ContentValues getContentValues(Word word) {
        ContentValues values = new ContentValues();

        values.put(Columns.WORD_ID, word.getWord());
        values.put(Columns.FIRST_DESCRIPTION, word.getFirstDescription());
        values.put(Columns.SECOND_DESCRIPTION, word.getSecondDescription());
        values.put(Columns.DATE, word.getDate().getTime());
        values.put(Columns.TRAIN_NUM, word.getNumTries());
        values.put(Columns.GUESSED, word.getNumGuessed());

        return values;
    }

    public static String[] toStringArray(Cursor data) {
        String[] out = new String[data.getCount()];
        for (int i = 0; i < out.length; i++) {
            data.moveToPosition(i);
            out[i] = data.getString(0);
        }
        return out;
    }

    public static List<String> toList(Cursor data) {
        List<String> out = new ArrayList<>(data.getCount());
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            out.add(data.getString(0));
        }
        return out;
    }
}
