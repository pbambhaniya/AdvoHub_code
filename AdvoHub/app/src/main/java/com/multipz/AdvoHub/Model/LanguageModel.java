package com.multipz.AdvoHub.Model;

/**
 * Created by Admin on 18-12-2017.
 */

public class LanguageModel {
    boolean ischeck;


    String  ah_language_id, language_name;

    public String getAh_language_id() {
        return ah_language_id;
    }

    public void setAh_language_id(String ah_language_id) {
        this.ah_language_id = ah_language_id;
    }

    public String getLanguage_name() {
        return language_name;
    }

    public void setLanguage_name(String language_name) {
        this.language_name = language_name;
    }





    public boolean isIscheck() {
        return ischeck;
    }

    public boolean setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
        return ischeck;
    }
}
