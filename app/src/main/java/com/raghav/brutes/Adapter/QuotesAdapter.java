package com.raghav.brutes.Adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.nambimobile.widgets.efab.FabOption;
import com.raghav.brutes.AboutActivity;
import com.raghav.brutes.BuildConfig;
import com.raghav.brutes.FavouriteActivity;
import com.raghav.brutes.Favourites.FavoriteDatabase;
import com.raghav.brutes.Favourites.FavoriteList;
import com.raghav.brutes.ImageDownloadActivity;
import com.raghav.brutes.MainActivity;
import com.raghav.brutes.Model.Quotes;
import com.raghav.brutes.R;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class QuotesAdapter extends PagerAdapter
{
      Context context;
      List<Quotes> quotesList;
      LayoutInflater inflater;
      Bitmap bitmap;
      LikeButton favBtn;
      int quoteid;
     public static FavoriteDatabase favoriteDatabase;


    //


    public QuotesAdapter(Context context, List<Quotes> quotesList) {
        this.context = context;
        this.quotesList = quotesList;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return quotesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        //inflate view

        View view = inflater.inflate(R.layout.viewpaget_litem,container,false);

        // view
        final ImageView image = view.findViewById(R.id.q_image);
        final TextView textView = view.findViewById(R.id.q_text);
        ImageView copy_btn = view.findViewById(R.id.copy_btn);
        final ImageView share_btn = view.findViewById(R.id.share_btn);
        FabOption wallpaper = view.findViewById(R.id.wallpaper_btn);
        FabOption fav_btn = view.findViewById(R.id.fav_op_btn);
        FabOption about_btn = view.findViewById(R.id.aboout_btn);
        final LikeButton favourite = view.findViewById(R.id.star_button);
        TextView q_id = view.findViewById(R.id.vp_id);
        TextView total = view.findViewById(R.id.vp_total);


        about_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AboutActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        ///////////////////////////////////////fav//////////////////////////////

           final Quotes quotes =quotesList.get(position);
      //  favoriteDatabase= Room.databaseBuilder(context.getApplicationContext(),FavoriteDatabase.class,"myfavdb").allowMainThreadQueries().build();
        if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(quotes.getId()) == 1)
            favourite.setLiked(true);
        else
            favourite.setLiked(false);


        favourite.setOnLikeListener(new OnLikeListener()
        {
            @Override
            public void liked(LikeButton likeButton) {
                FavoriteList favoriteList = new FavoriteList();

                int id = quotes.getId();
                String des = quotes.getDes();
                String url = quotes.getUrl();
                favoriteList.setId(id);
                favoriteList.setDes(des);
                favoriteList.setUrl(url);

                // favoriteList.setId(quotesList.get(position).getId());
//                favoriteList.setDes(quotesList.get(position).getDes());
//                favoriteList.setUrl(quotesList.get(position).getUrl());
                Toast.makeText(context, "Quotes added to favourites", Toast.LENGTH_SHORT).show();
                if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(id) != 1) {
                    favourite.setLiked(true);
                   MainActivity. favoriteDatabase.favoriteDao().addData(favoriteList);

                } else {
                    favourite.setLiked(false);
                   MainActivity. favoriteDatabase.favoriteDao().delete(favoriteList);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                FavoriteList favoriteList = new FavoriteList();


                int id = quotes.getId();
                String des = quotes.getDes();
                String url = quotes.getUrl();
                favoriteList.setId(id);
                favoriteList.setDes(des);
                favoriteList.setUrl(url);


//                int id = quotesList.get(position).getId();
//                favoriteList.setId(id);
//                favoriteList.setDes(quotesList.get(position).getDes());
//                favoriteList.setUrl(quotesList.get(position).getUrl());
                Toast.makeText(context, "Quotes removed from favourites", Toast.LENGTH_SHORT).show();
                if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(id) != 1) {
                    favourite.setLiked(true);
                    MainActivity.favoriteDatabase.favoriteDao().addData(favoriteList);

                } else {
                    favourite.setLiked(false);
                    MainActivity.favoriteDatabase.favoriteDao().delete(favoriteList);
                }
            }
        });

////////////////////////////////////////favend////////////////////////////////////////////////





        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),FavouriteActivity.class);
                v.getContext().startActivity(intent);
            }
        });





//        bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                setImageWallpaper();

                Intent intent = new Intent(v.getContext(),ImageDownloadActivity.class);
                intent.putExtra("img",quotesList.get(position).getUrl());
                v.getContext().startActivity(intent);

            }
        });


        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", quotesList.get(position).getDes());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Quotes copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();

            }

            private void popup()
            {
                PopupMenu popup = new PopupMenu(context, share_btn);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("SetWorldReadable")
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.sub_text:
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, quotesList.get(position).getDes() );
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Bruce Lee");
                                context.startActivity(Intent.createChooser(shareIntent, "Share Quote"));
                                Toast.makeText(context, "Share as Text", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.sub_image:


                                Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(),
                                        Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(bitmap);
                                image.draw(canvas);
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("*/*");
                                intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+context.getPackageName());
                                context.startActivity(Intent.createChooser(intent, "Premium Quotes"));

                                Toast.makeText(context, "Share as Image", Toast.LENGTH_SHORT).show();


                                return true;
                        }
                        return false;
                    }
                });
                popup.inflate(R.menu.menu_item);

                popup.show();
            }


        });




        //set data
        Glide.with(this.context).load(quotesList.get(position).getUrl()).into(image);
        textView.setText(quotesList.get(position).getDes());
        q_id.setText(Integer.toString(quotesList.get(position).getId()));
        total.setText(Integer.toString( quotesList.size()));


        container.addView(view);
        return  view;

    }




    private Uri getLocalBitmapUri(Bitmap bitmap)
    {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "Premium Quotes" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
