package com.raghav.brutes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;

public class SplashScreenActivity extends AppCompatActivity
{

    private boolean splash_active=true,paused=false;
    private long ms=0,splashtime=900;
    private ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash_screen);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setstatusbarcolor(R.color.colorPrimary);
        layout=findViewById(R.id.con_ly);

        Thread thread=new Thread()
        {
            public void run()
            {
                try
                {
                    while (splash_active && ms<splashtime)
                    {
                        if(!paused)
                            ms=ms+100;
                        sleep(500);

                    } } catch (Exception e)
                {
                    e.printStackTrace();
                }finally
                {
                    if (!isOnline())
                    {
                        Snackbar snackbar=Snackbar.make(layout,"No Internet Access",Snackbar.LENGTH_INDEFINITE)
                                .setAction("retry", new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        recreate();
                                    }
                                });snackbar.show();

                    }else {
                        gomain();
                    }
                }
            }
        };
        thread.start();

    }

    private void gomain()
    {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private boolean isOnline()
    {
        ConnectivityManager connection = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        return connection.getActiveNetworkInfo() != null && connection.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void setstatusbarcolor(int colorPrimary)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            int color= ContextCompat.getColor(this,colorPrimary);
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }


    }
}