package dominando.android.provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class HotelDetalheFragment extends Fragment {

    public static final String TAG_DETAIL = "tagDetail";
    private static final String EXTRA_HOTEL = "hotel";

    TextView mTextName;
    TextView mTextAddress;
    RatingBar mRatingStars;

    Hotel mHotel;

    ShareActionProvider mShareActionProvider;

    public static HotelDetalheFragment newInstance(Hotel hotel){
        Bundle parameters = new Bundle();
        parameters.putSerializable(EXTRA_HOTEL, hotel);

        HotelDetalheFragment fragment = new HotelDetalheFragment();
        fragment.setArguments(parameters);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHotel = (Hotel) getArguments().getSerializable(EXTRA_HOTEL);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_detail_hotel, container, false);

        mTextName = layout.findViewById(R.id.txtName);
        mTextAddress = layout.findViewById(R.id.txtAddress);
        mRatingStars = layout.findViewById(R.id.rtbStars);

        if(mHotel != null){
            mTextName.setText(mHotel.name);
            mTextAddress.setText(mHotel.address);
            mRatingStars.setRating(mHotel.stars);
        }

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_hotel_detalhe, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        String texto = getString(R.string.texto_compartilhar, mHotel.name, mHotel.stars);

        Intent it = new Intent(Intent.ACTION_SEND);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        it.setType("text/plain");
        it.putExtra(Intent.EXTRA_TEXT, texto);
        mShareActionProvider.setShareIntent(it);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.acao_editar){
            Activity activity = getActivity();
            if(activity instanceof AoEditarHotel){
                AoEditarHotel aoEditarHotel = (AoEditarHotel) activity;
                aoEditarHotel.aoEditarhotel(mHotel);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public Hotel getmHotel(){
        return mHotel;
    }

    public interface AoEditarHotel{
        void aoEditarhotel(Hotel hotel);
    }

















}
