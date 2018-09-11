package dominando.android.textview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView com fonte customizada
        Typeface typeface = Typeface.createFromAsset(getAssets(), "From Cartoon Blocks.ttf");
        TextView txt1 = findViewById(R.id.txt_fontCustom);
        txt1.setTypeface(typeface);

        //TextView com fonte riscada
        TextView txt2 = findViewById(R.id.txt_strike);
        txt2.setPaintFlags(txt2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        //TextView html
        TextView txtHtml = findViewById(R.id.txt_html);
        final String textoEmHtml =
                "<html><body>Html em "
                + "<b>Negrito</b>, <i>Itálico</i>"
                + " e <u>Sublinhado</u>.<br>"
                + "Mario: <img src='mario.png' /><br>"
                + "Yoshi: <img src='yoshi.png' /><br>"
                + "Um texto depois da imagem</body></html>";

        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                BitmapDrawable drawable = null;
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open(source));
                    drawable = new BitmapDrawable(getResources(), bitmap);
                    drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return drawable;
            }
        };
        txtHtml.setText(Html.fromHtml(textoEmHtml, imageGetter, null));

        
    }
}
