package com.bcit.walksforwalks;

import android.net.Uri;

import com.squareup.picasso.Picasso;

public class User {
    public String fullName;
    public String email;
    public String phone;
    public String petName;
    public String petBreed;
    public String postalCode;
    public String profilePic;

    public User() {

    }

    public User(String fullName, String email, String phone, String petName, String petBreed, String postalCode, String profilePic) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.petName = petName;
        this.petBreed = petBreed;
        this.postalCode = postalCode;
        this.profilePic = profilePic;
    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.phone = "000-000-0000";
        this.petName = "Spot";
        this.petBreed = "mutt";
    }

    public void updateUser(String fullName, String phone, String petName, String petBreed, String postalCode) {
        if (fullName !=null){
            this.fullName = fullName;
        }
        if (fullName !=null){
            this.phone = phone;
        }
        if (fullName !=null){
            this.petName = petName;
        }
        if (fullName !=null){
            this.petBreed = petBreed;
        }
        if (fullName !=null){
            this.postalCode = postalCode;
        }
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public String getPostalCode() { return postalCode; }

    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getProfilePic() { return profilePic; }

    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }
}
