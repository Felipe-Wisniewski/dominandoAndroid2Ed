package dominando.android.webview;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.webview);
        mWebView.loadUrl("http://www.nglauber.com.br");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view, url);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else{
            super.onBackPressed();
        }
    }
}
