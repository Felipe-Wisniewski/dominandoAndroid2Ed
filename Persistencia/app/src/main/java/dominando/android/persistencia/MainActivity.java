package dominando.android.persistencia;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PERMISSAO_SDCARD = 0;

    EditText editText;
    TextView textView;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edtTexto);
        textView = findViewById(R.id.txtTexto);
        radioGroup = findViewById(R.id.rgTipo);

        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnLer).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean ler = false;
        if(v.getId() == R.id.btnLer){
            ler = true;
        }

        int tipo = radioGroup.getCheckedRadioButtonId();

        if(ler){
            switch (tipo){
                case R.id.rbInterno:
                    carregarInterno();
                    break;

                case R.id.rbExtPrivado:
                    carregarDoSdCard(true);
                    break;

                case R.id.rbExtPublica:
                    carregarDoSdCard(false);
                    break;
            }
        }else {
            switch (tipo){
                case R.id.rbInterno:
                    salvarInterno();
                    break;

                case R.id.rbExtPrivado:
                    salvarNoSdCard(true);
                    break;

                case R.id.rbExtPublica:
                    salvarNoSdCard(false);
                    break;
            }
        }
    }

    private void salvarInterno() {
        try{
            FileOutputStream fos = openFileOutput("arquivo.txt", Context.MODE_PRIVATE);
            salvar(fos);
        } catch (IOException e) {
            Log.e("FLMWG", "Erro ao salvar arquivo", e);
        }
    }

    private void carregarInterno() {
        try{
            FileInputStream fis = openFileInput("arquivo.txt");
            carregar(fis);
        } catch (IOException e) {
            Log.e("FLMWG", "Erro ao carregar arquivo", e);
        }
    }

    private void salvarNoSdCard(boolean privado) {

        boolean temPermissao = checarPermissao(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSAO_SDCARD);

        if(!temPermissao) return;

        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){
            File meuDir = getExternalDir(privado);

            try{
                if(!meuDir.exists()){
                    meuDir.mkdir();
                }

                File arquivoTxt = new File(meuDir, "arquivo.txt");

                if(!arquivoTxt.exists()){
                    arquivoTxt.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(arquivoTxt);
                salvar(fos);

            } catch (IOException e) {
                Log.e("FLMWG", "Erro ao salvar arquivo", e);
            }
        }else {
            Log.e("FLMWG", "Não é possível escrever no SD Card");
        }
    }

    private void carregarDoSdCard(boolean privado) {
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(privado) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(privado)){
            File meuDir = getExternalDir(privado);

            if(meuDir.exists()){
                File arquivoTxt = new File(meuDir, "arquivo.txt");

                if(arquivoTxt.exists()){
                    try{
                        arquivoTxt.createNewFile();
                        FileInputStream fis = new FileInputStream(arquivoTxt);
                        carregar(fis);

                    } catch (IOException e) {
                        Log.d("FLMWG", "Erro ao carregar arquivo", e);
                    }
                }
            }
        }else {
            Log.e("FLMWG", "SD Card indisponível");
        }
    }

    private File getExternalDir(boolean privado) {
        if(privado){
            // SDCard/Android/data/pacote.da.App/files
            return getExternalFilesDir(null);
        }else {
            // SDCard/DCIM
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        }
    }

    private void salvar(FileOutputStream fos) throws IOException {
        String[] linhas = TextUtils.split(editText.getText().toString(), "\n");

        PrintWriter writer = new PrintWriter(fos);

        for(String linha : linhas){
            writer.println(linha);
        }
        writer.flush();
        writer.close();
        fos.close();
    }

    private void carregar(FileInputStream fis) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

        StringBuilder sb = new StringBuilder();
        String linha;

        while ((linha = reader.readLine()) != null){
            if(sb.length() != 0) sb.append('\n');
            sb.append(linha);
        }
        reader.close();
        fis.close();

        textView.setText(sb.toString());
    }

    private boolean checarPermissao(String permissao, int requestCode){

        if(ActivityCompat.checkSelfPermission(this, permissao) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissao)){
                Toast.makeText(this, "Você tem que habilitar essa permissão para" +
                        " poder salvar o arquivo", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{permissao}, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSAO_SDCARD: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permissão concedida", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //======================== Preferências Button ===================================
    public void abrirPreferencias(View v){
        startActivity(new Intent(this, ConfigActivity.class));
    }

    public void lerPreferencias(View v){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String cidade = prefs.getString(getString(R.string.pref_cidade), getString(R.string.pref_cidade_default));
        String redeSocial = prefs.getString(getString(R.string.pref_rede_social),
                getString(R.string.pref_rede_social_default));
        boolean mensagens = prefs.getBoolean(getString(R.string.pref_mensagens), false);
        String msg = String.format("%s = %s\n%s = %s\n%s = %s",
                getString(R.string.titulo_cidade), cidade,
                getString(R.string.titulo_rede_social), redeSocial,
                getString(R.string.titulo_mensagens), String.valueOf(mensagens));

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


}
