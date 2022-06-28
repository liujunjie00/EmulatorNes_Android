package com.ritchie.mapsandftms.proFile;

import android.os.Parcel;
import android.os.Parcelable;

public class BIkeData1Parcelable implements Parcelable {
    private BikeData1 bikeData1;

    protected BIkeData1Parcelable(Parcel in) {
    }

    public static final Creator<BIkeData1Parcelable> CREATOR = new Creator<BIkeData1Parcelable>() {
        @Override
        public BIkeData1Parcelable createFromParcel(Parcel in) {
            return new BIkeData1Parcelable(in);
        }

        @Override
        public BIkeData1Parcelable[] newArray(int size) {
            return new BIkeData1Parcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
