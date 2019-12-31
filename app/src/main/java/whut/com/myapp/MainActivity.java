package whut.com.myapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.heweather.plugin.view.HeContent;
import com.heweather.plugin.view.HorizonView;
import com.heweather.plugin.view.VerticalView;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import whut.com.myapp.UI.MainUIActivity;


public class MainActivity extends AppCompatActivity {

    TextView tv_cityname;
    TextView tv_now_wendu;
    TextView tv_now_shidu;
    TextView tv_now_st;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        HeConfig.init("HE1912301700331680", "744a3fe9e3634724967a741e8df9d161");
        HeConfig.switchToFreeServerNode();
        Intent intent = new Intent(this, MainUIActivity.class);
        startActivity(intent);


        HeWeather.getWeatherNow(MainActivity.this, "CN101200101", Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable e) {
                Log.i("123", "Weather Now onError: ", e);
            }

            @Override
            public void onSuccess(Now dataObject) {
                Log.i("124", " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus())) {
                    //此时返回数据
                    NowBase now = dataObject.getNow();
                    tv_now_wendu.setText(now.getTmp()+"°");
                    tv_now_shidu.setText("湿度: "+now.getHum());
                    tv_now_st.setText( now.getCond_txt());

                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i("125", "failed code: " + code);
                }
            }
        });

    }


    public void init() {
        tv_cityname = (TextView) findViewById(R.id.cityname);
        tv_now_wendu = (TextView) findViewById(R.id.now_wendu);
        tv_now_st = (TextView)findViewById(R.id.now_st);
        tv_now_shidu = (TextView)findViewById(R.id.now_shidu);

    }
}
