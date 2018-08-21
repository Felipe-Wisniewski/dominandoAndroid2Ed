package dominando.android.navegacao;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class TelaSpinnerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String OPCAO_ATUAL = "opcaoAtual";
    Toolbar mToolbar;
    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                getResources().getStringArray(R.array.secoes));

        mSpinner = new Spinner(this);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.addView(mSpinner);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(OPCAO_ATUAL, mSpinner.getSelectedItemPosition());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState.containsKey(OPCAO_ATUAL)){
            mSpinner.setSelection(savedInstanceState.getInt(OPCAO_ATUAL));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        exibirItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {    }

    private void exibirItem(int position){
        ((TextView)mSpinner.getChildAt(0)).setTextColor(Color.WHITE);
        String[] titulosAbas = getResources().getStringArray(R.array.secoes);
        TypedArray bgColors = getResources().obtainTypedArray(R.array.cores_bg);
        TypedArray textColors = getResources().obtainTypedArray(R.array.cores_texto);

        SegundoNivelFragment fragment = SegundoNivelFragment.novaInstancia(
                titulosAbas[position],
                bgColors.getColor(position, 0),
                textColors.getColor(position, 0));

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag("tag");
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment, "tag");
        if(f != null){
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        ft.commit();
    }

}
