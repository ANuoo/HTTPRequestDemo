package com.anuo.workeight.data;

public class Item {
    private String title;
    private String user;
    private String time;
    private String imageUrl;

    public Item(String title, String user, String time, String imageUrl) {
        this.title = title;
        this.user = user;
        this.time = time;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
