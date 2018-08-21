package dominando.android.httpbroadcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class LivrosGridAdapter extends ArrayAdapter<Livro> {

    private ImageLoader mLoader;

    public LivrosGridAdapter(Context context, List<Livro> objects) {
        super(context, 0, objects);
        mLoader = VolleySingleton.getInstance(context).getImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context ctx = parent.getContext();
        if(convertView == null){
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_livro_grid, null);
        }

        NetworkImageView img = convertView.findViewById(R.id.imgCapa_ilg);
        TextView txt = convertView.findViewById(R.id.txtTitulo_ilg);

        Livro livro = getItem(position);
        txt.setText(livro.titulo);
        img.setImageUrl(livro.capa, mLoader);

        return convertView;
    }
}
