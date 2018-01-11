package com.multipz.AdvoHub.Model;

/**
 * Created by Admin on 30-11-2017.
 */

public class Model {

    String img, text;

    public Model(String img, String text) {
        this.img = img;
        this.text = text;

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
