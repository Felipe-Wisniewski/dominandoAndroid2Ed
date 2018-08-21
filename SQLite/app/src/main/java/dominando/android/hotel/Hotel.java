package dominando.android.hotel;

import java.io.Serializable;

public class Hotel implements Serializable {

    public long id;
    public String name;
    public String address;
    public float stars;

    public Hotel(long id, String name, String address, float stars) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.stars = stars;
    }

    public Hotel(String name, String address, float stars) {
        this(0, name, address, stars);
    }

    @Override
    public String toString(){
        return name;
    }
}
