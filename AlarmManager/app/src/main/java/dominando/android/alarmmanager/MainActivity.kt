package dominando.android.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var contador = 0

    lateinit var mHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHandler = Handler()

        buttonStart.setOnClickListener {
            buttonStart.isEnabled = false
            mHandler.post(MinhaThread())
        }

        buttonSchedule.setOnClickListener {
            agendarAlarme()
        }
    }

    private fun agendarAlarme() {

        val tratador : TimePickerDialog.OnTimeSetListener = object : TimePickerDialog.OnTimeSetListener {

            override fun onTimeSet(view : TimePicker, hourOfDay : Int, minute : Int) {
                val it = Intent(this@MainActivity, AlarmReceiver::class.java)
                val pit : PendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, it, 0)
                val alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pit)
            }
        }
        val calendar = Calendar.getInstance()
        val dialog = TimePickerDialog(this, tratador, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true)
        dialog.show()
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
                buttonStart.isEnabled = true
            }
        }
    }
}
