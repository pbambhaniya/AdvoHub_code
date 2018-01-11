package com.multipz.AdvoHub.Model;

/**
 * Created by Admin on 20-12-2017.
 */

public class AskQyeModel {
    String ah_ask_question_id,ah_ask_question_user_id,title,description,attachment,answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAh_ask_question_id() {
        return ah_ask_question_id;
    }

    public void setAh_ask_question_id(String ah_ask_question_id) {
        this.ah_ask_question_id = ah_ask_question_id;
    }

    public String getAh_ask_question_user_id() {
        return ah_ask_question_user_id;
    }

    public void setAh_ask_question_user_id(String ah_ask_question_user_id) {
        this.ah_ask_question_user_id = ah_ask_question_user_id;
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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
