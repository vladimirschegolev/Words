package com.gmail.randzjx.words.activity;

import android.os.Bundle;

import com.gmail.randzjx.words.R;
import com.gmail.randzjx.words.fragments.FragmentReader;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class ActivityReader extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getData() != null) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag(getString(R.string.TAG_reader));
            if (fragment == null) {
                fragment = FragmentReader.getInstance(getIntent().getData());
                fm.beginTransaction()
                        .add(fragment, getString(R.string.TAG_reader))
                        .commit();
            }
        }

    }
}
