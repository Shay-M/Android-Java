package com.pinky.sharerecipebook.models;/* Created by Shay Mualem 31/08/2021 */

import android.icu.text.SimpleDateFormat;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    private String txt;
    private String userId;
    private String postDate;

    public Comment(String txt, String userId) {
        this.txt = txt;
        this.userId = userId; // yyMMdd_HHmm >> convert yy.MM.dd
        String timeStamp = new SimpleDateFormat("dd.MM.yy.HH:mm:ss").format(new Date());
        this.postDate = timeStamp;
    }

    public Comment() {
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }


}
