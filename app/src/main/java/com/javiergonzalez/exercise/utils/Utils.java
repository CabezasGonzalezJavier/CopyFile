package com.javiergonzalez.exercise.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.javiergonzalez.exercise.R;
import com.javiergonzalez.exercise.interfaces.FoldersFilesListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by javiergonzalezcabezas on 9/7/15.
 */
public class Utils {


    static FoldersFilesListener sListener;

    public static void copyFileOrDirectory(FoldersFilesListener listener, final String srcDir, final String dstDir) {

        sListener = listener;

        new Thread(new Runnable() {
            public void run() {
                copy(srcDir, dstDir);
            }

            /**
             * copied directory from origin to destination
             * @param srcDir
             * @param dstDir
             */
            private void copy(String srcDir, String dstDir) {
                try {

                    File src = new File(srcDir);
                    File dst = new File(dstDir, src.getName());

                    //Showed directory in Progress
                    sListener.paintFolderFile(srcDir);

                    //Check is a directory or file
                    if (src.isDirectory()) {

                        String files[] = src.list();
                        int filesLength = files.length;
                        for (int i = 0; i < filesLength; i++) {
                            String src1 = (new File(src, files[i]).getPath());
                            String dst1 = dst.getPath();
                            copy(src1, dst1);

                        }
                    } else {
                        copyFile(src, dst);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * Copied file in directory from origin to destination
             * @param sourceFile
             * @param destFile
             * @throws IOException
             */
            public void copyFile(File sourceFile, File destFile) throws IOException {
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

                    // Showed File in progress
                    sListener.paintFolderFile(sourceFile.toString());
                } finally {
                    if (source != null) {
                        source.close();
                    }
                    if (destination != null) {
                        destination.close();
                    }
                }
            }
        }).start();
    }


}