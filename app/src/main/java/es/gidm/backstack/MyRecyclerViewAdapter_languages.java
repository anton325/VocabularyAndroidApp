package es.gidm.backstack;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;
import java.util.Random;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MyRecyclerViewAdapter_languages extends RecyclerView.Adapter<MyRecyclerViewAdapter_languages
        .ViewHolder> {

    private List<String> lists;
    private HashMap<String,Integer> colors;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context myContext;

    // data is passed into the constructor
    MyRecyclerViewAdapter_languages(Context context, List<String> lists, HashMap<String,Integer> colors,Context myContext) {
        this.mInflater = LayoutInflater.from(context);
        this.lists = lists;
        this.colors = colors;
        this.myContext = myContext;
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
            holder.listName.setBackgroundColor(0xff49b675);  //0x7a7a7a grey
        }
        else {
            // dont color background
            holder.listName.setBackgroundColor(0xffffff); // 0xffffff white
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