package dominando.android.hotel;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;


public class HotelActivity extends AppCompatActivity implements HotelListFragment.AoClicarNoHotel,
        HotelDialogFragment.AoSalvarHotel, SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener, HotelDetalheFragment.AoEditarHotel,
        HotelListFragment.AoExcluirHoteis{

    public static final int REQUEST_EDITAR_HOTEL = 0;

    private long mIdSelecionado;

    private FragmentManager mFragmentManager;
    private HotelListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        mFragmentManager = getSupportFragmentManager();
        mListFragment = (HotelListFragment) mFragmentManager.findFragmentById(R.id.fragmentList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hotel, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.hint_busca));

        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_EDITAR_HOTEL && resultCode == RESULT_OK){
            mListFragment.limparBusca();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_info:
                SobreDialogFragment dialogFragment = new SobreDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "sobre");
                break;

            /*case R.id.action_new:
                HotelDialogFragment hotelDialogFragment = HotelDialogFragment.newInstace(null);
                hotelDialogFragment.abrir(getSupportFragmentManager());
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarHotelClick(View v){
        HotelDialogFragment hotelDialogFragment = HotelDialogFragment.newInstace(null);
        hotelDialogFragment.abrir(getSupportFragmentManager());
    }

    @Override
    public void clicouNoHotel(Hotel hotel) {
        mIdSelecionado = hotel.id;
        if(isTablet()){
            HotelDetalheFragment fragment = HotelDetalheFragment.newInstance(hotel);

            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.detalhe, fragment, HotelDetalheFragment.TAG_DETAIL);
            ft.commit();
        }else{
            Intent it = new Intent(this, HotelDetalheActivity.class);
            it.putExtra(HotelDetalheActivity.EXTRA_HOTEL, hotel);
            startActivityForResult(it, REQUEST_EDITAR_HOTEL);
        }
    }

    private boolean isTablet(){
        return getResources().getBoolean(R.bool.tablet);
    }

    @Override
    public void salvouHotel(Hotel hotel) {
        HotelRepositorio repositorio = new HotelRepositorio(this);
        repositorio.salvar(hotel);
        mListFragment.limparBusca();
        if(isTablet()){
            clicouNoHotel(hotel);
        }
    }

    @Override
    public void aoEditarhotel(Hotel hotel) {
        HotelDialogFragment editNameDialog = HotelDialogFragment.newInstace(hotel);
        editNameDialog.abrir(getSupportFragmentManager());
    }

    @Override
    public void exclusaoCompleta(List<Hotel> excluidos) {
        HotelDetalheFragment f = (HotelDetalheFragment)
                mFragmentManager.findFragmentByTag(HotelDetalheFragment.TAG_DETAIL);

        if(f != null){
            boolean encontrou = false;
            for(Hotel h : excluidos){
                if(h.id == mIdSelecionado){
                    encontrou = true;
                    break;
                }
            }
            if(encontrou){
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.remove(f);
                ft.commit();
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mListFragment.buscar(newText);
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;  //para expandir a view.
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        mListFragment.limparBusca();
        return true;   //para voltar ao normal.
    }

    //========================================CLASS=================================================
    public static class SobreDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(which == DialogInterface.BUTTON_NEGATIVE){
                        Intent it = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.nglauber.com.br"));
                        startActivity(it);
                    }
                }
            };

            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.sobre_titulo)
                    .setMessage(R.string.sobre_mensagem)
                    .setPositiveButton(android.R.string.ok, null)
                    .setNegativeButton(R.string.sobre_botao_site, listener)
                    .create();

            return dialog;
        }
    }
}
