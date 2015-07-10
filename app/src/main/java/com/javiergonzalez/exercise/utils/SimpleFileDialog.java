package com.javiergonzalez.exercise.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by javiergonzalezcabezas on 9/7/15.
 */
public class SimpleFileDialog {

    private int FileOpen = 0;
    private int FileSave = 1;
    private int FolderChoose = 2;
    private int SelectType = FileSave;
    private String mSdcardDirectory = "";
    private Context mContext;
    private TextView mTitleView1;
    private TextView mTitleView;
    public String DefaultFileName = "default.txt";
    private String SelectedFileName = DefaultFileName;
    private EditText input_text;

    private String mDir = "";
    private List<String> mSubdirs = null;
    private SimpleFileDialogListener mSimpleFileDialogListener = null;
    private ArrayAdapter<String> mListAdapter = null;


    /**
     * Callback interface for selected directory
     */
    public interface SimpleFileDialogListener {
        public void onChosenDir(String chosenDir);
    }

    public SimpleFileDialog(Context context, String file_select_type, SimpleFileDialogListener SimpleFileDialogListener) {
        if (file_select_type.equals("FileOpen")) SelectType = FileOpen;
        else if (file_select_type.equals("FileSave")) SelectType = FileSave;
        else if (file_select_type.equals("FolderChoose")) SelectType = FolderChoose;
        else SelectType = FileOpen;

        mContext = context;
        mSdcardDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        mSimpleFileDialogListener = SimpleFileDialogListener;

        try {
            mSdcardDirectory = new File(mSdcardDirectory).getCanonicalPath();
        } catch (IOException ioe) {
        }
    }

    /**
     * Loaded directory chooser dialog for initial,default sdcard directory
     */
    public void chooseFile_or_Dir() {
        // Initial directory is sdcard directory
        if (mDir.equals("")) chooseFileOrDir(mSdcardDirectory);
        else chooseFileOrDir(mDir);
    }


    /**
     * Loaded directory chooser dialog for initial
     * @param dir
     */
    public void chooseFileOrDir(String dir) {
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            dir = mSdcardDirectory;
        }

        try {
            dir = new File(dir).getCanonicalPath();
        } catch (IOException ioe) {
            return;
        }

        mDir = dir;
        mSubdirs = getDirectories(dir);

