package com.gmail.randzjx.words.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.gmail.randzjx.words.database.helper.Adapter;
import com.gmail.randzjx.words.database.Word;
import com.gmail.randzjx.words.database.WordsContentProvider;
import com.gmail.randzjx.words.database.WordsDbSchema.WordsTable.Columns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class GetDescription extends AsyncTaskLoader<Word> {
    private String mWordKey;

    public GetDescription(@NonNull Context context, String wordkey) {
        super(context);
        mWordKey = wordkey;
    }

    @Nullable
    @Override
    public Word loadInBackground() {
        if (mWordKey == null || mWordKey.equals("")) return null;

        ContentResolver cr = getContext().getContentResolver();
        Cursor cursor = cr.query(WordsContentProvider.CONTENT_URI, null, Columns.WORD_ID + "=?", new String[]{mWordKey}, null);
        if (cursor == null) return null;
        Word word = Adapter.getWord(cursor);
        cursor.close();
        return word;
    }
}
