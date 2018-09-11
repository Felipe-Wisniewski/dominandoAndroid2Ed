package dominando.android.handler1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var contador = 0

    lateinit var mHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHandler = Handler()

        button.setOnClickListener {
            button.isEnabled = false
            mHandler.post(MinhaThread())
        }
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacksAndMessages(null)
    }

    inner class MinhaThread : Thread(){
        override fun run() {
            super.run()

            if (contador < 10) {
                contador++
                textView.text = "Contador: $contador"
                mHandler.postDelayed(MinhaThread(), 1000)
            } else {
                contador = 0
                textView.text = "Acabou!"
                button.isEnabled = true
            }
        }
    }
}
