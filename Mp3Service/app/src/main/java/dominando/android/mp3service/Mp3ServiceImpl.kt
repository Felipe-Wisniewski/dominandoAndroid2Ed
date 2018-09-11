package dominando.android.mp3service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import java.io.FileInputStream

class Mp3ServiceImpl : Service(), Mp3Service {

    companion object {
        const val EXTRA_ARQUIVO = "arquivo"
        const val EXTRA_ACAO = "acao"
        const val ACAO_PLAY = "play"
        const val ACAO_PAUSE = "pause"
        const val ACAO_STOP = "stop"
    }

    private lateinit var mPlayer: MediaPlayer
    private var mArquivo = ""
    private var mPausado: Boolean = false

    override fun onCreate() {
        super.onCreate()
        mPlayer = MediaPlayer()
    }

    override fun onBind(intent: Intent): IBinder {
        return Mp3Binder(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null)
            when(intent.getStringExtra(EXTRA_ACAO)){
                ACAO_PLAY  -> play(intent.getStringExtra(EXTRA_ARQUIVO))
                ACAO_PAUSE  -> pause()
                ACAO_STOP  -> stop()
            }

        return super.onStartCommand(intent, flags, startId)
    }

    //IMPLEMENTANDO A INTERFACE MP3SERVICE
    override fun play(file: String) {
        if (!mPlayer.isPlaying && !mPausado) {
            try {
                mPlayer.reset()
                val fileInputStream = FileInputStream(file)
                mPlayer.setDataSource(fileInputStream.fd)
                mPlayer.prepare()
                mArquivo = file
            }catch (e: Exception){
                e.printStackTrace()
                return
            }
        }
        mPausado = false
        mPlayer.start()
    }

    override fun pause() {
        if (mPlayer.isPlaying){
            mPausado = true
            mPlayer.pause()
        }
    }

    override fun stop() {
        if (mPlayer.isPlaying || mPausado){
            mPausado = false
            mPlayer.stop()
            mPlayer.reset()
        }
    }

    override fun getMusicaAtual(): String {
        return mArquivo
    }

    override fun getTempoTotal(): Int {
        if (mPlayer.isPlaying || mPausado){
            return mPlayer.duration
        }
        return 0
    }

    override fun getTempoDecorrido(): Int {
        if (mPlayer.isPlaying || mPausado){
            return mPlayer.currentPosition
        }
        return 0
    }

}
