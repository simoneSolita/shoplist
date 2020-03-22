package com.academy.shoplist.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;

import com.academy.shoplist.constants.IntentConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class Utility {

    public static String createIdProdotto (){
        return UUID.randomUUID().toString()+System.currentTimeMillis();
    }

    public static String createIdImmagine (){
        return UUID.randomUUID().toString()+System.currentTimeMillis();
    }


}
