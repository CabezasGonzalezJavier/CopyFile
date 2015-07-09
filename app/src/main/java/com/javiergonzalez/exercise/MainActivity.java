package com.javiergonzalez.exercise;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.javiergonzalez.exercise.utils.SimpleFileDialog;
import com.javiergonzalez.exercise.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener, FoldersFilesListener {


    ScrollView mScrollView;
    List<String> mList;
    ArrayAdapter<String> mAdapter;

    ListView mListView;
    TextView mOriginTextView;
    TextView mDestinationTextView;

    String mOriginPath;
    String mDestinationPath;

    final static int TYPE_DESTINO = 2;
    final static int TYPE_ORIGIN = 1;

    public FoldersFilesListener mFolderFileListener;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

        Button origitnButton = (Button) findViewById(R.id.button_origin);
        origitnButton.setOnClickListener(this);
        mOriginTextView = (TextView) findViewById(R.id.textview_origin);

        Button destinationButton = (Button) findViewById(R.id.button_destination);
        destinationButton.setOnClickListener(this);
        mDestinationTextView = (TextView) findViewById(R.id.textview_destination);

        Button copy = (Button) findViewById(R.id.button_copy);
        copy.setOnClickListener(this);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
//        mListView = (ListView) findViewById(R.id.listview);
//
//        mList = new ArrayList<String>();

//        showList();
    }

    public void showList() {
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,mList);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        int type=0;
        switch (v.getId()) {
            case R.id.button_origin:
                choosen(TYPE_ORIGIN);
                break;
            case R.id.button_destination:
                choosen(TYPE_DESTINO);
                break;
            case R.id.button_copy:
                if (mOriginPath != null && mDestinationPath != null){

                    Utils.copyFileOrDirectory(this,mScrollView, mOriginPath, mDestinationPath);

                }else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
                }
        }
    }

    public void choosen(final int type) {
        //Create FileOpenDialog and register a callback
        SimpleFileDialog FolderChooseDialog =  new SimpleFileDialog(MainActivity.this, "FolderChoose",
                new SimpleFileDialog.SimpleFileDialogListener()
                {
                    @Override
                    public void onChosenDir(String chosenDir)
                    {
                        // The code in this function will be executed when the dialog OK button is pushed
                        Toast.makeText(MainActivity.this, "Chosen FileOpenDialog File: " +
                                chosenDir, Toast.LENGTH_LONG).show();
                        if (type == TYPE_DESTINO) {
                            mDestinationTextView.setText(chosenDir);
                            mDestinationPath = chosenDir;
                        } else {
                            mOriginTextView.setText(chosenDir);
                            mOriginPath = chosenDir;
                        }
                    }
                });

        FolderChooseDialog.chooseFile_or_Dir();


    }

    @Override
    public void paintFolderFile(String stringList) {
        mList.add(stringList);
        mAdapter.notifyDataSetChanged();
        showList();
    }
}
