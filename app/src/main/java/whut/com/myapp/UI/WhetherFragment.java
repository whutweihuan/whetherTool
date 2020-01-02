package whut.com.myapp.UI;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.basic.Update;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import whut.com.myapp.MainActivity;
import whut.com.myapp.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhetherFragment extends Fragment {
    TextView tv_cityname;
    TextView tv_now_wendu;
    TextView tv_now_wind_dir;
    TextView tv_now_st;
    TextView tv_lastUpdateTime;

    SharedPreferences sp = null;

    public WhetherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_whether, container, false);
        sp = getActivity().getSharedPreferences("whether_data", MODE_PRIVATE);
        sp.edit().putString("first","1");
        sp.edit().commit();
        initView(view);

        return view;
    }

    public void initView(View view) {
        tv_cityname = (TextView) view.findViewById(R.id.cityname);
        tv_now_wendu = (TextView) view.findViewById(R.id.now_wendu);
        tv_now_st = (TextView)view.findViewById(R.id.now_st);
        tv_now_wind_dir = (TextView)view.findViewById(R.id.tv_now_wind_dir);
        tv_lastUpdateTime = (TextView) view.findViewById(R.id.tv_lastUpdateTime);
        initWhetherData();

    }

    public void initWhetherData(){
        String first = sp.getString("first","1");
        // 含有数据，不是第一次
        if(first.equals("0")){
            tv_now_wendu.setText(sp.getString("now_wendu","25"));
            tv_now_wind_dir.setText(sp.getString("now_wind_dir",""));
            tv_now_st.setText(sp.getString("now_st",""));
            tv_lastUpdateTime.setText(tv_lastUpdateTime.getText()+sp.getString("last_update_time",""));
            return;
        }

        // 第一次运行
        /**
         * 获得当前时间的状况
         */
        HeWeather.getWeatherNow(getActivity(), "CN101200101", Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable e) {
                Log.i("123", "Weather Now onError: ", e);
            }

            @Override
            public void onSuccess(Now dataObject) {
//                Log.i("weihuan", " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus())) {
                    //此时返回数据
                    NowBase now = dataObject.getNow();
                    Update update = dataObject.getUpdate();
                    tv_now_wendu.setText(now.getTmp());
                    tv_now_wind_dir.setText(now.getWind_dir());
                    tv_now_st.setText( now.getCond_txt());
                    tv_lastUpdateTime.setText(tv_lastUpdateTime.getText()+update.getLoc());

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("now_wendu",now.getTmp());
                    editor.putString("now_wind_dir",now.getWind_dir());
                    editor.putString("now_st",now.getCond_txt());
                    editor.putString("last_update_time",update.getLoc());
                    editor.putString("first","0");
                    editor.commit();


                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i("125", "failed code: " + code);
                }
            }
        });

        /**
         * 获得三天天气的预报
         */
        HeWeather.getWeatherForecast(getActivity(), "CN101200101", Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new  HeWeather.OnResultWeatherForecastBeanListener(){

            @Override
            public void onError(Throwable e) {
                Log.i("123", "Weather Now onError: ", e);
            }


            @Override
            public void onSuccess(Forecast dataObject) {
                Log.i("weihuan", " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus())) {
                    //此时返回数据
//                    NowBase now = dataObject.getNow();
//                    Update update = dataObject.getUpdate();
//                    tv_now_wendu.setText(now.getTmp());
//                    tv_now_wind_dir.setText(now.getWind_dir());
//                    tv_now_st.setText( now.getCond_txt());
//                    tv_lastUpdateTime.setText(tv_lastUpdateTime.getText()+update.getLoc());



//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString("now_wendu",now.getTmp());
//                    editor.putString("now_wind_dir",now.getWind_dir());
//                    editor.putString("now_st",now.getCond_txt());
//                    editor.putString("last_update_time",update.getLoc());
//                    editor.putString("first","0");
//                    editor.commit();


                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i("125", "failed code: " + code);
                }
            }

        });

        /**
         * 获得每小时的天气状况
         */
        HeWeather.getWeatherHourly(getActivity(), "CN101200101", Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherHourlyBeanListener() {
            @Override
            public void onError(Throwable e) {
                Log.i("Hourly Error", e.toString());
            }

            @Override
            public void onSuccess(Hourly dataObject) {
                Log.i("weihuan2", " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus())) {
                    //此时返回数据


                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i("125", "failed code: " + code);
                }
            }
        });


    }
}
