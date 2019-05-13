package com.gmail.randzjx.words.fragments;

import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.randzjx.words.R;
import com.gmail.randzjx.words.database.Word;
import com.gmail.randzjx.words.database.WordsContentProvider;
import com.gmail.randzjx.words.database.WordsDbSchema.WordsTable.Columns;
import com.gmail.randzjx.words.database.helper.CursorAdapter;
import com.gmail.randzjx.words.translator.Translation;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.Context.CLIPBOARD_SERVICE;

public class FragmentWordEdit extends Fragment implements View.OnClickListener {

    public static final int DELETE = 0;
    public static final int UPDATE = 1;
    public static final int NEW = 2;

    private static final String WORD_KEY = "wordkey";
    private static final String TV_WORD_KEY = "tvwordkey";
    private static final String TV_FIRST_KEY = "tvfirstkey";
    private static final String TV_SECOND_KEY = "tvsecondkey";

    private Button paste, loadDescription, save, delete, addNew;
    private EditText tWord, tDescriptionFirst, tDescriptionSecond;
    private LinearLayout lAlternative;
    private String wordKey;
    private Context mContext;
    private View view;
    private CallbackWordEdit mCallback;

    public static FragmentWordEdit getInstance(String wordKey) {
        FragmentWordEdit fragment = new FragmentWordEdit();
        Bundle bundle = new Bundle();
        bundle.putString(WORD_KEY, wordKey);
        fragment.setArguments(bundle);
        return fragment;
    }

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
        view = inflater.inflate(R.layout.fragment_word_edit, container, false);

        tWord = view.findViewById(R.id.word);
        lAlternative = view.findViewById(R.id.layout_alternative);
        paste = view.findViewById(R.id.btn_paste);
        loadDescription = view.findViewById(R.id.btn_load);
        save = view.findViewById(R.id.btn_add_word_start);
        delete = view.findViewById(R.id.btn_delete);
        addNew = view.findViewById(R.id.btn_add_word_edit);
        tDescriptionFirst = view.findViewById(R.id.text_description_first);
        tDescriptionSecond = view.findViewById(R.id.text_description_second);

        initWordKey();

