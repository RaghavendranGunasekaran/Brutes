package com.raghav.brutes.Favourites;

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
import com.raghav.brutes.MainActivity;
import com.raghav.brutes.Model.Quotes;
import com.raghav.brutes.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FavouriteAdapter extends PagerAdapter
{

    Context context;
    List<FavoriteList> favoriteLists;
    List<Quotes> quotes;
    LayoutInflater inflater;
    int quoteid;
    public static FavoriteDatabase favoriteDatabase;



    public FavouriteAdapter(Context context, List<FavoriteList> favoriteLists) {
        this.context = context;
        this.favoriteLists = favoriteLists;
        inflater= LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return favoriteLists.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        //inflate view
        View view = inflater.inflate(R.layout.favourite_list_item,container,false);

        // view
        final ImageView image = view.findViewById(R.id.f_q_image);
        final TextView textView = view.findViewById(R.id.f_q_text);
        ImageView copy_btn = view.findViewById(R.id.f_copy_btn);
        final ImageView share_btn = view.findViewById(R.id.f_share_btn);
        FabOption wallpaper = view.findViewById(R.id.f_wallpaper_btn);
        ImageView back = view.findViewById(R.id.back);
        FabOption about = view.findViewById(R.id.f_about_btn);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AboutActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);

            }
        });

        final LikeButton favourite = view.findViewById(R.id.f_star_button);

       final FavoriteList fl =favoriteLists.get(position);


       // favoriteDatabase= Room.databaseBuilder(context.getApplicationContext(),FavoriteDatabase.class,"myfavdb").allowMainThreadQueries().build();
        if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(fl.getId()) == 1)
            favourite.setLiked(true);
        else
            favourite.setLiked(false);


        favourite.setOnLikeListener(new OnLikeListener()
        {
            @Override
            public void liked(LikeButton likeButton) {
                FavoriteList favoriteList = new FavoriteList();

                   int id = fl.getId();
                   String des = fl.getDes();
                   String url = fl.getUrl();
                   favoriteList.setId(id);
                   favoriteList.setDes(des);
                   favoriteList.setUrl(url);





//                favoriteList.setId(favoriteLists.get(position).getId());
//                favoriteList.setDes(favoriteLists.get(position).getDes());
//                favoriteList.setUrl(favoriteLists.get(position).getUrl());
                Toast.makeText(context, "Quotes added to favourites", Toast.LENGTH_SHORT).show();
                if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(id) != 1) {
                    favourite.setLiked(true);
                    MainActivity.favoriteDatabase.favoriteDao().addData(favoriteList);

                } else {
                    favourite.setLiked(false);
                    MainActivity.favoriteDatabase.favoriteDao().delete(favoriteList);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {

                try{

                    FavoriteList favoriteList = new FavoriteList();
                    int id = fl.getId();
                    String des = fl.getDes();
                    String url = fl.getUrl();
                    favoriteList.setId(id);
                    favoriteList.setDes(des);
                    favoriteList.setUrl(url);
                    Toast.makeText(context, "Quotes removed from favourites", Toast.LENGTH_SHORT).show();
                    if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(id) != 1) {
                        favourite.setLiked(true);
                        MainActivity.favoriteDatabase.favoriteDao().addData(favoriteList);

                    } else {
                        favourite.setLiked(false);
                        MainActivity.favoriteDatabase.favoriteDao().delete(favoriteList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error"+e, Toast.LENGTH_SHORT).show();
                }

            }
        });

//        fav_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), FavouriteActivity.class);
//                v.getContext().startActivity(intent);
//            }
//        });


        wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                setImageWallpaper();

                Intent intent = new Intent(v.getContext(), FavouriteImageDownloadActivity.class);
                intent.putExtra("img",favoriteLists.get(position).getUrl());
                v.getContext().startActivity(intent);

            }
        });


        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", favoriteLists.get(position).getDes());
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
                                shareIntent.putExtra(Intent.EXTRA_TEXT, favoriteLists.get(position).getDes() );
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Premium Quotes");
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
        Glide.with(this.context).load(favoriteLists.get(position).getUrl()).into(image);
        textView.setText(favoriteLists.get(position).getDes());

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
