package dominando.android.provider;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class HotelDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private static final String DIALOG_TAG = "editDialog";
    private static final String EXTRA_HOTEL = "hotel";

    private EditText txtNome;
    private EditText txtEndereco;
    private RatingBar rtbEstrelas;
    private Hotel mHotel;

    public static HotelDialogFragment newInstace(Hotel hotel){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_HOTEL, hotel);

        HotelDialogFragment dialog = new HotelDialogFragment();
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHotel = (Hotel) getArguments().getSerializable(EXTRA_HOTEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_dialog_hotel, container, false);

        txtNome = layout.findViewById(R.id.txtNome);
        txtNome.requestFocus();
        txtEndereco = layout.findViewById(R.id.txtEndereco);
        txtEndereco.setOnEditorActionListener(this);
        rtbEstrelas = layout.findViewById(R.id.rtbEstrelas);

        if(mHotel != null){
            txtNome.setText(mHotel.name);
            txtEndereco.setText(mHotel.address);
            rtbEstrelas.setRating(mHotel.stars);
        }
        //Abre o teclado virtual ao exibir o Dialog
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(R.string.acao_novo);

        return layout;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(EditorInfo.IME_ACTION_DONE == actionId){
            Activity activity = getActivity();

            if(activity instanceof AoSalvarHotel){
                if(mHotel == null){
                    mHotel = new Hotel(
                            txtNome.getText().toString(),
                            txtEndereco.getText().toString(),
                            rtbEstrelas.getRating());
                }else {
                    mHotel.name = txtNome.getText().toString();
                    mHotel.address = txtEndereco.getText().toString();
                    mHotel.stars = rtbEstrelas.getRating();
                }
                AoSalvarHotel listener = (AoSalvarHotel) activity;
                listener.salvouHotel(mHotel);

                dismiss();  //Fecha o dialog
                return true;
            }
        }
        return false;
    }

    public interface AoSalvarHotel{
        void salvouHotel(Hotel hotel);
    }

    public void abrir(FragmentManager fm){
        if(fm.findFragmentByTag(DIALOG_TAG) == null){
            show(fm, DIALOG_TAG);
        }
    }



}
