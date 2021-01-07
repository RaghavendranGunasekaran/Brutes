package com.raghav.brutes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.raghav.brutes.Favourites.FavouriteImageDownloadActivity;
import com.raghav.brutes.Helper.BitMapHelper;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ImageDownloadActivity extends AppCompatActivity {


    ImageView imageView,back_btn;
    Button download_btn,wallpaper;
    Bitmap bitmap;
    private ConstraintLayout layout;
    private  static  final  int WRITE_EXTERNAL_STORAGE_CODE =1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        layout=findViewById(R.id.id_cons);

        imageView = findViewById(R.id.get_image);
        back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        Glide.with(this).load(intent.getStringExtra("img")).into(imageView);

        download_btn=findViewById(R.id.d_btn);

        wallpaper = findViewById(R.id.wallpaper);
        wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AestheticDialog.Builder(ImageDownloadActivity.this, DialogStyle.FLAT, DialogType.SUCCESS)
                        .setTitle("Image set a wallpaper")
                        .setMessage("Successfully !")
                        .setCancelable(false)
                        .setDarkMode(true)
                        .setGravity(Gravity.CENTER)
                        .setAnimation(DialogAnimation.SHRINK)
                        .setOnClickListener(new OnDialogClickListener() {
                            @Override
                            public void onClick(AestheticDialog.Builder builder) {
                                onBackPressed();
                            }
                        })

                        .show();
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(ImageDownloadActivity.this);
                //get full view display
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                //get width and height
                int w = displayMetrics.widthPixels;
                int h = displayMetrics.heightPixels;

                try {
                    if (bitmap != null){

                        wallpaperManager.setBitmap(bitmap);
                        wallpaperManager.suggestDesiredDimensions(w,h);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,WRITE_EXTERNAL_STORAGE_CODE);
                    }else {

                        saveImage();
                    }
                }


            }
        });

        if (savedInstanceState == null) {
            layout.setVisibility(View.INVISIBLE);

            final ViewTreeObserver viewTreeObserver = layout.getViewTreeObserver();

            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            circularRevealActivity();
                        }
                        layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                });
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circularRevealActivity()
    {
        int cx = layout.getLeft() - getDips(44);
        int cy = layout.getBottom() - getDips(44);

        float finalRadius = Math.max(layout.getWidth(), layout.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                layout,
                cx,
                cy,
                0,
                finalRadius);

        circularReveal.setDuration(1500);
        layout.setVisibility(View.VISIBLE);
        circularReveal.start();

    }

    private int getDips(int dps)
    {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics());
    }


    private void saveImage()
    {

        bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path+"/DCIM");
        dir.mkdirs();
        String imagename = time+".PNG";
        File file =new File(dir,imagename);
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
            outputStream.close();

            Toast.makeText(ImageDownloadActivity.this, "Image saved in DCIM", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ImageDownloadActivity.this, "Error"+e, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {switch (requestCode){

        case  WRITE_EXTERNAL_STORAGE_CODE:{

            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){


            }else {

                Toast.makeText(this, "Enable permission", Toast.LENGTH_SHORT).show();
            }
        }

    }


    }
}