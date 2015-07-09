package com.javiergonzalez.exercise.utils;

import android.content.Context;
import android.widget.ScrollView;
import android.widget.TextView;

import com.javiergonzalez.exercise.FoldersFilesListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by javiergonzalezcabezas on 9/7/15.
 */
public class Utils {


    public static void copyFileOrDirectory(Context context, ScrollView mScrollView,String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(context, mScrollView, src1, dst1);

                }
            } else {
                copyFile(context, mScrollView, src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(Context context, ScrollView scrollView, File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

//        mFolderFileListener.paintFolderFile(sourceFile.toString());


        TextView textView= new TextView(context);
        textView.setText(sourceFile.toString());
        scrollView.addView(textView);

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
