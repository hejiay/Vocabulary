package com.example.hejiayuan.vocabulary.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.activities.MainActivity;
import com.example.hejiayuan.vocabulary.databases.WordList;
import com.example.hejiayuan.vocabulary.utils.MyApplication;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder>{
    private List<WordList> mWordLsits;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View wordListView;
        TextView wordText;
        TextView interpretText;
        ImageButton imgBtnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            wordListView = itemView;
            wordText = (TextView) itemView.findViewById(R.id.id_note_text_word);
            interpretText = (TextView) itemView.findViewById(R.id.id_note_text_interpret);
            imgBtnDelete = (ImageButton) itemView.findViewById(R.id.image_note_btn_delete);
        }
    }

    public WordListAdapter(List<WordList> mWordLsits) {
        this.mWordLsits = mWordLsits;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_word_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.wordListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                WordList wordList = mWordLsits.get(position);
                Toast.makeText(MyApplication.getContext(), " " + position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContext(), "点击删除", Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WordList wordList = mWordLsits.get(position);
        holder.wordText.setText(wordList.getWord());
        holder.interpretText.setText(wordList.getInterpret());
    }

    @Override
    public int getItemCount() {
        return mWordLsits.size();
    }

    private void clickWordList() {

    }

    private void clickDeleteIcon() {

    }
}
