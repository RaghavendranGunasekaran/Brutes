package com.raghav.brutes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.raghav.brutes.Model.Play;
import com.raghav.brutes.Model.Quotes;

import java.util.List;

public class AboutActivity extends AppCompatActivity {

    private AdView mAdView;
    private ImageView insta,github,web,back,share;
    private TextView pp;
    private LinearLayout layout;
    List<Play> playstore_link;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //ca-app-pub-7839522611253816~5263267521
        //ca-app-pub-7839522611253816/7251136611

        insta = findViewById(R.id.insta);
        github = findViewById(R.id.github);
        web = findViewById(R.id.web);
        back = findViewById(R.id.a_back_btn);
        share = findViewById(R.id.a_share);
        pp = findViewById(R.id.pp);
        layout=findViewById(R.id.lin_lay);

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                //pass the url to intent data
                intent.setData(Uri.parse("https://github.com/RaghavendranGunasekaran/BruceLee-Privacy-Policy/blob/main/Bruce%20Lee%20-%20Privacy%20Policy.txt"));
                startActivity(intent);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download this app :https://play.google.com/store/apps/details?id=exarcplus.com.jayanagarajaguars" );
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Bruce Lee");
                startActivity(Intent.createChooser(shareIntent, "Share Quote"));
                Toast.makeText(AboutActivity.this, "Share app link", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                //pass the url to intent data
                intent.setData(Uri.parse("https://www.instagram.com/d.r.o.i.d.x/"));
                startActivity(intent);

            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                //pass the url to intent data
                intent.setData(Uri.parse("https://github.com/RaghavendranGunasekaran"));
                startActivity(intent);
            }
        });

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                //pass the url to intent data
                intent.setData(Uri.parse("https://raghavendran.com/"));
                startActivity(intent);
            }
        });


        mAdView = (AdView) findViewById(R.id.adView);
        //mAdView.setAdUnitId(getString(R.string.about_footer));

        //MobileAds.initialize(this, String.valueOf(R.string.admob_app_id));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus)
            {

            }
        });
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });


        mAdView.loadAd(adRequest);


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



    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}