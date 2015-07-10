package com.javiergonzalez.exercise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.javiergonzalez.exercise.R;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 9/7/15.
 */
public class ListAdapter extends BaseAdapter {


    private Context mContext;

    private List<String> mList;

    public ListAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    public void setListData(List<String> data){
        mList = data;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        public TextView titleTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.row_list, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) rowView.findViewById(R.id.row_list_title);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.titleTextView.setText(mList.get(position));

        return rowView;
    }
}