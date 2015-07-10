package com.javiergonzalez.exercise;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.javiergonzalez.exercise.adapters.ListAdapter;
import com.javiergonzalez.exercise.utils.SimpleFileDialog;
import com.javiergonzalez.exercise.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener, FoldersFilesListener {


    ScrollView mScrollView;
    List<String> mList;
    ListAdapter mAdapter;

    ListView mListView;
    TextView mOriginTextView;
    TextView mDestinationTextView;

    String mOriginPath;
    String mDestinationPath;

    final static int TYPE_DESTINO = 2;
    final static int TYPE_ORIGIN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button origitnButton = (Button) findViewById(R.id.button_origin);
        origitnButton.setOnClickListener(this);
        mOriginTextView = (TextView) findViewById(R.id.textview_origin);

        Button destinationButton = (Button) findViewById(R.id.button_destination);
        destinationButton.setOnClickListener(this);
        mDestinationTextView = (TextView) findViewById(R.id.textview_destination);

        Button copy = (Button) findViewById(R.id.button_copy);
        copy.setOnClickListener(this);


        mListView = (ListView) findViewById(R.id.listview);
        mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mListView.setStackFromBottom(true);

        mList = new ArrayList<String>();
        mAdapter = new ListAdapter(this, mList);
        mListView.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View v) {
        int type = 0;
        switch (v.getId()) {
            case R.id.button_origin:
                chosen(TYPE_ORIGIN);
                break;
            case R.id.button_destination:
                chosen(TYPE_DESTINO);
                break;
            case R.id.button_copy:
                if (mOriginPath != null && mDestinationPath != null) {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                    Utils.copyFileOrDirectory(this, mOriginPath, mDestinationPath);

                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
                }
        }
    }

    public void chosen(final int type) {
        //Create FileOpenDialog and register a callback
        SimpleFileDialog FolderChooseDialog = new SimpleFileDialog(MainActivity.this, "FolderChoose",
                new SimpleFileDialog.SimpleFileDialogListener() {
                    @Override
                    public void onChosenDir(String chosenDir) {
                        // The code in this function will be executed when the dialog OK button is pushed
                        Toast.makeText(MainActivity.this, "Chosen FileOpenDialog File: " +
                                chosenDir, Toast.LENGTH_LONG).show();
                        switch (type) {
                            case TYPE_DESTINO:
                                mDestinationTextView.setText(chosenDir);
                                mDestinationPath = chosenDir;
                                break;
                            case TYPE_ORIGIN:
                                mOriginTextView.setText(chosenDir);
                                mOriginPath = chosenDir;
                                break;
                        }
                    }
                });

        FolderChooseDialog.chooseFile_or_Dir();
    }

    @Override
    public void paintFolderFile(final String stringList) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mList.add(stringList);
                mAdapter.setListData(mList);
                mAdapter.notifyDataSetChanged();
                mListView.requestLayout();
            }
        });
    }
}
