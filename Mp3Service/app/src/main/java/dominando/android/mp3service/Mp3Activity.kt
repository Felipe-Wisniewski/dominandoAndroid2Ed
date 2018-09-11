package dominando.android.mp3service

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v4.widget.SimpleCursorAdapter
import android.text.format.DateUtils
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_mp3.*

class Mp3Activity : AppCompatActivity(), ServiceConnection, AdapterView.OnItemClickListener,
                        LoaderManager.LoaderCallbacks<Cursor> {

    private var mMusica: String? = null

    private var mMP3Service: Mp3Service? = null
    private lateinit var mAdapter: SimpleCursorAdapter

    val colunas = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATA,
                                MediaStore.MediaColumns._ID)

    private val mHandler = Handler()
    private val mThreadProgresso = object:Thread() {
        override fun run() {
            atualizarTela()
            if (mMP3Service!!.getTempoTotal() > mMP3Service!!.getTempoDecorrido()) {
                mHandler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mp3)

        val componentes = IntArray(2)
        componentes[0] = android.R.id.text1
        componentes[1] = android.R.id.text2

        mAdapter = SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null,
                                        colunas, componentes, 0)
    }

    override fun onResume() {
        super.onResume()

        if (mAdapter.count == 0){
            val permissao = Manifest.permission.READ_EXTERNAL_STORAGE

            if (ActivityCompat.checkSelfPermission(this, permissao) == PackageManager.PERMISSION_GRANTED){
                supportLoaderManager.initLoader(0, null, this)

            }else{
                ActivityCompat.requestPermissions(this, arrayOf(permissao), 0)
            }
        }
        val it = Intent(this, Mp3ServiceImpl::class.java)
        startService(it)
        bindService(it, this, 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            supportLoaderManager.initLoader(0, null, this)
        }else{
            Toast.makeText(this, "Permissão negada.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onPause() {
        super.onPause()

        unbindService(this)
        mHandler.removeCallbacks(mThreadProgresso)
    }

    fun btnPlayClick(v: View){
        mHandler.removeCallbacks(mThreadProgresso)
        if (mMusica != null){
            mMP3Service?.play(mMusica!!)
            mHandler.post(mThreadProgresso)
        }
    }

    fun btnPauseClick(v: View){
        mMP3Service?.pause()
        mHandler.removeCallbacks(mThreadProgresso)
    }

    fun btnStopClick(v: View){
        mMP3Service?.stop()
        mHandler.removeCallbacks(mThreadProgresso)
        progressBar.progress = 0
        txtTempo.text = DateUtils.formatElapsedTime(0)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mMP3Service = (service as Mp3Binder).servico
        mHandler.post(mThreadProgresso)
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        mMP3Service = null
    }

    override fun onItemClick(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
        val cursor : Cursor  = adapterView?.getItemAtPosition(i) as Cursor
        val musica = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA))

        mHandler.removeCallbacks(mThreadProgresso)

        if (musica != null){
            mMP3Service?.stop()
            mMP3Service?.play(musica)
            mHandler.post(mThreadProgresso)
        }
    }

    private fun atualizarTela() {
        mMusica = mMP3Service?.getMusicaAtual()
        txtMusica.text = mMusica
        progressBar.max = mMP3Service!!.getTempoTotal()
        progressBar.progress = mMP3Service!!.getTempoDecorrido()
        txtTempo.text = DateUtils.formatElapsedTime((mMP3Service!!.getTempoDecorrido() / 1000).toLong())
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(this, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, colunas,
                MediaStore.Audio.AudioColumns.IS_MUSIC + " = 1", null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        mAdapter.swapCursor(data)
        listView.adapter = mAdapter
        listView.onItemClickListener = this
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter.swapCursor(null)
    }
}
