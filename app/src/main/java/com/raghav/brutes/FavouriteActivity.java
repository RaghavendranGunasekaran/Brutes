package com.raghav.brutes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raghav.brutes.Adapter.QuotesAdapter;
import com.raghav.brutes.Favourites.FavoriteList;
import com.raghav.brutes.Favourites.FavouriteAdapter;
import com.raghav.brutes.Listener.FirebaseLoader;
import com.raghav.brutes.VPAnimation.DepthTransformation;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity
{

    private ViewPager viewPager;
    private FavouriteAdapter adapter;
    private DatabaseReference reference;
    FirebaseLoader loader;
    private int ITEM_COUNT = 4;
    private ArrayList<FavoriteList> imageArry = new ArrayList<FavoriteList>();
    List<FavoriteList> favoriteLists = MainActivity.favoriteDatabase.favoriteDao().getFavoriteData();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        reference= FirebaseDatabase.getInstance().getReference("quotes");

        //init event




        viewPager = findViewById(R.id.f_vp);
        viewPager.setOffscreenPageLimit(ITEM_COUNT-1);
        DepthTransformation depthTransformation = new DepthTransformation();
        viewPager.setPageTransformer(true, depthTransformation);
        getFavData();


        if (savedInstanceState == null) {
            viewPager.setVisibility(View.INVISIBLE);

            final ViewTreeObserver viewTreeObserver = viewPager.getViewTreeObserver();

            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            circularRevealActivity();
                        }
                        viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                });
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circularRevealActivity()
    {
        int cx = viewPager.getLeft() - getDips(44);
        int cy = viewPager.getBottom() - getDips(44);

        float finalRadius = Math.max(viewPager.getWidth(), viewPager.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                viewPager,
                cx,
                cy,
                0,
                finalRadius);

        circularReveal.setDuration(1500);
        viewPager.setVisibility(View.VISIBLE);
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

    private void getFavData()
    {

        for (FavoriteList cn : favoriteLists) {

            imageArry.add(cn);
        }

        if (imageArry.isEmpty())
        {

           // NoQuotes.setVisibility(View.VISIBLE);
            new AestheticDialog.Builder(this, DialogStyle.FLAT, DialogType.ERROR)
                    .setTitle("Your favourite list is empty")
                    .setMessage("Explore more and show some love!")
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
        }

        adapter=new FavouriteAdapter(this,favoriteLists);
        viewPager.setAdapter(adapter);

    }



}