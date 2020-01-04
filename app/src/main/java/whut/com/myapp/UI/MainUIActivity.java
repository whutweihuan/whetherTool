package whut.com.myapp.UI;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import whut.com.myapp.R;
import whut.com.myapp.adapter.MyFragmentPagerAdapter;


public class MainUIActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

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

    @Override
    public void handleResult(Result rawResult) {

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK){
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .title("退出")
                    .content("确认退出应用吗?")
                    .positiveText("确认")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            return ;
                        }
                    });

                    MaterialDialog dialog = builder.build();
                    dialog.show();
                    return false;
        }


        return super.onKeyUp(keyCode, event);
    }
}
