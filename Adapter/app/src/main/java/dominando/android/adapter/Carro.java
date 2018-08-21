package dominando.android.adapter;

public class Carro {
    public String modelo;
    public int ano;
    public int fabricante; //0 = VW, 1 = GM, 2 = FIAT, 3 = FORD.
    public boolean gasolinha;
    public boolean etanol;

    public Carro(String modelo, int ano, int fabricante, boolean gasolinha, boolean etanol) {
        this.modelo = modelo;
        this.ano = ano;
        this.fabricante = fabricante;
        this.gasolinha = gasolinha;
        this.etanol = etanol;
    }
}