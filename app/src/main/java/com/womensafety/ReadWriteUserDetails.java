package com.womensafety;

public class ReadWriteUserDetails {


    String username, email, phone;

    public ReadWriteUserDetails()
    {

    }

    public ReadWriteUserDetails(String username, String email, String phone) {
        this.username=username;
        this.email=email;
        this.phone=phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
