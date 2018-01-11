package com.multipz.AdvoHub.Model;

/**
 * Created by Admin on 11-12-2017.
 */

public class CaseListModel {
    int caseid;
    String title, description, create_date, city_name, state_name, lawyer_type;


    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getLawyer_type() {
        return lawyer_type;
    }

    public void setLawyer_type(String lawyer_type) {
        this.lawyer_type = lawyer_type;
    }


    public int getCaseid() {
        return caseid;
    }

    public void setCaseid(int caseid) {
        this.caseid = caseid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
