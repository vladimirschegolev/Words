package com.gmail.randzjx.words.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.gmail.randzjx.words.database.WordsDbSchema.WordsTable.Columns;
import com.gmail.randzjx.words.database.helper.WordsDbHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.gmail.randzjx.words.database.WordsDbSchema.WordsTable.WORDS_TABLE;

public class WordsContentProvider extends ContentProvider {


    private static final String AUTHORITY = "com.gmail.randzjx.words.database.WordsContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final int TRAINING = 1;
    private static final int TRAINING_WORD = 2;

    private static String TRAINING_QUERY = String.format(
                    "select * from (select * from %1$s order by %2$s, %3$s limit 1) " +
                    "union " +
                    "select * from (select * from %1$s order by random() limit ?) " +
                    "order by %2$s, %3$s limit ?;", WORDS_TABLE, Columns.TRAIN_NUM, Columns.DATE);

    private static final UriMatcher sURIMatcher;

    static {
        sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sURIMatcher.addURI(AUTHORITY, "training/#", TRAINING);
        sURIMatcher.addURI(AUTHORITY, "training/*", TRAINING_WORD);
    }

    private WordsDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new WordsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (sURIMatcher.match(uri)) {
            case UriMatcher.NO_MATCH:
//                Log.d("Logs", "sort order " + String.valueOf(sortOrder));
                return db.query(WORDS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
            case TRAINING:
                return db.rawQuery(TRAINING_QUERY, new String[]{uri.getLastPathSegment(), uri.getLastPathSegment()});
//                return db.query(WORDS_TABLE, null, null, null, null, null, Columns.TRAIN_NUM + ", " + Columns.DATE, uri.getLastPathSegment());
            case TRAINING_WORD:
                return db.query(WORDS_TABLE, null, Columns.WORD_ID + "=?", new String[]{uri.getLastPathSegment()}, null, null, sortOrder);
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case UriMatcher.NO_MATCH:
                return "no match";
            case TRAINING:
                return "training";
            case TRAINING_WORD:
                return "training word";
        }
        return "not work";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (dbHelper.getWritableDatabase().insert(WORDS_TABLE, null, values) >= 0)
            return uri;
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return dbHelper.getWritableDatabase().delete(WORDS_TABLE, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return dbHelper.getWritableDatabase().update(WORDS_TABLE, values, selection, selectionArgs);
    }
}
