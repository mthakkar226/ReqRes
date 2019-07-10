package com.example.reqres.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class data {

    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("first_name")
    @Expose
    String fname;
    @SerializedName("last_name")
    @Expose
    String lname;
    @SerializedName("avatar")
    @Expose
    String avtar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAvtar() {
        return avtar;
    }

    public void setAvtar(String avtar) {
        this.avtar = avtar;
    }
}