        class SimpleFileDialogOnClickListener implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int item) {
                String m_dir_old = mDir;
                String sel = "" + ((AlertDialog) dialog).getListView().getAdapter().getItem(item);
                if (sel.charAt(sel.length() - 1) == '/') sel = sel.substring(0, sel.length() - 1);

                // Navigate into the sub-directory
                if (sel.equals("..")) {
                    mDir = mDir.substring(0, mDir.lastIndexOf("/"));
                } else {
                    mDir += "/" + sel;
                }
                SelectedFileName = DefaultFileName;

                if ((new File(mDir).isFile())) // If the selection is a regular file
                {
                    mDir = m_dir_old;
                    SelectedFileName = sel;
                }

                updateDirectory();
            }
        }

        AlertDialog.Builder dialogBuilder = createDirectoryChooserDialog(dir, mSubdirs,
                new SimpleFileDialogOnClickListener());

        dialogBuilder.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Current directory chosen
                // Call registered listener supplied with the chosen directory
                if (mSimpleFileDialogListener != null) {
                    {
                        if (SelectType == FileOpen || SelectType == FileSave) {
                            SelectedFileName = input_text.getText() + "";
                            mSimpleFileDialogListener.onChosenDir(mDir + "/" + SelectedFileName);
                        } else {
                            mSimpleFileDialogListener.onChosenDir(mDir);
                        }
                    }
                }
            }
        }).setNegativeButton("Cancel", null);

        final AlertDialog dirsDialog = dialogBuilder.create();

        // Show directory chooser dialog
        dirsDialog.show();
    }

    private boolean createSubDir(String newDir) {
        File newDirFile = new File(newDir);
        if (!newDirFile.exists()) return newDirFile.mkdir();
        else return false;
    }

    /**
     * Gotten directories
     * @param dir
     * @return
     */
    private List<String> getDirectories(String dir) {
        List<String> dirs = new ArrayList<String>();
        try {
            File dirFile = new File(dir);

            // if directory is not the base sd card directory add ".." for going up one directory
            if (!mDir.equals(mSdcardDirectory)) dirs.add("..");

            if (!dirFile.exists() || !dirFile.isDirectory()) {
                return dirs;
            }

            for (File file : dirFile.listFiles()) {
                if (file.isDirectory()) {
                    // Add "/" to directory names to identify them in the list
                    dirs.add(file.getName() + "/");
                } else if (SelectType == FileSave || SelectType == FileOpen) {
                    // Add file names to the list if we are doing a file save or file open operation
                    dirs.add(file.getName());
                }
            }
        } catch (Exception e) {
        }

        Collections.sort(dirs, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return dirs;
    }


    /**
     * Started dialog defination
     * @param title
     * @param listItems
     * @param onClickListener
     * @return
     */
    private AlertDialog.Builder createDirectoryChooserDialog(String title, List<String> listItems,
                                                             DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        // Create title text showing file select type
        mTitleView1 = new TextView(mContext);
        mTitleView1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        if (SelectType == FileOpen) mTitleView1.setText("Open:");
        if (SelectType == FileSave) mTitleView1.setText("Save As:");
        if (SelectType == FolderChoose) mTitleView1.setText("Folder Select:");

        //need to make this a variable Save as, Open, Select Directory
        mTitleView1.setGravity(Gravity.CENTER_VERTICAL);
        mTitleView1.setBackgroundColor(-12303292); // dark gray 	-12303292
        mTitleView1.setTextColor(mContext.getResources().getColor(android.R.color.white));

        // Create custom view for AlertDialog title
        LinearLayout titleLayout1 = new LinearLayout(mContext);
        titleLayout1.setOrientation(LinearLayout.VERTICAL);
        titleLayout1.addView(mTitleView1);


        if (SelectType == FolderChoose || SelectType == FileSave) {
            // Create New Folder Button
            Button newDirButton = new Button(mContext);
            newDirButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            newDirButton.setText("New Folder");
            newDirButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    final EditText input = new EditText(mContext);

                                                    // Show new folder name input dialog
                                                    new AlertDialog.Builder(mContext).
                                                            setTitle("New Folder Name").
                                                            setView(input).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            Editable newDir = input.getText();
                                                            String newDirName = newDir.toString();
                                                            // Create new directory
                                                            if (createSubDir(mDir + "/" + newDirName)) {
                                                                // Navigate into the new directory
                                                                mDir += "/" + newDirName;
                                                                updateDirectory();
                                                            } else {
                                                                Toast.makeText(mContext, "Failed to create '"
                                                                        + newDirName + "' folder", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }).setNegativeButton("Cancel", null).show();
                                                }
                                            }
            );
            titleLayout1.addView(newDirButton);
        }

        // Create View with folder path and entry text box
        LinearLayout titleLayout = new LinearLayout(mContext);
        titleLayout.setOrientation(LinearLayout.VERTICAL);

        mTitleView = new TextView(mContext);
        mTitleView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mTitleView.setBackgroundColor(-12303292); // dark gray -12303292
        mTitleView.setTextColor(mContext.getResources().getColor(android.R.color.white));
        mTitleView.setGravity(Gravity.CENTER_VERTICAL);
        mTitleView.setText(title);

        titleLayout.addView(mTitleView);

        if (SelectType == FileOpen || SelectType == FileSave) {
            input_text = new EditText(mContext);
            input_text.setText(DefaultFileName);
            titleLayout.addView(input_text);
        }
        // Set Views and Finish Dialog builder
        dialogBuilder.setView(titleLayout);
        dialogBuilder.setCustomTitle(titleLayout1);
        mListAdapter = createListAdapter(listItems);
        dialogBuilder.setSingleChoiceItems(mListAdapter, -1, onClickListener);
        dialogBuilder.setCancelable(false);
        return dialogBuilder;
    }

    private void updateDirectory() {
        mSubdirs.clear();
        mSubdirs.addAll(getDirectories(mDir));
        mTitleView.setText(mDir);
        mListAdapter.notifyDataSetChanged();
        //#scorch
        if (SelectType == FileSave || SelectType == FileOpen) {
            input_text.setText(SelectedFileName);
        }
    }

    /**
     * Created list adapter
     * @param items
     * @return
     */
    private ArrayAdapter<String> createListAdapter(List<String> items) {
        return new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_item, android.R.id.text1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (v instanceof TextView) {
                    // Enable list item (directory) text wrapping
                    TextView tv = (TextView) v;
                    tv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                    tv.setEllipsize(null);
                }
                return v;
            }
        };
    }
}
