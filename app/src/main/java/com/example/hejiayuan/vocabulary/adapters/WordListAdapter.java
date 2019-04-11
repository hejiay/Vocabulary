package com.example.hejiayuan.vocabulary.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.databases.WordList;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder>{
    private List<WordList> mWordLsit;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordText;
        TextView interpretText;
        ImageButton imgBtnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            wordText = (TextView) itemView.findViewById(R.id.id_note_text_word);
            interpretText = (TextView) itemView.findViewById(R.id.id_note_text_interpret);
            imgBtnDelete = (ImageButton) itemView.findViewById(R.id.image_note_btn_delete);
        }
    }

    public WordListAdapter(List<WordList> mWordLsit) {
        this.mWordLsit = mWordLsit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_word_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WordList wordList = mWordLsit.get(position);
        holder.wordText.setText(wordList.getWord());
        holder.interpretText.setText(wordList.getInterpret());
    }

    @Override
    public int getItemCount() {
        return mWordLsit.size();
    }
}
