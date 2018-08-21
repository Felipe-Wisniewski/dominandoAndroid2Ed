package dominando.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String text = editText.getText().toString();

        switch (v.getId()){
            case R.id.button1:
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                break;

            case R.id.button2:
                Intent it = new Intent(this, Tela2Activity.class);
                it.putExtra("nome", "Felipe");
                it.putExtra("idade", 31);
                startActivity(it);
                break;

            case R.id.button3:
                Cliente cliente =  new Cliente(1,"Felipe");
                Intent intent = new Intent(this, Tela2Activity.class);
                intent.putExtra("cliente", cliente);
                startActivity(intent);
                break;
        }

    }
}
