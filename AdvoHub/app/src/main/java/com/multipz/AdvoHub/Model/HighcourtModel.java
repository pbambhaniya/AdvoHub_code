package com.multipz.AdvoHub.Model;

/**
 * Created by Admin on 01-12-2017.
 */

public class HighcourtModel {

    String text, date, time, img;

    public HighcourtModel(String text, String date, String time, String img) {
        this.text = text;
        this.date = date;
        this.time = time;
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
