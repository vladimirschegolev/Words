package com.gmail.randzjx.words.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.randzjx.words.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


public class FragmentReader extends Fragment {



    public static Fragment getInstance(Uri uri) {
        FragmentReader reader = new FragmentReader();

        return reader;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reader, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private class ReaderAdapter extends RecyclerView.Adapter<ReaderHolder> {

        @NonNull
        @Override
        public ReaderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ReaderHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class ReaderHolder extends RecyclerView.ViewHolder {

        public ReaderHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

//    private static class BookLoader extends AsyncTask<File, Void, FictionBook> {
//        @Override
//        protected FictionBook doInBackground(File... files) {
//            try {
//                return new FictionBook(files[0]);
//            } catch (ParserConfigurationException | IOException | SAXException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
}
