package com.gmail.randzjx.words.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.randzjx.words.R;
import com.gmail.randzjx.words.database.Word;
import com.gmail.randzjx.words.database.WordsContentProvider;
import com.gmail.randzjx.words.database.WordsDbSchema.WordsTable.Columns;
import com.gmail.randzjx.words.database.helper.Adapter;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

public class FragmentTrainWords extends Fragment implements View.OnClickListener {


    private LinearLayout layout;
    private TextView wordTextView;
    private Word[] words;
    private TextView[] descriptions;
    private long time;
    private boolean pick;
    private Context mContext;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_training, container, false);
        wordTextView = view.findViewById(R.id.word_train);
        layout = view.findViewById(R.id.layout_desc);
        return view;
    }

    private void updateUI() {
        wordTextView.setText(words[0].getWord());
        if (descriptions == null) {
            descriptions = new TextView[words.length];
            for (int i = 0; i < words.length; i++) {
                descriptions[i] = getNewTextView();
                descriptions[i].setOnClickListener(this);
            }
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String desc = pref.getString("description", "first");
        layout.removeAllViews();
        for (int i = 0, k = (int) (Math.random() * words.length); i < words.length; i++, k++) {
            if (k == words.length) k = 0;
            if (i % 2 == 0) {
                descriptions[k].setBackgroundColor(Color.rgb(33, 33, 33));
            } else {
                descriptions[k].setBackgroundColor(Color.rgb(22, 22, 22));
            }
            if ("first".equals(desc)) {
                descriptions[k].setText(words[k].getFirstDescription());
            } else {
                descriptions[k].setText(words[k].getSecondDescription());
            }
            layout.addView(descriptions[k]);
        }
    }

    private void getDescriptions() {
        if (words == null) {
            String num = PreferenceManager.getDefaultSharedPreferences(mContext).getString("num_descriptions", "4");
            ContentResolver cr = mContext.getContentResolver();
            Cursor cursor = cr.query(Uri.withAppendedPath(WordsContentProvider.CONTENT_URI, "training/" + num), null, null, null, null);
            if (cursor == null) return;
            words = Adapter.getWords(cursor);
        }
        updateUI();

    }

    private TextView getNewTextView() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        int size = Integer.parseInt(Objects.requireNonNull(pref.getString("training_font_size", "10")));
        TextView out = new TextView(mContext);
        out.setGravity(Gravity.CENTER);
        out.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        out.setPadding(15, 15, 15, 15);
        return out;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!pick) {
            outState.putParcelableArray("words", words);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("words")) {
            words = (Word[]) savedInstanceState.getParcelableArray("words");
        }
        getDescriptions();
    }

    @Override
    public void onClick(View v) {
        if (!pick) {
            ContentResolver cr = mContext.getContentResolver();
            if (v == descriptions[0]) {
                Toast.makeText(mContext, "right", Toast.LENGTH_SHORT).show();
                words[0].guessed();
            } else {
                Toast.makeText(mContext, "wrong", Toast.LENGTH_SHORT).show();
                v.setBackgroundColor(Color.rgb(99, 11, 11));
                words[0].addTry();
            }
            descriptions[0].setBackgroundColor(Color.rgb(11, 99, 11));
            cr.update(WordsContentProvider.CONTENT_URI, Adapter.getContentValues(words[0]), Columns.WORD_ID + "=?", new String[]{words[0].getWord()});
            pick = true;
            time = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - time > 1000) {
            pick = false;
            words = null;
            getDescriptions();
        }
    }
}
