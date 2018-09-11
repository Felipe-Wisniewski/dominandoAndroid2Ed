package dominando.android.mp3service

import android.os.Binder

class Mp3Binder(s: Mp3Service) : Binder() {

    val servico: Mp3Service

    init {
        servico = s
    }



}