package com.gmail.randzjx.words.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gmail.randzjx.words.database.WordsDbSchema;
import com.gmail.randzjx.words.database.WordsDbSchema.WordsTable.Columns;

public class WordsDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "wordsbase.db";

    public WordsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder s = new StringBuilder();
        s.append("create table ").append(WordsDbSchema.WordsTable.WORDS_TABLE)
                .append('(').append(Columns.WORD_ID).append(" text primary key not null, ")
                .append(Columns.FIRST_DESCRIPTION).append(", ")
                .append(Columns.SECOND_DESCRIPTION).append(", ")
                .append(Columns.TRAIN_NUM).append(" integer not null, ")
                .append(Columns.GUESSED).append(" integer int not null, ")
                .append(Columns.DATE).append(" unsigned big int not null)");
        db.execSQL(s.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
