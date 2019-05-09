package com.example.hejiayuan.vocabulary.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;

import com.example.hejiayuan.vocabulary.R;
import com.example.hejiayuan.vocabulary.activities.DictAvtivity;
import com.example.hejiayuan.vocabulary.databases.DataBaseHelperReview;
import com.example.hejiayuan.vocabulary.databases.WordList;
import com.example.hejiayuan.vocabulary.utils.MyApplication;
import com.example.hejiayuan.vocabulary.utils.PopWindower;


import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder>{
    private List<WordList> mWordLsits;

    private Context context = null;

    private View layout;

    private boolean isFirst = true;


    RecyclerView recyclerView;


    DataBaseHelperReview dbHelper = new DataBaseHelperReview(MyApplication.getContext(), "reviewlist");
    SQLiteDatabase dbW = dbHelper.getWritableDatabase();
    SQLiteDatabase dbR = dbHelper.getReadableDatabase();

    static class ViewHolder extends RecyclerView.ViewHolder {
        View wordListView;
        TextView wordText;
        TextView interpretText;
        ImageButton imgBtnDelete;
        Button graspBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            wordListView = itemView;
            wordText = (TextView) itemView.findViewById(R.id.id_note_text_word);
            interpretText = (TextView) itemView.findViewById(R.id.id_note_text_interpret);
            imgBtnDelete = (ImageButton) itemView.findViewById(R.id.image_note_btn_delete);
            graspBtn = (Button) itemView.findViewById(R.id.id_note_worditem_btn_grasp);
        }
    }

    public WordListAdapter(List<WordList> mWordLsits, Context context, RecyclerView recyclerView) {
        this.mWordLsits = mWordLsits;
        this.context = context;
        this.recyclerView = recyclerView;
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
                gotoDictActivity(wordList);
            }
        });
        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                int position = holder.getAdapterPosition();
                WordList wordList = mWordLsits.get(position);
                String word = wordList.getWord();
                deleteFromWordList(word);
                mWordLsits.remove(position);
                WordListAdapter adapter = new WordListAdapter(mWordLsits, MyApplication.getContext(), recyclerView);
                recyclerView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();

            }
        });
       holder.graspBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int position = holder.getAdapterPosition();
               WordList wordList = mWordLsits.get(position);
               String word = wordList.getWord();
                new PopWindower(v, word, holder.graspBtn).showPopupWindow();
           }
       });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WordList wordList = mWordLsits.get(position);
        String word = wordList.getWord();
         int grasp = 0;
        Cursor cursor = dbR.query("reviewlist", new String[]{ "grasp" }, "word = ?", new String[]{ word }, null, null, null);
        while (cursor.moveToNext()) {
            grasp = cursor.getInt(cursor.getColumnIndex("grasp"));
        }
        holder.wordText.setText(wordList.getWord());
        holder.interpretText.setText(wordList.getInterpret());
        setDefaultSelectOfSpinn(holder, grasp);

    }

    @Override
    public int getItemCount() {
        return mWordLsits.size();
    }

    private void gotoDictActivity(WordList wordList) {
        String word = wordList.getWord();
        Intent intent = new Intent(MyApplication.getContext(), DictAvtivity.class);
        intent.putExtra("searchedword", word);
        MyApplication.getContext().startActivity(intent);//第一次会报错，空指针对象
    }

    public void deleteFromWordList(String word) {
        dbW.delete("reviewlist", "word = ?", new String[]{ word });
    }

    public void setDefaultSelectOfSpinn(ViewHolder holder, int grasp) {
        if (grasp >= 7 && grasp <= 10) {
            holder.graspBtn.setText("熟悉");
        } else if (grasp >= 4 && grasp <= 6) {
            holder.graspBtn.setText("模糊");
        } else {
            holder.graspBtn.setText("陌生");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dbHelper.close();
        dbR.close();
        dbW.close();
    }
}
