package com.javiergonzalez.exercise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.javiergonzalez.exercise.R;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 9/7/15.
 */
public class ListAdapter extends ArrayAdapter<String> {


    private Context mContext;

    private List<String> mList;

    public ListAdapter(Context context, List<String> list) {
        super(context, R.layout.row_list, list);
        mContext = context;
        mList = list;
    }

    static class ViewHolder {
        public TextView titleTextView;
        public TextView intergerTextView;

        public Button button;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.row_list, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) rowView.findViewById(R.id.row_list_title);
            viewHolder.intergerTextView = (TextView) rowView.findViewById(R.id.row_list_interger);
            viewHolder.button = (Button) rowView.findViewById(R.id.row_list_button);

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.titleTextView.setText(mList.get(position));
        holder.intergerTextView.setText(mList.get(position));

        return rowView;
    }
}
