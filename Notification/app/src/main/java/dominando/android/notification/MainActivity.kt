package dominando.android.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val NOTIFICACAO_SIMPLES = 1
        val NOTIFICACAO_COMPLETA = 2
        val NOTIFICACAO_BIG = 3
    }

    lateinit var mReceiver : MeuReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mReceiver = MeuReceiver()

        registerReceiver(mReceiver, IntentFilter(NotificationUtils.ACAO_DELETE))
        registerReceiver(mReceiver, IntentFilter(NotificationUtils.ACAO_NOTIFICACAO))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

    fun criarNotificacaoSimples(v: View){
        NotificationUtils.criarNotificacaoSimples(
                this,
                editText.text.toString(),
                NOTIFICACAO_SIMPLES)
    }

    fun criarNotificacaoCompleta(v: View){
        NotificationUtils.criarNotificacaoCompleta(
                this,
                editText.text.toString(),
                NOTIFICACAO_COMPLETA)
    }

    fun criarNotificacaoBig(v: View){
        NotificationUtils.criarNotificationBig(
                this,
                NOTIFICACAO_BIG)
    }

    class MeuReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "${intent?.action}" , Toast.LENGTH_SHORT).show()
        }
    }
}
