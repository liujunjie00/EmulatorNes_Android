package com.ritchie.mapsandftms.proFile;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class ListParcelable implements Parcelable {
    private List<String[]> list = new ArrayList<>();

    protected ListParcelable(Parcel in) {
       list =  in.readArrayList(ClassLoader.getSystemClassLoader());
    }

    public static final Creator<ListParcelable> CREATOR = new Creator<ListParcelable>() {
        @Override
        public ListParcelable createFromParcel(Parcel in) {
            return new ListParcelable(in);
        }

        @Override
        public ListParcelable[] newArray(int size) {
            return new ListParcelable[size];
        }
    };

    public ListParcelable(List<String[]> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(list);
    }

    public List<String[]> getList() {
        return list;
    }

    public void setList(List<String[]> list) {
        this.list = list;
    }
}
