package com.raghav.brutes.Helper;

import android.graphics.Bitmap;

public class BitMapHelper {

    private Bitmap bitmap = null;
    private  static  final BitMapHelper instance = new BitMapHelper();

    public BitMapHelper() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static BitMapHelper getInstance() {
        return instance;
    }
}
