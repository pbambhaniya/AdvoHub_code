package com.multipz.AdvoHub.Model;

/**
 * Created by Admin on 15-12-2017.
 */

public class DiaryModel {

    public DiaryModel() {

    }

    public String getAh_advocate_dairy_id() {
        return ah_advocate_dairy_id;
    }

    public void setAh_advocate_dairy_id(String ah_advocate_dairy_id) {
        this.ah_advocate_dairy_id = ah_advocate_dairy_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAh_users_id() {
        return ah_users_id;
    }

    public void setAh_users_id(String ah_users_id) {
        this.ah_users_id = ah_users_id;
    }

    String ah_advocate_dairy_id, name, description, date, time, create_date, update_date, status, ah_users_id;

}
