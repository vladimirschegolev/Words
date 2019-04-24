package com.gmail.randzjx.words.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.gmail.randzjx.words.R;
import com.gmail.randzjx.words.fragments.FragmentPreference;
import com.gmail.randzjx.words.fragments.FragmentReader;
import com.gmail.randzjx.words.fragments.FragmentTrainWords;
import com.gmail.randzjx.words.fragments.FragmentWordList;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_ID = "FragmentID";

    private int fragmentID = R.id.menu_word_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FRAGMENT_ID, fragmentID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(FRAGMENT_ID)) {
            fragmentID = savedInstanceState.getInt(FRAGMENT_ID);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFragment();
    }

    private void loadFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Fragment fragment;
        switch (fragmentID) {
            case R.id.menu_train:
                fragment = fm.findFragmentByTag(getString(R.string.TAG_train_fragment));
                if (fragment == null) {
                    fragment = new FragmentTrainWords();
                    fm.beginTransaction()
                            .replace(R.id.fragment_container, fragment, getString(R.string.TAG_train_fragment))
                            .commit();
                }
                break;
            case R.id.menu_word_list:
                fragment = fm.findFragmentByTag(getString(R.string.TAG_word_list_fragment));
                if (fragment == null) {
                    fragment = new FragmentWordList();
                    fm.beginTransaction()
                            .replace(R.id.fragment_container, fragment, getString(R.string.TAG_word_list_fragment))
                            .commit();
                }
                break;
            case R.id.menu_preference:
                fragment = fm.findFragmentByTag(getString(R.string.TAG_preference_fragment));
                if (fragment == null) {
                    fragment = new FragmentPreference();
                    fm.beginTransaction()
                            .replace(R.id.fragment_container, fragment, getString(R.string.TAG_preference_fragment))
                            .commit();
                }
                break;
            case R.id.menu_reader:
                fragment = fm.findFragmentByTag(getString(R.string.TAG_reader_fragment));
                if (fragment == null) {
                    fragment = new FragmentReader();
                    fm.beginTransaction()
                            .replace(R.id.fragment_container, fragment, getString(R.string.TAG_reader_fragment))
                            .commit();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragmentID = item.getItemId();
        loadFragment();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
