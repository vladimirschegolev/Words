package com.gmail.randzjx.words.activity;

import android.os.Bundle;

import com.gmail.randzjx.words.R;
import com.gmail.randzjx.words.fragments.FragmentPreferences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityPreferences extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new FragmentPreferences())
                .commit();
    }
}
