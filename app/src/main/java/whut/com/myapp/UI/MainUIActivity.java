package whut.com.myapp.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import whut.com.myapp.R;
import whut.com.myapp.adapter.MyFragmentPagerAdapter;


public class MainUIActivity extends AppCompatActivity {

    ViewPager vp_main;
    MyFragmentPagerAdapter mAdapter =null;
    ToolBarRadioGroup radiogroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);
        mAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager());
        initView();


//        //android6.0及以上检查权限
//        if(Build.VERSION.SDK_INT>=23){
//            new CheckPermissionsActivity(this);
//        }
    }
    public void initView(){
        vp_main=(ViewPager)findViewById(R.id.vp_main);
        radiogroup=(ToolBarRadioGroup) findViewById(R.id.radiogroup);

        vp_main.setAdapter(mAdapter);
        radiogroup.setViewPager(vp_main);
    }
}
