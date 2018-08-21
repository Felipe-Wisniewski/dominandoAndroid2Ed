package dominando.android.expandlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExpandableListView listView = findViewById(R.id.expandableListView);

        List<String> listPe = new ArrayList<String>();
        listPe.add("Caruaru");
        listPe.add("Recife");
        listPe.add("Fernando de Noronha");

        List<String> listSp = new ArrayList<String>();
        listSp.add("São Paulo");
        listSp.add("Campinas");
        listSp.add("Santos");
        listSp.add("Guarujá");

        List<String> listRs = new ArrayList<String>();
        listRs.add("Porto Alegre");
        listRs.add("Pelotas");
        listRs.add("Tapes");

        List<String> listSc = new ArrayList<String>();
        listSc.add("Florianópolis");
        listSc.add("Imbituba");
        listSc.add("Joinvile");

        List<String> listRj = new ArrayList<String>();
        listRj.add("Rio de Janeiro");
        listRj.add("Paraty");
        listRj.add("Angra dos Reis");

        Map<String, List<String>> dados = new HashMap<String, List<String>>();
        dados.put("PE", listPe);
        dados.put("SP", listSp);
        dados.put("RS", listRs);
        dados.put("SC", listSc);
        dados.put("RJ", listRj);

        listView.setAdapter(new MeuExpandableAdapter(dados));
    }
}
