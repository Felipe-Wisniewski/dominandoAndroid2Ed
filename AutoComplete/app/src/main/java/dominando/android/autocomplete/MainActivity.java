package dominando.android.autocomplete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> cidade = new ArrayList<String>();
        cidade.add("Santos");
        cidade.add("Porto Alegre");
        cidade.add("Pelotas");
        cidade.add("Guarujá");
        cidade.add("Florianópolis");
        cidade.add("São Paulo");
        cidade.add("Salvador");
        cidade.add("Rio de Janeiro");
        cidade.add("Cubatão");
        cidade.add("Alagoas");
        cidade.add("Brasília");
        cidade.add("Balneário Camburiu");

        MeuAutoCompleteAdapter adapter = new MeuAutoCompleteAdapter(this,
                android.R.layout.simple_dropdown_item_1line, cidade);

        AutoCompleteTextView actv = findViewById(R.id.autoCompleteTextView);
        actv.setAdapter(adapter);
    }
}
