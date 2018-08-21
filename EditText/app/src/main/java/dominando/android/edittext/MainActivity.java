package dominando.android.edittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    EditText mEdtName;
    EditText mEdtEmail;
    EditText mEdtPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdtName = findViewById(R.id.edt_name);
        mEdtEmail = findViewById(R.id.edt_email);
        mEdtPsw = findViewById(R.id.edt_psw);
        mEdtPsw.setOnEditorActionListener(this);

        final EditText edtZipCode = findViewById(R.id.edt_zip);
        edtZipCode.addTextChangedListener(new TextWatcher() {

            boolean isUpdating;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isUpdating){
                    isUpdating = false;
                    return;
                }

                boolean hasMask = s.toString().indexOf('.') > -1 || s.toString().indexOf('-') > -1;

                String str = s.toString()
                        .replaceAll("[.]", "")
                        .replaceAll("[-]","");

                if(count > before){
                    if(str.length() > 5){
                        str = str.substring(0,2) + '.' +
                                str.substring(2,5) + '-' +
                                str.substring(5);
                    }else if(str.length() > 2){
                        str = str.substring(0,2) + '.' +
                                str.substring(2);
                    }

                    isUpdating = true;

                    edtZipCode.setText(str);
                    edtZipCode.setSelection(edtZipCode.getText().length());

                }else{
                    isUpdating = true;

                    edtZipCode.setText(str);
                    edtZipCode.setSelection(Math.max(0, Math.min(hasMask ? start - before : start, str.length())));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(v == mEdtPsw && EditorInfo.IME_ACTION_DONE == actionId) {
            String name = mEdtName.getText().toString();
            String email = mEdtEmail.getText().toString();
            String password = mEdtPsw.getText().toString();
            boolean ok =  true;

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                mEdtEmail.setError(getString(R.string.msg_error_email));
                ok = false;
            }

            if(!password.equals("123")){
                mEdtPsw.setError(getString(R.string.msg_error_password));
                ok = false;
            }

            if(ok){
                Toast.makeText(this, getString(R.string.msg_sucess, name, email),
                        Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return false;
    }
}
