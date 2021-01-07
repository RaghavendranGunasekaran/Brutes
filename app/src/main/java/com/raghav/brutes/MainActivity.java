package com.raghav.brutes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raghav.brutes.Adapter.QuotesAdapter;
import com.raghav.brutes.Favourites.FavoriteDatabase;
import com.raghav.brutes.Listener.FirebaseLoader;
import com.raghav.brutes.Model.Quotes;
import com.raghav.brutes.VPAnimation.DepthTransformation;
import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class MainActivity extends AppCompatActivity implements FirebaseLoader,ForceUpdateChecker.OnUpdateNeededListener
{

    private ViewPager viewPager;
    private QuotesAdapter adapter;
    private DatabaseReference reference;
    FirebaseLoader loader;
    private int ITEM_COUNT = 115;
    public static FavoriteDatabase favoriteDatabase;
    private AlertDialog alertDialog;
    private ACProgressFlower flower_dialog;
    private long backpressedtime;
    private Toast backtoast;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();

//        alertDialog=new SpotsDialog.Builder()
//                .setContext(MainActivity.this)
//                .setCancelable(false)
//                .setTheme(R.style.Custom)
//                .build();

         flower_dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading")
                .fadeColor(Color.DKGRAY).build();


        reference= FirebaseDatabase.getInstance().getReference("quotes");
        favoriteDatabase= Room.databaseBuilder(getApplicationContext(),FavoriteDatabase.class,"myfavdb").allowMainThreadQueries().build();


        //init event
        loader=this;
         


        viewPager = findViewById(R.id.vp);
        viewPager.setOffscreenPageLimit(ITEM_COUNT-1);
        DepthTransformation depthTransformation = new DepthTransformation();
        viewPager.setPageTransformer(true, depthTransformation);


    }

    @Override
    protected void onResume() {
        super.onResume();
        flower_dialog.show();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Quotes> quotesList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    quotesList.add(ds.getValue(Quotes.class));
                    loader.OnFirebaseLoadSuccess(quotesList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loader.OnFirebaseLoadFailed(error.getMessage());
            }
        });
    }

//    private void loadQuotes()
//    {
//
//    }

    @Override
    public void OnFirebaseLoadSuccess(List<Quotes> quotesList)
    {
        adapter = new QuotesAdapter(this,quotesList);
        viewPager.setAdapter(adapter);
        flower_dialog.dismiss();
    }

    @Override
    public void OnFirebaseLoadFailed(String message)
    {
        Toast.makeText(this, "Error"+message, Toast.LENGTH_SHORT).show();
        flower_dialog.dismiss();
    }


    @Override
    public void onUpdateNeeded(final String updateUrl)
    {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                             redirectstore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //finish();
                            }
                        }).create();
        dialog.show();
        dialog.setCancelable(false);

    }

    private void redirectstore(String updateUrl)
    {

        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    @Override
    public void onBackPressed()
    {

        if (backpressedtime +2000 > System.currentTimeMillis())
        {
            backtoast.cancel();
            super.onBackPressed();
            return;
        }else
        {


            backtoast= Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            backtoast.show();
        }

        backpressedtime=System.currentTimeMillis();
    }
}