        initLis();

        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            wordKey = savedInstanceState.getString(WORD_KEY);
            tWord.setText(savedInstanceState.getString(TV_WORD_KEY));
            tDescriptionFirst.setText(savedInstanceState.getString(TV_FIRST_KEY));
            tDescriptionSecond.setText(savedInstanceState.getString(TV_SECOND_KEY));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(WORD_KEY, wordKey);
        outState.putString(TV_WORD_KEY, tWord.getText().toString());
        outState.putString(TV_FIRST_KEY, tDescriptionFirst.getText().toString());
        outState.putString(TV_SECOND_KEY, tDescriptionSecond.getText().toString());
    }

    private void initWordKey() {
        if (wordKey == null && getArguments() != null && getArguments().containsKey(WORD_KEY)) {
            wordKey = getArguments().getString(WORD_KEY);
            Log.d("args", "wordkey " + String.valueOf(wordKey));
            if (wordKey.equals(getString(R.string.TAG_new_word))) return;
            tWord.setText(wordKey);
            ContentResolver cr = mContext.getContentResolver();
            Cursor cursor = cr.query(WordsContentProvider.CONTENT_URI, null, Columns.WORD_ID + "=?", new String[]{wordKey}, null);
            if (cursor == null || cursor.getCount() == 0) return;
            Word word = CursorAdapter.getWord(cursor);
            cursor.close();
            tDescriptionFirst.setText(word.getFirstDescription());
            tDescriptionSecond.setText(word.getSecondDescription());
        }
    }

    private void initLis() {
        paste.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
            if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN)) {
                String data = String.valueOf(clipboard.getPrimaryClip().getItemAt(0).getText());
                if (!data.equals("null"))
                    tWord.setText(data);
            }
        });

        loadDescription.setOnClickListener(v -> {
            if (!tWord.getText().toString().equals("")) {
                new LoadTranslation(this).execute(tWord.getText().toString());
            }
        });


        save.setOnClickListener(v -> {
            if (save() && mCallback != null)
                mCallback.actionPerformed(null, UPDATE);

            hideKeyboard();
        });

        delete.setOnClickListener(v -> {
            if (!getString(R.string.TAG_new_word).equals(wordKey)) {
                mContext.getContentResolver().delete(WordsContentProvider.CONTENT_URI, Columns.WORD_ID + " = ?", new String[]{wordKey});
                if (mCallback != null) mCallback.actionPerformed(wordKey, DELETE);
            }
            hideKeyboard();
        });

        addNew.setOnClickListener(v -> {
            save();
            clear();
            if (mCallback != null) mCallback.actionPerformed(null, NEW);
        });
    }

    private void clear() {
        wordKey = getString(R.string.TAG_new_word);
        tWord.setText("");
        tDescriptionFirst.setText("");
        tDescriptionSecond.setText("");
    }

    private boolean save() {
        if (!tWord.getText().toString().equals("") && !tWord.getText().toString().equals(getString(R.string.TAG_new_word))) {
            Word word = new Word();
            word.setWord(tWord.getText().toString().toLowerCase());
            word.setFirstDescription(tDescriptionFirst.getText().toString());
            word.setSecondDescription(tDescriptionSecond.getText().toString());

            ContentResolver cr = mContext.getContentResolver();
            if (wordKey == null || wordKey.equals(getString(R.string.TAG_new_word))) {
                cr.insert(WordsContentProvider.CONTENT_URI, CursorAdapter.getContentValues(word));
            } else if (wordKey.equals(word.getWord())) {
                cr.update(WordsContentProvider.CONTENT_URI, CursorAdapter.getContentValues(word), Columns.WORD_ID + " = ?", new String[]{wordKey});
            } else {
                cr.delete(WordsContentProvider.CONTENT_URI, Columns.WORD_ID + " = ?", new String[]{wordKey});
                cr.insert(WordsContentProvider.CONTENT_URI, CursorAdapter.getContentValues(word));
            }
            wordKey = word.getWord();
            return true;
        }
        return false;
    }

    private void hideKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }

    }

    @Override
    public void onClick(View v) {
        tWord.setText(((TextView) v).getText());
    }

    public FragmentWordEdit setCallback(CallbackWordEdit callback) {
        this.mCallback = callback;
        return this;
    }

    private static class LoadTranslation extends AsyncTask<String, Void, Translation> {
        private WeakReference<FragmentWordEdit> mFragment;


        LoadTranslation(FragmentWordEdit fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            mFragment.get().loadDescription.setEnabled(false);
        }

        @Override
        protected Translation doInBackground(String... strings) {
            return new Translation(strings[0]);
        }

        @Override
        protected void onPostExecute(Translation translation) {
            Log.d("translation.success ", String.valueOf(translation.success()));
            if (mFragment != null){
                if (translation.success()) {
                    mFragment.get().loadDescription.setEnabled(true);
                    mFragment.get().tDescriptionFirst.setText(translation.getDescription());
                    mFragment.get().tDescriptionSecond.setText(translation.getTranslate());
                    mFragment.get().lAlternative.removeAllViews();
                    String[] alt = translation.getAlternative();
                    if (alt != null) {
                        for (int i = 0; i < alt.length; i++) {
                            TextView tv = new TextView(mFragment.get().mContext);
                            tv.setText(alt[i]);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                            tv.setOnClickListener(mFragment.get());
                            mFragment.get().lAlternative.addView(tv);
                        }
                    }
                } else {
                    mFragment.get().loadDescription.setEnabled(true);
                }
            }

        }
    }

    public interface CallbackWordEdit {
        void actionPerformed(String wordkey, int ACTION);
    }
}
