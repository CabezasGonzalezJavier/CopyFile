package com.javiergonzalez.exercise;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.javiergonzalez.exercise.adapters.ListAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {
    ListView mListView;
    TextView mOriginTextView;
    TextView mDestinationTextView;

    String mOriginPath;
    String mDestinationPath;

    final static int TYPE_DESTINO = 2;
    final static int TYPE_ORIGIN = 1;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
//        mProgressDialog.setMessage(getString(R.string.activity_detail_sending));

        Button origitnButton = (Button) findViewById(R.id.button_origin);
        origitnButton.setOnClickListener(this);
        mOriginTextView = (TextView) findViewById(R.id.textview_origin);

        Button destinationButton = (Button) findViewById(R.id.button_destination);
        destinationButton.setOnClickListener(this);
        mDestinationTextView = (TextView) findViewById(R.id.textview_destination);

        Button copy = (Button) findViewById(R.id.button_copy);
        copy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int type=0;
        switch (v.getId()) {
            case R.id.button_origin:
                mOriginPath = choosen(TYPE_ORIGIN);
                break;
            case R.id.button_destination:
                mDestinationPath = choosen(TYPE_DESTINO);
                break;
            case R.id.button_copy:
                if (mOriginPath != null && mDestinationPath != null){

                    copyFileOrDirectory(mOriginPath,mDestinationPath);


//                    File fileOrigin = new File(getFilesDir(), mOriginPath);
//                    File fileDestine = new File(getFilesDir(), mDestinationPath);
//                    try {
//                        copy(fileOrigin, fileDestine);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
                }
        }
    }

    public String choosen(final int type) {
        final String[] result = {new String()};
        //Create FileOpenDialog and register a callback
        SimpleFileDialog FolderChooseDialog =  new SimpleFileDialog(MainActivity.this, "FolderChoose",
                new SimpleFileDialog.SimpleFileDialogListener()
                {
                    @Override
                    public void onChosenDir(String chosenDir)
                    {
                        // The code in this function will be executed when the dialog OK button is pushed
                        result[0] = chosenDir;
                        Toast.makeText(MainActivity.this, "Chosen FileOpenDialog File: " +
                                result[0], Toast.LENGTH_LONG).show();
                        if (type == TYPE_DESTINO) {
                            mDestinationTextView.setText(result[0]);
                            mDestinationPath = result [0];
                        } else {
                            mOriginTextView.setText(result[0]);
                            mOriginPath = result [0];
                        }
                    }
                });

        FolderChooseDialog.chooseFile_or_Dir();




        return result[0];

    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        mProgressDialog.show();

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
        mProgressDialog.cancel();
    }


    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

}
