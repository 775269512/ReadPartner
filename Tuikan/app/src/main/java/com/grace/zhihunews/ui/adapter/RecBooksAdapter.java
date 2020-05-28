package com.grace.zhihunews.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grace.zhihunews.R;
import com.grace.zhihunews.event.GotoNewsDetailEvent;
import com.grace.zhihunews.network.entity.RecBook;
import com.grace.zhihunews.ui.activity.BookInfoActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class RecBooksAdapter extends RecyclerView.Adapter<RecBooksAdapter.ViewHolder> {
    private Context mContext;
    private List<RecBook> recBooks;

    public RecBooksAdapter(Context context, List<RecBook> recBooks) {
        mContext = context;
        this.recBooks = recBooks;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //TODO 适配器修改
        @BindView(R.id.book_img)
        ImageView bookImg;
        @BindView(R.id.book_title)
        TextView bookTitle;
        @BindView(R.id.book_introduce)
        TextView bookIntroduce;
        @BindView(R.id.book_publish)
        TextView bookPublish;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecBooksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View storyView = LayoutInflater.from(context).inflate(R.layout.item_recbook, parent, false);
        final RecBooksAdapter.ViewHolder viewHolder = new RecBooksAdapter.ViewHolder(storyView);
        storyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BookInfoActivity.class);
                mContext.startActivity(intent);
                EventBus.getDefault().post(new GotoNewsDetailEvent(recBooks.get(viewHolder.getLayoutPosition()).getId()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecBooksAdapter.ViewHolder holder, int position) {
        RecBook recBook = recBooks.get(position);
        holder.bookTitle.setText(recBook.getTitle());
        holder.bookIntroduce.setText(recBook.getIntroduce());
        holder.bookPublish.setText(recBook.getPublish());
        Picasso.with(mContext).load(recBook.getImage()).into(holder.bookImg);
    }

    @Override
    public int getItemCount() {
        return recBooks.size();
    }
}
