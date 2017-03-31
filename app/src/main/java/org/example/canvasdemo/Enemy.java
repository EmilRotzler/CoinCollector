package org.example.canvasdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Emil Rotzler on 30-03-2017.
 */
public class Enemy implements Parcelable{
    public boolean alive;
    public int posx;
    public int posy;
    public int direction;

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (alive) {
            dest.writeInt(1);
        }
        else{
        dest.writeInt(0);
        }
        dest.writeInt(posx);
        dest.writeInt(posy);
        dest.writeInt(direction);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Enemy createFromParcel(Parcel in) {
            return new Enemy(in);
        }

        public Enemy[] newArray(int size) {
            return new Enemy[size];
        }
    };

    // "De-parcel object
    public Enemy(Parcel in) {
        int tempInt = in.readInt();
        if(tempInt == 1){
            alive = true;
        }
        else{
            alive = false;
        }
        posx = in.readInt();
        posy = in.readInt();
        direction = in.readInt();
    }

    public Enemy(boolean Alive, int Posx, int Posy, int Direction){
        this.alive = Alive;
        this.posx = Posx;
        this.posy = Posy;
        this.direction = Direction;
    }
}
