package whut.com.myapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HeConfig.init("HE1912311844551513", "b45967b119604ef485b69c73c497a5c3");
//        HeConfig.switchToFreeServerNode();
        Intent intent = new Intent(this, MainUIActivity.class);
        startActivity(intent);
        finish();
    }
}
