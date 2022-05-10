package es.gidm.backstack;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class MyRecyclerViewAdapter_languages extends RecyclerView.Adapter<MyRecyclerViewAdapter_languages
        .ViewHolder> {

    private List<String> lists;
    private HashMap<String,Integer> colors;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapter_languages(Context context, List<String> lists, HashMap<String,Integer> colors) {
        this.mInflater = LayoutInflater.from(context);
        this.lists = lists;
        this.colors = colors;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_languages, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String listName = lists.get(position);
        holder.listName.setText(listName);
        if(colors.get(lists.get(position)) == 1) {
            // color background
            holder.listName.setBackgroundColor(0xfff00000);
        }
        else {
            // dont color background
            holder.listName.setBackgroundColor(0xaea12aa2);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return lists.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView listName;

        ViewHolder(View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.listName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return lists.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}