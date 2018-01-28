package com.willowtreeapps.doggogame.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Doggo implements Parcelable {
    private String breedName;
    private String imageUrl;

    public Doggo(String breedName, String imageUrl) {
        this.breedName = breedName;
        this.imageUrl = imageUrl;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.breedName);
        dest.writeString(this.imageUrl);
    }

    protected Doggo(Parcel in) {
        this.breedName = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Creator<Doggo> CREATOR = new Creator<Doggo>() {
        @Override
        public Doggo createFromParcel(Parcel source) {
            return new Doggo(source);
        }

        @Override
        public Doggo[] newArray(int size) {
            return new Doggo[size];
        }
    };
}
