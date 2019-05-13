package com.gmail.randzjx.words.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.randzjx.words.R;
import com.gmail.randzjx.words.database.WordsContentProvider;
import com.gmail.randzjx.words.database.WordsDbSchema;
import com.gmail.randzjx.words.database.helper.CursorAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

public class FragmentPager extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, FragmentWordEdit.CallbackWordEdit {
    private static final String[] projection_words = new String[]{WordsDbSchema.WordsTable.Columns.WORD_ID};
    private static final int LOADER_ID = 0;
    private static final String WORD_KEY = "wordkey";

    private ViewPager mViewPager;
    private List<String> wordKeys;
    private String currentWord;

    private Context mContext;

    public static FragmentPager getInstance(String word) {
        FragmentPager fragment = new FragmentPager();
        Bundle arg = new Bundle();
        arg.putString(WORD_KEY, word);
        fragment.setArguments(arg);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if (getArguments() != null && getArguments().containsKey(WORD_KEY)) {
            currentWord = getArguments().getString(WORD_KEY);
        }
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager, container, false);

        mViewPager = v.findViewById(R.id.viewpager_word_edit);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (wordKeys != null) {
                    return FragmentWordEdit.getInstance(wordKeys.get(position))
                            .setCallback(FragmentPager.this);
                }
                return FragmentWordEdit.getInstance("Error");
            }

            @Override
            public int getCount() {
                if (wordKeys != null) return wordKeys.size();
                return 0;
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentWord = wordKeys.get(position);
                Log.d("Logs", "onPageSelected currentWord " + currentWord);
            }
        });


        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(WORD_KEY, currentWord);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(WORD_KEY)) {
            currentWord = savedInstanceState.getString(WORD_KEY);
            Log.d("Logs", "currentWord " + currentWord);
        }
    }

    private void update() {
        if (mViewPager.getAdapter() != null) {
            mViewPager.getAdapter().notifyDataSetChanged();
            mViewPager.setCurrentItem(wordKeys.indexOf(currentWord), false);

        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(mContext, WordsContentProvider.CONTENT_URI, projection_words, null, null, WordsDbSchema.WordsTable.Columns.WORD_ID);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            wordKeys = CursorAdapter.toList(data);
        } else {
            if (wordKeys == null) wordKeys = new ArrayList<>();
        }
        wordKeys.add(0, getString(R.string.TAG_new_word));
        update();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    public void actionPerformed(String wordkey, int ACTION) {
        switch (ACTION) {
            case FragmentWordEdit.DELETE:
                int pos = wordKeys.indexOf(wordkey);
                if (pos >= 0) {
                    wordKeys.remove(pos);
                    if (wordKeys.size() == 0) {
                        if (getFragmentManager() != null)
                            getFragmentManager().popBackStack();
                        return;
                    }
                    if (pos > 0) pos--;
                    currentWord = wordKeys.get(pos);
                    update();
                }
                break;
            case FragmentWordEdit.NEW:
                currentWord = getString(R.string.TAG_new_word);
//                wordKeys.remove(currentWord);
//                wordKeys.add(mViewPager.getCurrentItem(), currentWord);
//                update();
//                break;
            case FragmentWordEdit.UPDATE:
                LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, this);
        }
    }
}
