package dominando.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Tela2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela2);

        TextView txt = findViewById(R.id.txtTextTela2);
        Intent it = getIntent();

        Cliente cliente = it.getParcelableExtra("cliente");

        if(cliente != null){
            String texto = String.format("Nome: %s / Código: %d", cliente.nome, cliente.codigo);
            txt.setText(texto);
        }else{
            String nome = it.getStringExtra("nome");
            int idade = it.getIntExtra("idade", -1);
            txt.setText(String.format("Nome: %s / Idade: %d", nome, idade));
        }

    }
}
