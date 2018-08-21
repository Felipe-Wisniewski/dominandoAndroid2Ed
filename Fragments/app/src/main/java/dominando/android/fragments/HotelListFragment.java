package dominando.android.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HotelListFragment extends ListFragment implements ActionMode.Callback, AdapterView.OnItemLongClickListener {

    List<Hotel> mHotels;
    ArrayAdapter<Hotel> mAdapter;

    ListView mListView;
    ActionMode mActionMode;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setRetainInstance(true);
        mHotels = loadHotels();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = getListView();
        limparBusca();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mActionMode != null){
            iniciarModoExclusao();
            atualizarTitulo();
        }
    }

    //Carrega uma lista de Hoteis
    private List<Hotel> loadHotels() {
        List<Hotel> hotels = new ArrayList<Hotel>();

        hotels.add(new Hotel("New Beach Hotel", "Av. Boa Viagem", 4.5f));
        hotels.add(new Hotel("Recife Hotel", "Rua dos Navegantes", 2.5f));
        hotels.add(new Hotel("Canário Hotel", "Av. Protásio Alves", 4.1f));
        hotels.add(new Hotel("Hotel Praiano", "Av. Conselheiro Nébias", 5f));
        hotels.add(new Hotel("Ibis Hotel", "Rua da Praia", 4.5f));
        hotels.add(new Hotel("Mércuri Hotel", "Av. Washiton Luiz", 1.5f));
        hotels.add(new Hotel("Grand Hotel Dor", "Av. Washiton Luiz", 4.4f));
        hotels.add(new Hotel("Hotel Tulipa", "Rua Ribeiro Brito", 2.7f));
        hotels.add(new Hotel("Hotel Infinit", "Av. Boa Viagem", 5f));
        hotels.add(new Hotel("Hotel LePulgue", "Av. Epitácio Pessoa", 1.5f));
        hotels.add(new Hotel("Grand Shit Hotel", "Rua da Vala", 4.2f));
        hotels.add(new Hotel("Hotel GMT", "Av. General San Martin", 5f));
        hotels.add(new Hotel("Hotel DuQue", "Av. Viagem Boa", 3.5f));

        return hotels;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(mActionMode == null){
            Activity activity = getActivity();

            if(activity instanceof AoClicarNoHotel){
                Hotel hotel = (Hotel) l.getItemAtPosition(position);

                AoClicarNoHotel listener = (AoClicarNoHotel) activity;
                listener.clicouNoHotel(hotel);
            }
        }else {
            atualizarItensMarcados(mListView, position);
            if(qtdeItensMarcados() == 0){
                mActionMode.finish();
            }
        }
    }

    public interface AoClicarNoHotel{
        void clicouNoHotel(Hotel hotel);
    }

    public void buscar(String s){
        if(s == null || s.trim().equals("")){
            limparBusca();
            return;
        }

        List<Hotel> hoteisEncontrados = new ArrayList<Hotel>(mHotels);

        for (int i = hoteisEncontrados.size() - 1; i >= 0; i-- ){
            Hotel hotel = hoteisEncontrados.get(i);
            if(! hotel.name.toUpperCase().contains(s.toUpperCase())){
                hoteisEncontrados.remove(hotel);
            }
        }
        mListView.setOnItemLongClickListener(null);

        mAdapter = new ArrayAdapter<Hotel>(
                getActivity(), android.R.layout.simple_list_item_1, hoteisEncontrados);
        setListAdapter(mAdapter);
    }

    public void limparBusca() {
        mListView.setOnItemLongClickListener(this);
        ordenar();
        mAdapter = new ArrayAdapter<Hotel>(
                getActivity(), android.R.layout.simple_list_item_activated_1, mHotels);
        setListAdapter(mAdapter);
    }

    public void adicionar(Hotel hotel){
        mHotels.add(hotel);
        ordenar();
        mAdapter.notifyDataSetChanged();
    }

    private void ordenar(){
        Collections.sort(mHotels, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel o1, Hotel o2) {
                return o1.name.compareTo(o2.name);
            }
        });
    }

    //Metodos do ActionMode.Callback
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu_delete_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if(item.getItemId() == R.id.acao_delete){
            remover();
            mode.finish();
            return true;
        }
        return false;
    }

    private void remover(){
        final List<Hotel> hoteisExcluidos = new ArrayList<Hotel>();

        SparseBooleanArray checked = mListView.getCheckedItemPositions();
        for (int i = checked.size() - 1; i >= 0; i--){
            if(checked.valueAt(i)){
                int position = checked.keyAt(i);
                hoteisExcluidos.add(mHotels.remove(position));
            }
        }

        Snackbar.make(mListView, getString(R.string.mensagem_excluir, hoteisExcluidos.size()),
                Snackbar.LENGTH_LONG).setAction(R.string.desfazer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Hotel hotel : hoteisExcluidos){
                    mHotels.add(hotel);
                }
                limparBusca();
            }
        }).show();
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        mListView.clearChoices();
        mListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        limparBusca();
    }

    //Metodo do AdapterView.OnItemLongClickListener
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        boolean consumed = (mActionMode == null);

        if(consumed){
            iniciarModoExclusao();
            mListView.setItemChecked(position, true);
            atualizarItensMarcados(mListView, position);
        }
        return consumed;
    }

    private void iniciarModoExclusao(){
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mActionMode = activity.startSupportActionMode(this);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void atualizarItensMarcados(ListView l, int position){
        l.setItemChecked(position, l.isItemChecked(position));
        atualizarTitulo();
    }

    private int qtdeItensMarcados(){
        SparseBooleanArray checked = mListView.getCheckedItemPositions();
        int checkedCount = 0;
        for(int i = 0; i < checked.size(); i++){
            if(checked.valueAt(i)){
                checkedCount++;
            }
        }
        return checkedCount;
    }

    private void atualizarTitulo(){
        int checkedCount = qtdeItensMarcados();
        String selecionados = getResources().getQuantityString(
                R.plurals.numero_selecionados, checkedCount, checkedCount);
        mActionMode.setTitle(selecionados);
    }
}
