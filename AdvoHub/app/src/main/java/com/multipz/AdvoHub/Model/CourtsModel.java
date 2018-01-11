package com.multipz.AdvoHub.Model;

/**
 * Created by Admin on 18-12-2017.
 */

public class CourtsModel {
    private String ah_court_id, ah_city_id, court_name;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAh_court_id() {
        return ah_court_id;
    }

    public void setAh_court_id(String ah_court_id) {
        this.ah_court_id = ah_court_id;
    }

    public String getAh_city_id() {
        return ah_city_id;
    }

    public void setAh_city_id(String ah_city_id) {
        this.ah_city_id = ah_city_id;
    }

    public String getCourt_name() {
        return court_name;
    }

    public void setCourt_name(String court_name) {
        this.court_name = court_name;
    }
}
