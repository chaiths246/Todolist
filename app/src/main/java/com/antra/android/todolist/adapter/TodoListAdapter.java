package com.antra.android.todolist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.antra.android.todolist.CustomItemClickListener;
import com.antra.android.todolist.R;
import com.antra.android.todolist.db.SqliteDataBase;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.MyViewHolder> {

    private List<String> itemList;
    CustomItemClickListener listener;
    Context mContext;
    private SqliteDataBase mDatabase;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageView img;
        ImageView imgdel;
        ImageView imgedt;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name_list);
            img = (ImageView) view.findViewById(R.id.img_list);
            imgedt = (ImageView) view.findViewById(R.id.edit_list);
            imgdel = (ImageView) view.findViewById(R.id.delete_list);

        }
    }


    public TodoListAdapter(Context mContext,List<String> listItems, CustomItemClickListener listener) {
        this.itemList = listItems;
        this.mContext = mContext;
        this.listener = listener;
        mDatabase = new SqliteDataBase(mContext);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        final MyViewHolder mViewHolder = new MyViewHolder(itemView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        ListItems movie = itemList.get(position);
        holder.title.setText(itemList.get(position));
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNameClick(v, holder.getPosition()+1,holder.title.getText().toString());
            }
        });
//        holder.img.setImageResource(R.drawable.listimg_v);
        holder.imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelClick(position);
            }
        });

        holder.imgedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEditClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void refreshActivity() {
//        ((Activity)mContext).finish();
//        mContext.startActivity(((Activity) mContext).getIntent());
    }

}

