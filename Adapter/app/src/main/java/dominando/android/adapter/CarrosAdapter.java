package dominando.android.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CarrosAdapter extends BaseAdapter {

    Context ctx;
    List<Carro> carros;

    public CarrosAdapter(Context ctx, List<Carro> carros){
        this.ctx = ctx;
        this.carros = carros;
    }

    @Override
    public int getCount() {
        return carros.size();
    }

    @Override
    public Object getItem(int position) {
        return carros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Carro carro = carros.get(position);

        ViewHolder holder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_carro, null);

            holder = new ViewHolder();
            holder.imgLogo = convertView.findViewById(R.id.imgLogo);
            holder.txtModelo = convertView.findViewById(R.id.txtModelo);
            holder.txtAno = convertView.findViewById(R.id.txtAno);
            holder.txtCombustivel = convertView.findViewById(R.id.txtCombustivel);

            convertView.setTag(holder);
            Log.d("NGVL: ", "View Nova => positon: " + position);
        }else{
            holder = (ViewHolder) convertView.getTag();
            Log.d("NGVL: ", "View existente => position: " + position);
        }

        //0 = VW, 1 = GM, 2 = FIAT, 3 = FORD.
        Resources res = ctx.getResources();
        TypedArray logos = res.obtainTypedArray(R.array.logos);

        holder.imgLogo.setImageDrawable(logos.getDrawable(carro.fabricante));
        holder.txtModelo.setText(carro.modelo);
        holder.txtAno.setText(String.valueOf(carro.ano));
        holder.txtCombustivel.setText((carro.gasolinha ? "G" : "") + (carro.etanol ? "E" : ""));

        return convertView;
    }

    static class ViewHolder{
        ImageView imgLogo;
        TextView txtModelo;
        TextView txtAno;
        TextView txtCombustivel;
    }
}
