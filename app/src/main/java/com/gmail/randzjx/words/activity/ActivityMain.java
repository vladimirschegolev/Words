package com.gmail.randzjx.words.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.gmail.randzjx.words.R;
import com.gmail.randzjx.words.fragments.FragmentStartMenu;
import com.gmail.randzjx.words.fragments.FragmentWordEdit;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class ActivityMain extends FragmentActivity implements FragmentWordEdit.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(getString(R.string.TAG_start_menu));
        Log.d("log", "start fragment " + String.valueOf(fragment));
        if (fragment == null) {
            fragment = new FragmentStartMenu();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, getString(R.string.TAG_start_menu))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("settings");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void actionPerformed(String wordkey, int ACTION) {

    }
}
