package dominando.android.http;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LivrosListFragment extends Fragment {
    LivrosTask mTask;
    List<Livro> mLivros;
    ListView mListView;
    TextView mTextMensagem;
    ProgressBar mProgressBar;
    ArrayAdapter<Livro> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_livros_list, null);

        mTextMensagem = layout.findViewById(R.id.empty_fll);
        mProgressBar = layout.findViewById(R.id.progressBar_fll);
        mListView = layout.findViewById(R.id.listView_fll);
        mListView.setEmptyView(mTextMensagem);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mLivros == null){
            mLivros = new ArrayList<Livro>();
        }

        mAdapter = new LivrosListAdapter(getActivity(), mLivros);
        mListView.setAdapter(mAdapter);

        if(mTask == null){
            if(LivroHttp.temConexao(getActivity())) {
                iniciarDownload();
            }else {
                mTextMensagem.setText("Sem conexão");
            }
        }else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            exibirProgress(true);
        }
    }

    private void iniciarDownload() {
        if(mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
            mTask = new LivrosTask();
            mTask.execute();
        }
    }

    private void exibirProgress(boolean exibir) {
        if(exibir){
            mTextMensagem.setText("Baixando informações dos livros...");
        }
        mTextMensagem.setVisibility(exibir ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    //========================================= CLASS ==============================================
    private class LivrosTask extends AsyncTask<Void, Void, List<Livro>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exibirProgress(true);
        }

        @Override
        protected List<Livro> doInBackground(Void... strings) {
            return LivroHttp.carregarLivrosJson();
        //  return LivroHttp.carregarLivrosXml();
        }

        @Override
        protected void onPostExecute(List<Livro> livros) {
            super.onPostExecute(livros);
            exibirProgress(false);
            if(livros != null){
                mLivros.clear();
                mLivros.addAll(livros);
                mAdapter.notifyDataSetChanged();
            }else {
                mTextMensagem.setText("Falha ao obter livros");
            }
        }
    }
}
