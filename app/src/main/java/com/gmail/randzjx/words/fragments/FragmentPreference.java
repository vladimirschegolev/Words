package com.gmail.randzjx.words.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.gmail.randzjx.words.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentPreference extends Fragment {

    private SharedPreferences pref;
    private SeekBar sbListFontSize, sbTrainFontSize, sbTrainSize;
    private TextView tvListFontSize, tvTrainFontSize, tvTrainSize;
    private Switch language, theme;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preferences, container, false);

        sbTrainSize = v.findViewById(R.id.sb_train_size);
        tvTrainSize = v.findViewById(R.id.tv_train_size);

        sbTrainFontSize = v.findViewById(R.id.sb_train_font_size);
        tvTrainFontSize = v.findViewById(R.id.tv_train_font_size);

        sbListFontSize = v.findViewById(R.id.sb_list_font_size);
        tvListFontSize = v.findViewById(R.id.tv_list_font_size);

        language = v.findViewById(R.id.desc_lang);
        theme = v.findViewById(R.id.switch_theme);

        loadPref();
        initLis();

        return v;
    }

    private void loadPref() {
        sbTrainSize.setProgress(pref.getInt(getString(R.string.pref_train_size), 3) - 2);
        sbTrainFontSize.setProgress(pref.getInt(getString(R.string.pref_train_font_size), 20) - 10);
        sbListFontSize.setProgress(pref.getInt(getString(R.string.pref_list_font_size), 20) - 10);

        language.setChecked(pref.getBoolean(getString(R.string.pref_description), true));
        if (language.isChecked()) {
            language.setText(getText(R.string.description1));
        } else {
            language.setText(getText(R.string.description2));
        }

        theme.setChecked(pref.getBoolean(getString(R.string.pref_night_theme), true));
    }

    private void initLis() {
        sbTrainSize.setOnSeekBarChangeListener(
                new BarListener(sbTrainSize.getProgress(), 2, getString(R.string.train_size), tvTrainSize, getString(R.string.pref_train_size)));
        sbTrainFontSize.setOnSeekBarChangeListener(
                new BarListener(sbTrainFontSize.getProgress(), 10, getString(R.string.train_font_size), tvTrainFontSize, getString(R.string.pref_train_font_size)));
        sbListFontSize.setOnSeekBarChangeListener(
                new BarListener(sbListFontSize.getProgress(), 10, getString(R.string.list_font_size), tvListFontSize, getString(R.string.pref_list_font_size)));

        language.setOnCheckedChangeListener((buttonView, isChecked) -> {
            pref.edit().putBoolean(getString(R.string.pref_description), isChecked).apply();
            if (isChecked) {
                language.setText(getText(R.string.description1));
            } else {
                language.setText(getText(R.string.description2));
            }
        });

        theme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            pref.edit().putBoolean(getString(R.string.pref_night_theme), isChecked).apply();
            restart();
        });


    }

    private void restart() {
        if (getActivity() != null) getActivity().recreate();
    }

    private class BarListener implements SeekBar.OnSeekBarChangeListener {

        private int offset;
        private String format, preference;
        private TextView text;

        public BarListener(int start, int offset, String format, TextView text, String preference) {
            this.offset = offset;
            this.format = format;
            this.preference = preference;
            this.text = text;
            text.setText(String.format(format, offset + start));
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            text.setText(String.format(format, offset + seekBar.getProgress()));
            pref.edit().putInt(preference, offset + seekBar.getProgress()).apply();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
