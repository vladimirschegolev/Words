package com.gmail.randzjx.words.fragments;

import android.os.Bundle;

import com.gmail.randzjx.words.R;

import androidx.preference.PreferenceFragmentCompat;

public class FragmentPreferences extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
