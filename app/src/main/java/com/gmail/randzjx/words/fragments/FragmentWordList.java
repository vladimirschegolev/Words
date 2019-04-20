package com.gmail.randzjx.words.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gmail.randzjx.words.R;
import com.gmail.randzjx.words.activity.ActivityWordEditPager;
import com.gmail.randzjx.words.database.WordsContentProvider;
import com.gmail.randzjx.words.database.WordsDbSchema.WordsTable.Columns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentWordList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String[] projection_words = new String[]{Columns.WORD_ID};
    private static final int LOADER_ID = 0;

    private ImageButton brnAdd;
    private WordListAdapter adapter;
    private Context mContext;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        LoaderManager.getInstance(this).destroyLoader(LOADER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_word_list, container, false);
        brnAdd = view.findViewById(R.id.btn_add_word);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_word_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (adapter == null) adapter = new WordListAdapter();
        recyclerView.setAdapter(adapter);
        initLis();
        return view;
    }

    private void initLis() {
        brnAdd.setOnClickListener(v -> {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(mContext, WordsContentProvider.CONTENT_URI, projection_words, null, null, Columns.WORD_ID);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


    private class ListHolder extends RecyclerView.ViewHolder {

        TextView text;

        ListHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.word_list_item, parent, false));
            text = itemView.findViewById(R.id.text_view_item);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String size = pref.getString("list_font_size", "14");
            if (size != null)
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(size));
        }
    }

    private class WordListAdapter extends RecyclerView.Adapter<ListHolder> {

        private Cursor dataCursor;

        void swapCursor(Cursor cursor) {
            if (dataCursor == cursor) {
                return;
            }
            this.dataCursor = cursor;
            if (cursor != null) {
                this.notifyDataSetChanged();
            }
        }

        private void startEdit(String word) {
//            FragmentActivity fa = FragmentWordList.this.getActivity();
//            if (fa != null) {
//                Fragment fragment = fa.getSupportFragmentManager().findFragmentByTag(getString(R.string.TAG_word_edit_fragment));
//                if (fragment == null) {
//                    fragment = FragmentWordEdit.getInstance(word);
//                    fa.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, fragment, getString(R.string.TAG_word_edit_fragment))
//                            .addToBackStack(null)
//                            .commit();
//                }
//            }
            Intent intent = new Intent(getActivity(), ActivityWordEditPager.class);
            intent.putExtra("wordkey", word);
            startActivity(intent);
        }

        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            final ListHolder holder = new ListHolder(inflater, viewGroup);
            holder.itemView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && dataCursor.moveToPosition(position)) {
                    startEdit(dataCursor.getString(0));
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder holder, int position) {
            if (dataCursor.moveToPosition(position)) {
                holder.text.setText(dataCursor.getString(0));
            }
        }

        @Override
        public int getItemCount() {
            return dataCursor == null ? 0 : dataCursor.getCount();
        }
    }
}
