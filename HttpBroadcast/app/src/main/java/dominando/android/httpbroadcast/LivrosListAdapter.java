package dominando.android.httpbroadcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class LivrosListAdapter extends ArrayAdapter<Livro> {

    public LivrosListAdapter(Context context, List<Livro> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Livro livro = getItem(position);
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_livro_list, null);

            holder = new ViewHolder();
            holder.imgCapa = convertView.findViewById(R.id.imgCapa_ill);
            holder.txtTitulo = convertView.findViewById(R.id.txtTitulo_ill);
            holder.txtAutores = convertView.findViewById(R.id.txtAutores_ill);
            holder.txtPaginas = convertView.findViewById(R.id.txtPaginas_ill);
            holder.txtAno = convertView.findViewById(R.id.txtAno_ill);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.get().load(livro.capa).into(holder.imgCapa);
        holder.txtTitulo.setText(livro.titulo);
        holder.txtAutores.setText(livro.autor);
        holder.txtAno.setText(String.valueOf(livro.ano));
        holder.txtPaginas.setText(getContext().getString(R.string.n_paginas, livro.paginas));

        return convertView;
    }

    static class ViewHolder {
        ImageView imgCapa;
        TextView txtTitulo;
        TextView txtAutores;
        TextView txtPaginas;
        TextView txtAno;
    }
}
