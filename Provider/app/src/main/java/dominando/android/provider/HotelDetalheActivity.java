package dominando.android.provider;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HotelDetalheActivity extends AppCompatActivity implements HotelDetalheFragment.AoEditarHotel, HotelDialogFragment.AoSalvarHotel{

    public static final String EXTRA_HOTEL = "hotel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detalhe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //Botão voltar da ActionBar

        Intent intent = getIntent();
        Hotel hotel = (Hotel) intent.getSerializableExtra(EXTRA_HOTEL);
        exibirHotelFragment(hotel);
    }

    private void exibirHotelFragment(Hotel hotel) {
        HotelDetalheFragment fragment = HotelDetalheFragment.newInstance(hotel);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.detalhe, fragment, HotelDetalheFragment.TAG_DETAIL);
        ft.commit();
    }

    @Override
    public void aoEditarhotel(Hotel hotel) {
        HotelDialogFragment editNameDialog = HotelDialogFragment.newInstace(hotel);
        editNameDialog.abrir(getSupportFragmentManager());
    }

    @Override
    public void salvouHotel(Hotel hotel) {
        HotelRepositorio repositorio = new HotelRepositorio(this);
        repositorio.salvar(hotel);
        exibirHotelFragment(hotel);
        setResult(RESULT_OK);
    }
}
