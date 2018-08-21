package dominando.android.componentes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    CompoundButton checkbox;
    ToggleButton toggleButton;
    Switch switchButton;
    SeekBar seekBar;
    Spinner spinner;
    RadioGroup radioGroup;
    TextView txtValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkbox = findViewById(R.id.chkHabilitar);
        toggleButton = findViewById(R.id.tgbHabilitar);
        switchButton = findViewById(R.id.swtHabilitar);
        seekBar = findViewById(R.id.skbValor);
        spinner = findViewById(R.id.spnNomes);
        radioGroup = findViewById(R.id.rgOpcoes);
        txtValor = findViewById(R.id.txtValor);

        configurarSpinner();
        configurarSeekbar();
        configurarSwitch();

        checkbox.setChecked(true);
        seekBar.setProgress(20);
        spinner.setSelection(2);
        radioGroup.check(R.id.rbOpcao2);
    }

    private void configurarSwitch() {
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkbox.setEnabled(buttonView.isChecked());
                toggleButton.setEnabled(isChecked);
            }
        });
    }

    private void configurarSeekbar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtValor.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void configurarSpinner() {
        String[] names = new String[] {"Erick","Felipe","Nelson","Lais","Alipio","Gilcélia"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    public void verValores(View view) {
        int idRadioSelecionado = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(idRadioSelecionado);

        String habilitado = checkbox.isChecked() ? "Habilitado" : "Desabilitado";
        String valor = "valor: " + seekBar.getProgress();
        String nome = "nome: " + spinner.getSelectedItem().toString();
        String opcao = "opção: " + radioButton.getText();

        StringBuilder mensagem = new StringBuilder();
        mensagem.append(habilitado).append("\n").append(valor).append("\n")
                .append(nome).append("\n").append(opcao);

        Toast.makeText(this, mensagem.toString(), Toast.LENGTH_LONG).show();
    }
}
