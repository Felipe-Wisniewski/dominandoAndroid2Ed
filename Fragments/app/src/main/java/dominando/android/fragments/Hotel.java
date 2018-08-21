package dominando.android.fragments;

import java.io.Serializable;

public class Hotel implements Serializable {

    public String name;
    public String address;
    public float stars;

    public Hotel(String name, String address, float stars) {
        this.name = name;
        this.address = address;
        this.stars = stars;
    }

    @Override
    public String toString(){
        return name;
    }
}
