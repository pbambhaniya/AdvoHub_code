package com.multipz.AdvoHub.Model;

/**
 * Created by Admin on 23-12-2017.
 */

public class CasesModel {
    String ah_lawyer_type_id, title, full_name, email, description, create_date, status,ah_users_id,profile_img;
    int ah_lawyer_specialist_id, idstatus,ah_case_lawyer_id,ah_case_post_user_id;

    public int getAh_case_lawyer_id() {
        return ah_case_lawyer_id;
    }

    public void setAh_case_lawyer_id(int ah_case_lawyer_id) {
        this.ah_case_lawyer_id = ah_case_lawyer_id;
    }

    public int getAh_case_post_user_id() {
        return ah_case_post_user_id;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getAh_users_id() {
        return ah_users_id;
    }

    public void setAh_users_id(String ah_users_id) {
        this.ah_users_id = ah_users_id;
    }

    public void setAh_case_post_user_id(int ah_case_post_user_id) {
        this.ah_case_post_user_id = ah_case_post_user_id;
    }

    public String getAh_lawyer_type_id() {
        return ah_lawyer_type_id;
    }

    public void setAh_lawyer_type_id(String ah_lawyer_type_id) {
        this.ah_lawyer_type_id = ah_lawyer_type_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAh_lawyer_specialist_id() {
        return ah_lawyer_specialist_id;
    }

    public void setAh_lawyer_specialist_id(int ah_lawyer_specialist_id) {
        this.ah_lawyer_specialist_id = ah_lawyer_specialist_id;
    }

    public int getIdstatus() {
        return idstatus;
    }

    public void setIdstatus(int idstatus) {
        this.idstatus = idstatus;
    }
}
