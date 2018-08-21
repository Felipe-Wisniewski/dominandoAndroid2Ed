package dominando.android.intents;

import android.Manifest;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.acoes));

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);

        Uri uri = null;
        Intent intent = null;

        switch (position){
            //Abrindo uma URL
            case 0:
                uri = Uri.parse("https://www.waves.com.br");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                dispararIntent(intent);
                break;

            //Realiza chamada
            case 1:
                if(ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(this,
                            new String[] { Manifest.permission.CALL_PHONE }, 0);
                }else{
                    discar();
                }
                break;

            //Pesquisa uma posição no Maps
            case 2:
                uri = Uri.parse("geo:0,0?q=Rua+Alcides+Cruz+43, Porto Alegre");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                dispararIntent(intent);
                break;

            //Executa música no SDcard
            case 3:
                uri = Uri.parse("file:///mnt/media_rw/0CE8-1C05/musica.mp3");
                intent = new Intent()
                            .setAction(Intent.ACTION_VIEW)
                            .setDataAndType(uri, "audio/mp3");
                dispararIntent(intent);
                break;

            //Abrindo o editor SMS
            case 4:
                uri = Uri.parse("sms:51995927726");
                intent = new Intent(Intent.ACTION_VIEW, uri)
                                .putExtra("sms_body", "Olá, quer tc?");
                dispararIntent(intent);
                break;

            //Compartilhar
            case 5:
                intent = new Intent()
                            .setAction(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_TEXT, "Compartilhando via Intent.")
                            .setType("text/plain");
                dispararIntent(intent);
                break;

            //Alarme
            case 6:
                ArrayList<Integer> dias = new ArrayList<Integer>();
                dias.add(Calendar.MONDAY);
                dias.add(Calendar.WEDNESDAY);
                dias.add(Calendar.FRIDAY);
                intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                                .putExtra(AlarmClock.EXTRA_MESSAGE, "Estudar Android")
                                .putExtra(AlarmClock.EXTRA_HOUR, 19)
                                .putExtra(AlarmClock.EXTRA_MINUTES, 0)
                                .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                                .putExtra(AlarmClock.EXTRA_DAYS, dias);
                dispararIntent(intent);
                break;

            //Busca na web
            case 7:
                intent = new Intent(Intent.ACTION_SEARCH)
                                .putExtra(SearchManager.QUERY, "Android MVVM");
                dispararIntent(intent);
                break;

            //Configurações do aparelho
            case 8:
                intent = new Intent(Settings.ACTION_SETTINGS);
                dispararIntent(intent);
                break;

            //Ação customizada 1
            case 9:
                intent = new Intent("dominando.android.ACAO_CUSTOMIZADA");
                dispararIntent(intent);
                break;

            //Ação customizada 2
            case 10:
                uri = Uri.parse("produto://Notebook/Slim");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                dispararIntent(intent);
                break;

            default:
                finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            discar();
        }
    }

    private void discar() {
        Uri uri = Uri.parse("tel:51995529998");
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        dispararIntent(intent);
    }

    private void dispararIntent(Intent intent) {
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else{
            Toast.makeText(this, R.string.erro_intent, Toast.LENGTH_LONG).show();
        }
    }


}
