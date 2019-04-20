package com.gmail.randzjx.words.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import com.gmail.randzjx.words.R;
import com.gmail.randzjx.words.activity.ActivityPreferences;
import com.gmail.randzjx.words.activity.ActivityReader;
import com.gmail.randzjx.words.activity.ActivityWordEditPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class FragmentStartMenu extends Fragment {
    Button addWord, trainWord, wordList, openBook, settings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("log", "start fragment OK");
        View view = inflater.inflate(R.layout.fragment_start_menu, container, false);
        addWord = view.findViewById(R.id.btn_add_word_start);
        trainWord = view.findViewById(R.id.btn_train_word);
        wordList = view.findViewById(R.id.btn_word_list);
        settings = view.findViewById(R.id.btn_settings);
//        openBook = view.findViewById(R.id.btn_open_book);

        initLis();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null &&
                MimeTypeMap.getFileExtensionFromUrl(data.getData().getPath()).equals("fb2")) {
            Intent intent = new Intent(getActivity(), ActivityReader.class);
            intent.setData(data.getData());
            startActivity(intent);
        }

    }

    private void initLis() {
        addWord.setOnClickListener(v -> {
//            FragmentActivity fa = getActivity();
//            if (fa != null) {
//                Fragment fragment = fa.getSupportFragmentManager().findFragmentByTag(getString(R.string.TAG_word_edit_fragment));
//                if (fragment == null) {
//                    fragment = new FragmentWordEdit();
//                    fa.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, fragment, getString(R.string.TAG_word_edit_fragment))
//                            .addToBackStack(null)
//                            .commit();
//                }
//            }
            Intent intent = new Intent(getActivity(), ActivityWordEditPager.class);
            intent.putExtra("wordkey", getString(R.string.TAG_new_word));
            startActivity(intent);
        });

        wordList.setOnClickListener(v -> {
            FragmentActivity fa = getActivity();
            if (fa != null) {
                Fragment fragment = fa.getSupportFragmentManager().findFragmentByTag(getString(R.string.TAG_list_fragment));
                if (fragment == null) {
                    fragment = new FragmentWordList();
                    fa.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment, getString(R.string.TAG_list_fragment))
                            .addToBackStack(null)
                            .commit();
                }

            }
        });

        trainWord.setOnClickListener(v -> {
            FragmentActivity fa = getActivity();
            if (fa != null) {
                Fragment fragment = fa.getSupportFragmentManager().findFragmentByTag(getString(R.string.TAG_train_fragment));
                if (fragment == null) {
                    fragment = new FragmentTrainWords();
                    fa.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment, getString(R.string.TAG_train_fragment))
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ActivityPreferences.class);
            startActivity(intent);
        });

//        openBook.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            intent.setType("*/*");
//            startActivityForResult(intent, 42);
//        });
    }

}
