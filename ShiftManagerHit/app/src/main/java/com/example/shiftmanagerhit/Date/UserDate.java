package com.example.shiftmanagerhit.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDate implements Parcelable {

    public static final Creator<UserDate> CREATOR = new Creator<UserDate>() {
        @Override
        public UserDate createFromParcel(Parcel in) {
            return new UserDate(in);
        }

        @Override
        public UserDate[] newArray(int size) {
            return new UserDate[size];
        }
    };

    private String name;
    private String id;
    private String shiftWants;
    private String shiftGet;
    private String email;
    private String password;
    private Boolean is_Manger;

    public UserDate() {

    }

    public UserDate(String name, String id, String shiftWants, String shiftGet, String email, String password, Boolean is_manger) {
        this.name = name;
        this.id = id;
        this.shiftWants = shiftWants;
        this.shiftGet = shiftGet;
        this.email = email;
        this.password = password;
        this.is_Manger = is_manger;
    }


    public UserDate(String name, String email, String password, Boolean is_manger) {
        this.name = name;
        this.id = "No_id";
        this.shiftWants = "";
        this.shiftGet = "";
        this.email = email;
        this.password = password;
        this.is_Manger = is_manger;
    }


    protected UserDate(Parcel in) {
        name = in.readString();
        id = in.readString();
        shiftWants = in.readString();
        shiftGet = in.readString();
        email = in.readString();
        password = in.readString();
        byte tmpIs_Manger = in.readByte();
        is_Manger = tmpIs_Manger == 0 ? null : tmpIs_Manger == 1;
    }

    public String getShiftGet() {
        return shiftGet;
    }

    public void setShiftGet(String shiftGet) {
        this.shiftGet = shiftGet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShiftWants() {
        return shiftWants;
    }

    public void setShiftWants(String shiftWants) {
        this.shiftWants = shiftWants;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIs_Manger() {
        return is_Manger;
    }

    public void setIs_Manger(Boolean is_Manger) {
        this.is_Manger = is_Manger;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(shiftWants);
        dest.writeString(shiftGet);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeByte((byte) (is_Manger == null ? 0 : is_Manger ? 1 : 2));
    }
}
