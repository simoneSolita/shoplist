package com.academy.shoplist.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class AllegatiUtils {
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) throws Exception {
        /*ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();*/

        int size = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();
    }

    public static String getLocalLowQName(String originalName, boolean withExtenction) {
        String result = "";
        if (originalName.lastIndexOf('.') > 0)
            result = originalName.substring(0, originalName.lastIndexOf(".")) + "_lowQ";
        else
            result = originalName + "_lowQ";

        if (withExtenction)
            result += ".jpg";

        return result;
    }

    public static File createImageFile(String idImmagine) throws IOException {
        // Create an image file name
        String imageFileName = idImmagine + "_" + (System.currentTimeMillis() / 1000);

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    public static Bitmap decodeSampledBitmapFromResource(String image,
                                                         int maxSize) {

        int width;
        int height;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image, options);

        float bitmapRatio = (float) options.outWidth / (float) options.outHeight;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap imageBitmap = BitmapFactory.decodeFile(image, options);

        //determino l'orientamento della foto
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);


        return rotateBitmap(imageBitmap, orientation);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String savePicture(Bitmap bm, String imgName) {
        OutputStream fOut = null;
        String strDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        String encoded = "";
        File file = new File(strDirectory, imgName + ".jpg");
        try {
            fOut = new FileOutputStream(file);

            int MAX_IMAGE_SIZE = 2000 * 1024;
            int streamLength = MAX_IMAGE_SIZE;
            int compressQuality = 105;
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            while (streamLength >= MAX_IMAGE_SIZE && compressQuality > 5) {
                try {
                    bmpStream.flush();//to avoid out of memory error
                    bmpStream.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                compressQuality -= 5;
                bm.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
                byte[] bmpPicByteArray = bmpStream.toByteArray();
                encoded = Base64.encodeToString(bmpPicByteArray, Base64.DEFAULT);
                streamLength = encoded.getBytes().length;
            }
            bm.compress(Bitmap.CompressFormat.JPEG, compressQuality, fOut);
            fOut.flush();
            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return encoded;
    }

    public static Bitmap getBitmapByBase64 (String base64){
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
