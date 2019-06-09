package com.xx.medicalimg.util;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭连接工具类
 *
 */
public class CloseUtils {
    public static void close(Closeable closeable){
        if (closeable == null){
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("『Closeable』 close失败" + e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void close(AutoCloseable closeable){
        if (closeable == null){
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("『Closeable』 close失败" + e.getMessage());
        }
    }
}
