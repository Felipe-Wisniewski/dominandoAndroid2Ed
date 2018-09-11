package dominando.android.mp3service

interface Mp3Service {
    fun play(file: String)
    fun pause()
    fun stop()
    fun getMusicaAtual(): String
    fun getTempoTotal(): Int
    fun getTempoDecorrido(): Int
}