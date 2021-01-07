package com.raghav.brutes.Favourites;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.annotations.NotNull;

@Entity(tableName="favoritelist")
public class FavoriteList {

    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

  @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "des")
    public String des;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
