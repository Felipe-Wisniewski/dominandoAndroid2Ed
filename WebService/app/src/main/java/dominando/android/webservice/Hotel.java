package dominando.android.webservice;

import java.io.Serializable;

public class Hotel implements Serializable {

    public long id;
    public String name;
    public String address;
    public float stars;
    public Status status;
    public long idServidor;

    public Hotel(long id, String name, String address, float stars, long idServidor, Status status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.stars = stars;
        this.idServidor = idServidor;
        this.status = status;
    }

    public Hotel(String name, String address, float stars) {
        this(0, name, address, stars, 0, Status.INSERIR);
    }

    @Override
    public String toString(){
        return name;
    }

    public enum Status {
        OK, INSERIR, ATUALIZAR, EXCLUIR
    }
}
