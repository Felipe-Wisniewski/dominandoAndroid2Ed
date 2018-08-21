package dominando.android.webservice;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

public class HotelCursorAdapter extends CursorAdapter {

    public HotelCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtMessage = view.findViewById(R.id.txtNome_ih);
        RatingBar rtbEstrelas = view.findViewById(R.id.rtbEstrelas_ih);

        txtMessage.setText(cursor.getString(cursor.getColumnIndex(HotelSQLHelper.COLUNA_NOME)));
        rtbEstrelas.setRating(cursor.getFloat(cursor.getColumnIndex(HotelSQLHelper.COLUNA_ESTRELAS)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_hotel, null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = super.getView(position, convertView, parent);
        ListView listView = (ListView) parent;

        int color = listView.isItemChecked(position) ?
                Color.argb(0xFF, 0x31, 0xB6, 0xE7) :
                Color.TRANSPARENT;

        v.setBackgroundColor(color);
        return v;
    }
}
