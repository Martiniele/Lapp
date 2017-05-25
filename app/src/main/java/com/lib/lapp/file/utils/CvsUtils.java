package com.lib.lapp.file.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wxx on 2017/4/6.
 */

public class CvsUtils {
    public static final String mComma = ",";
    private static StringBuilder mStringBuilder = null;
    private static String mFileName = null;

    public static void open() {
        String folderName = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (path != null) {
                folderName = path + "/WIFIDATACSV/";
            }
        }

        File fileRobo = new File(folderName);
        if (!fileRobo.exists()) {
            fileRobo.mkdir();
        }
        mFileName = folderName + "log.csv";
        mStringBuilder = new StringBuilder();
        mStringBuilder.append("column1");
        mStringBuilder.append(mComma);
        mStringBuilder.append("column2");
        mStringBuilder.append(mComma);
        mStringBuilder.append("column3");
        mStringBuilder.append("\n");
    }

    public static void writeCsv(String value1, String value2, String value3) {
        mStringBuilder.append(value1);
        mStringBuilder.append(mComma);
        mStringBuilder.append(value2);
        mStringBuilder.append(mComma);
        mStringBuilder.append(value3);
        mStringBuilder.append("\n");
    }

    public static void flush() {
        if (mFileName != null) {
            try {
                File file = new File(mFileName);
                FileOutputStream fos = new FileOutputStream(file, false);
                fos.write(mStringBuilder.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("You should call open() before flush()");
        }
    }
}
