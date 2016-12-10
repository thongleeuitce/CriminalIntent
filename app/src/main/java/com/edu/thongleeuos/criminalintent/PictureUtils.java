package com.edu.thongleeuos.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Size;

/**
 * Created by thongleeuteam on 10/12/2016.
 */

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destHeight, int destWidth){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float srcHeight = options.outHeight;
        float srcWidth = options.outWidth;
        options.inSampleSize = 1;
        if(srcHeight > destHeight || srcWidth > destWidth){
            if(srcHeight > srcWidth)
                options.inSampleSize = Math.round(srcWidth/destWidth);
            else
                options.inSampleSize = Math.round(srcHeight/destHeight);
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.y, size.x);
    }
}
