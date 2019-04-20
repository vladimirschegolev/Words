package com.gmail.randzjx.words.loader;

import android.content.Context;
import android.text.Editable;

import com.gmail.randzjx.words.database.Word;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class LoadDescription extends AsyncTaskLoader<Word> {
    public LoadDescription(@NonNull Context context, Editable args) {
        super(context);
    }

    @Nullable
    @Override
    public Word loadInBackground() {
        return null;
    }
}
