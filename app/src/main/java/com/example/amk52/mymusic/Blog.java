package com.example.amk52.mymusic;

public class Blog {
    private String desc, imageurl, title;

    public Blog(String desc, String imageurl, String title) {
        this.desc = desc;
        this.imageurl = imageurl;
        this.title = title;
    }

    public Blog() {

    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}