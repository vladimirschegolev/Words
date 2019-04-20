package com.gmail.randzjx.words.activity;

import android.database.Cursor;
import android.os.Bundle;

import com.gmail.randzjx.words.R;
import com.gmail.randzjx.words.database.WordsContentProvider;
import com.gmail.randzjx.words.database.WordsDbSchema.WordsTable.Columns;
import com.gmail.randzjx.words.database.helper.Adapter;
import com.gmail.randzjx.words.fragments.FragmentWordEdit;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ActivityWordEditPager extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, FragmentWordEdit.Callbacks {

    private static final String[] projection_words = new String[]{Columns.WORD_ID};
    private static final int LOADER_ID = 0;
    private static final String WORD_KEY = "wordkey";

    private ViewPager mViewPager;
    private List<String> wordkeys;
    private String currentWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        if (savedInstanceState != null && savedInstanceState.containsKey(WORD_KEY)) {
            currentWord = savedInstanceState.getString(WORD_KEY);
        } else {
            Bundle extra = getIntent().getExtras();
            if (extra != null && extra.containsKey(WORD_KEY)) {
                currentWord = extra.getString(WORD_KEY);
            }
        }

        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);

        mViewPager = findViewById(R.id.viewpager_word_edit);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (wordkeys != null) {
                    return FragmentWordEdit.getInstance(wordkeys.get(position));
                }
                return FragmentWordEdit.getInstance("");
            }

            @Override
            public int getCount() {
                if (wordkeys != null) return wordkeys.size();
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
                currentWord = wordkeys.get(position);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("wordkey", currentWord);
    }

    private int getPosition(String wordkey) {
        return wordkeys.indexOf(wordkey);
    }

    private void update() {
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            if (currentWord != null) {
                mViewPager.setCurrentItem(getPosition(currentWord), false);
            }
        }
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getApplicationContext(), WordsContentProvider.CONTENT_URI, projection_words, null, null, Columns.WORD_ID);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            wordkeys = Adapter.toList(data);
            if (getString(R.string.TAG_new_word).equals(currentWord)) wordkeys.add(0, currentWord);
            update();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoaderManager.getInstance(this).destroyLoader(LOADER_ID);
    }

    @Override
    public void actionPerformed(String wordkey, int ACTION) {
        switch (ACTION) {
            case FragmentWordEdit.DELETE:
                int pos = wordkeys.indexOf(wordkey);
                if (pos >= 0) {
                    wordkeys.remove(pos);
                    if (wordkeys.size() == 0) {
                        finish();
                        return;
                    }
                    if (pos > 0) pos--;
                    currentWord = wordkeys.get(pos);
                    update();
                }
                break;
            case FragmentWordEdit.NEW:
                if (!wordkeys.contains(getString(R.string.TAG_new_word))) {
                    currentWord = getString(R.string.TAG_new_word);
                    wordkeys.add(mViewPager.getCurrentItem(), currentWord);
                    update();
                }
                break;
            case FragmentWordEdit.UPDATE:
                LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
        }

    }
}
