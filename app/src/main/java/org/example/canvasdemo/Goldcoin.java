package org.example.canvasdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Emil Rotzler on 16-03-2017.
 */

public class Goldcoin implements Parcelable{
    public boolean taken;
    public int posx;
    public int posy;

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (taken) {
            dest.writeInt(1);
        }
        else{
            dest.writeInt(0);
        }
        dest.writeInt(posx);
        dest.writeInt(posy);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Goldcoin createFromParcel(Parcel in) {
            return new Goldcoin(in);
        }

        public Goldcoin[] newArray(int size) {return new Goldcoin[size];}
    };

    // "De-parcel object
    public Goldcoin(Parcel in) {
        int tempInt = in.readInt();
        if(tempInt == 1){
            taken = true;
        }
        else{
            taken = false;
        }
        posx = in.readInt();
        posy = in.readInt();
    }

    public Goldcoin (boolean taken, int posx, int posy){
        this.taken = taken;
        this.posx = posx;
        this.posy = posy;
    }
}
