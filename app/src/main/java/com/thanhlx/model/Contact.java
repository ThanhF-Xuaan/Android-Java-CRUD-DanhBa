package com.thanhlx.model;

import androidx.annotation.NonNull;

public class Contact {
    private int id;
    private String name;
    private String phone_number;
    private byte[] avt;

    public Contact() {
    }

    public Contact(int id, String name, String phone_number, byte[] avt) {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.avt = avt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public byte[] getAvt() {
        return avt;
    }

    public void setAvt(byte[] avt) {
        this.avt = avt;
    }

    @NonNull
    @Override
    public String toString() {
        String s = "";
        s += this.id + " - ";
        s += this.name + "\n";
        s+= this.phone_number + " - ";
        if(this.avt != null) s+= "co anh";
        else s += "khong co anh";
        return s;
    }
}
