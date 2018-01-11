package com.multipz.AdvoHub.Model;

/**
 * Created by Admin on 04-12-2017.
 */

public class ReportModel {

    String img, name, time, type;

    public ReportModel(String img, String name, String time, String type) {
        this.img = img;
        this.name = name;
        this.time = time;
        this.type = type;

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
