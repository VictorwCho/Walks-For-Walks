package com.bcit.walksforwalks;

public class Upload {
    private String mImageUri;

    public Upload() {
        // empty constructor needed for firebase
    }

    public Upload( String mImageUri) {
        this.mImageUri = mImageUri;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }
}
