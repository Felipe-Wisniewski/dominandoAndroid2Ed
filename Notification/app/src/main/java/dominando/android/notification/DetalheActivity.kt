package dominando.android.notification

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detalhe.*

class DetalheActivity : AppCompatActivity() {

    companion object {
        val EXTRA_TEXTO = "texto"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe)

        val str = intent.getStringExtra(EXTRA_TEXTO)
        txtDetalhe.text = str
    }
}
