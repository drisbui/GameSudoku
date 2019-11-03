package com.example.gamesudoku.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gamesudoku.AchievementActivity;
import com.example.gamesudoku.R;

import java.util.ArrayList;
import java.util.List;


public class AchievementAdapter extends  BaseAdapter {

    private Context context;
    private ArrayList<Achievements> lst;


    public AchievementAdapter(@NonNull Context context, @NonNull ArrayList<Achievements> objects) {
        this.context=context;
        lst=objects;
    }

    @Override
    public int getCount() {
        if (lst.size() != 0 && !lst.isEmpty()) {
            return lst.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.layoutcustom_grid,null);
        }
        TextView tvID=view.findViewById(R.id.tvID);
        TextView tvName=view.findViewById(R.id.tvName);
        TextView tvTime=view.findViewById(R.id.tvTime);
        TextView tvDate=view.findViewById(R.id.tvDate);

        Achievements a=lst.get(i);
        if(lst!=null && !lst.isEmpty())
        {
            tvID.setText(a.getId());
            tvName.setText(a.getName());
            tvDate.setText(a.getDate());
            tvTime.setText(a.getTime());
        }
        return view;
    }
}
