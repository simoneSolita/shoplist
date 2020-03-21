package com.academy.shoplist.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class Utility {

    public static String createIdProdotto (){
        return UUID.randomUUID().toString()+System.currentTimeMillis();
    }

    public static String createIdImmagine (){
        return UUID.randomUUID().toString()+System.currentTimeMillis();
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
