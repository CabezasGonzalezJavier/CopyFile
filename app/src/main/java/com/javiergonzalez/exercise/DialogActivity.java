package com.javiergonzalez.exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.javiergonzalez.exercise.adapters.ListDialogAdapter;
import com.javiergonzalez.exercise.interfaces.DateListener;

import java.util.ArrayList;
import java.util.List;


public class DialogActivity extends Activity implements DateListener {

    private ListView mListView;
    private ListDialogAdapter mAdapter;
    private String[] mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        mListView = (ListView) findViewById(R.id.activity_dialog_Listview);
        mList= getResources().getStringArray(R.array.cases);

        mAdapter = new ListDialogAdapter(this,mList,getString(R.string.activity_dialog_not_set),this);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dialog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getDate(int year, int month, int day) {
        String currentDate = new StringBuilder().append(day).append(".")
                .append(month + 1).append(".").append(year).toString();
        mAdapter.notifyDataSetChanged();
        mAdapter = new ListDialogAdapter(this,mList,currentDate,this);
        mListView.setAdapter(mAdapter);
    }
}
