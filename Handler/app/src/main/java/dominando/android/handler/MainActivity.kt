package dominando.android.handler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val CONTADOR = 1
        val ACABOU = 2
    }

    lateinit var mHandler : MeuHandler
    var mThread : MinhaThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHandler = MeuHandler()

        button.setOnClickListener {
            mThread = MinhaThread()
            mThread!!.start()
            button.isEnabled = false
        }
    }

    override fun onPause() {
        super.onPause()

        mHandler.removeCallbacksAndMessages(null)
        if (mThread != null) mThread!!.interrupt()
    }

    inner class MeuHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            if (msg?.what == CONTADOR){
                textView.text = "Contador: ${msg.arg1}"
            }else if (msg?.what == ACABOU){
                textView.text = "Acabou!"
                button.isEnabled = true
            }
        }
    }

    inner class MinhaThread : Thread(){
        override fun run() {
            super.run()

            var graus = 0
            while (graus < 10){
                graus++
                val message = Message()
                message.what = CONTADOR
                message.arg1 = graus
                mHandler.sendMessage(message)
                try {
                    Thread.sleep(1000)
                }catch (e : InterruptedException){
                    e.printStackTrace()
                }
            }
            mHandler.sendEmptyMessage(ACABOU)
        }
    }
}
