package com.raghav.brutes.Model;

public class Quotes
{
    String des,url;
    int id;


    public Quotes() {
    }

    public Quotes(String des, String url, int id) {
        this.des = des;
        this.url = url;
        this.id = id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
