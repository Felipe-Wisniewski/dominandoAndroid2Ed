package dominando.android.navegacao;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class TelaPagerActivity extends AppCompatActivity {

    AbasPagerAdapter mAbasPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_pager);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mAbasPagerAdapter = new AbasPagerAdapter(this, getSupportFragmentManager());
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(mAbasPagerAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
