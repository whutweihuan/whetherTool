package whut.com.myapp.UI;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.Air;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowCity;
import interfaces.heweather.com.interfacesmodule.bean.basic.Update;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
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
    // 用于缓存保存数据的字符串索引
    private final String NOW_STAUS = "NOW_STATUS";
    private final String HOURLY = "HOURLY";
    private final String MULTDAY = "MULTDAY";
    private final String LIFECOC = "LIFECOC";
    private final String AIRCOC = "AIRCOC";

    Map<String, Integer> dic = new HashMap<>();

    SharedPreferences sp = null;
    SharedPreferences.Editor editor = null;


    // 实时天气状况
    TextView tv_cityname;
    TextView tv_now_wendu;
    TextView tv_now_wind_dir;
    TextView tv_now_st;
    TextView tv_lastUpdateTime;

    // 整点时间数据
    TextView tv_zd_time1;
    TextView tv_zd_time2;
    TextView tv_zd_time3;
    TextView tv_zd_time4;
    TextView tv_zd_time5;
    TextView tv_zd_time6;

    // 整点天气状况数据
    ImageView iv_zd_time1;
    ImageView iv_zd_time2;
    ImageView iv_zd_time3;
    ImageView iv_zd_time4;
    ImageView iv_zd_time5;
    ImageView iv_zd_time6;

    // 整点温度数据
    TextView tv_zd_wendu1;
    TextView tv_zd_wendu2;
    TextView tv_zd_wendu3;
    TextView tv_zd_wendu4;
    TextView tv_zd_wendu5;
    TextView tv_zd_wendu6;

    // 多天天气数据
    ImageView iv_mtd1;
    ImageView iv_mtd2;
    ImageView iv_mtd3;

    TextView tv_d1_maxWendu;
    TextView tv_d2_maxWendu;
    TextView tv_d3_maxWendu;

    TextView tv_d1_minWendu;
    TextView tv_d2_minWendu;
    TextView tv_d3_minWendu;

    TextView tv_mtd_cond1;
    TextView tv_mtd_cond2;
    TextView tv_mtd_cond3;

    // 生活指数
    TextView tv_tips_sport;
    TextView tv_tip_zwx;
    TextView tv_tips_cloth;
    TextView tv_tip_fever;

    // 空气质量

    ArcProgress arc_progress;
    TextView tv_air_pm10;
    TextView tv_air_pm25;
    TextView tv_air_no2;
    TextView tv_air_so2;
    TextView tv_air_o3;
    TextView tv_air_co;

    public WhetherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_whether, container, false);
        sp = getActivity().getSharedPreferences("whether_data", MODE_PRIVATE);
        editor = sp.edit();
        sp.edit().putString("first", "1");
        sp.edit().commit();
        initView(view);

        return view;
    }

    public void initView(View view) {
        dic.put("阴", R.drawable.more_cloud);
        dic.put("晴", R.drawable.sun_icon);
        dic.put("雨", R.drawable.rain);
        dic.put("小雨", R.drawable.rain);

        tv_cityname = (TextView) view.findViewById(R.id.cityname);
        tv_now_wendu = (TextView) view.findViewById(R.id.now_wendu);
        tv_now_st = (TextView) view.findViewById(R.id.now_st);
        tv_now_wind_dir = (TextView) view.findViewById(R.id.tv_now_wind_dir);
        tv_lastUpdateTime = (TextView) view.findViewById(R.id.tv_lastUpdateTime);

        tv_zd_time1 = (TextView) view.findViewById(R.id.tv_zd_time1);
        tv_zd_time2 = (TextView) view.findViewById(R.id.tv_zd_time2);
        tv_zd_time3 = (TextView) view.findViewById(R.id.tv_zd_time3);
        tv_zd_time4 = (TextView) view.findViewById(R.id.tv_zd_time4);
        tv_zd_time5 = (TextView) view.findViewById(R.id.tv_zd_time5);
        tv_zd_time6 = (TextView) view.findViewById(R.id.tv_zd_time6);

        iv_zd_time1 = (ImageView) view.findViewById(R.id.iv_zd_time1);
        iv_zd_time2 = (ImageView) view.findViewById(R.id.iv_zd_time2);
        iv_zd_time3 = (ImageView) view.findViewById(R.id.iv_zd_time3);
        iv_zd_time4 = (ImageView) view.findViewById(R.id.iv_zd_time4);
        iv_zd_time5 = (ImageView) view.findViewById(R.id.iv_zd_time5);
        iv_zd_time6 = (ImageView) view.findViewById(R.id.iv_zd_time6);

        tv_zd_wendu1 = (TextView) view.findViewById(R.id.tv_zd_wendu1);
        tv_zd_wendu2 = (TextView) view.findViewById(R.id.tv_zd_wendu2);
        tv_zd_wendu3 = (TextView) view.findViewById(R.id.tv_zd_wendu3);
        tv_zd_wendu4 = (TextView) view.findViewById(R.id.tv_zd_wendu4);
        tv_zd_wendu5 = (TextView) view.findViewById(R.id.tv_zd_wendu5);
        tv_zd_wendu6 = (TextView) view.findViewById(R.id.tv_zd_wendu6);

        tv_d1_maxWendu = (TextView) view.findViewById(R.id.tv_d1_maxWendu);
        tv_d2_maxWendu = (TextView) view.findViewById(R.id.tv_d2_maxWendu);
        tv_d3_maxWendu = (TextView) view.findViewById(R.id.tv_d3_maxWendu);

        tv_d1_minWendu = (TextView) view.findViewById(R.id.tv_d1_minWendu);
        tv_d2_minWendu = (TextView) view.findViewById(R.id.tv_d2_minWendu);
        tv_d3_minWendu = (TextView) view.findViewById(R.id.tv_d3_minWendu);

        iv_mtd1 = (ImageView) view.findViewById(R.id.iv_mtd1);
        iv_mtd2 = (ImageView) view.findViewById(R.id.iv_mtd2);
        iv_mtd3 = (ImageView) view.findViewById(R.id.iv_mtd3);

        tv_mtd_cond1 = (TextView) view.findViewById(R.id.tv_mtd_cond1);
        tv_mtd_cond2 = (TextView) view.findViewById(R.id.tv_mtd_cond2);
        tv_mtd_cond3 = (TextView) view.findViewById(R.id.tv_mtd_cond3);


        tv_tips_sport = (TextView) view.findViewById(R.id.tv_tips_sport);
        tv_tip_zwx = (TextView) view.findViewById(R.id.tv_tip_zwx);
        tv_tips_cloth = (TextView) view.findViewById(R.id.tv_tips_cloth);
        tv_tip_fever = (TextView) view.findViewById(R.id.tv_tip_fever);

        arc_progress = (ArcProgress) view.findViewById(R.id.arc_progress);

        tv_air_pm10 = (TextView) view.findViewById(R.id.tv_air_pm10);
        tv_air_pm25 = (TextView) view.findViewById(R.id.tv_air_pm25);
        tv_air_no2 = (TextView) view.findViewById(R.id.tv_air_no2);
        tv_air_so2 = (TextView) view.findViewById(R.id.tv_air_so2);
        tv_air_o3 = (TextView) view.findViewById(R.id.tv_air_o3);
        tv_air_co = (TextView) view.findViewById(R.id.tv_air_co);

        initWhetherData();

    }

    public void initWhetherData() {
        final String first = sp.getString("first", "1");
        // 含有数据，不是第一次
        if (first.equals("0")) {
            try{
                Gson gson = new Gson();
                String json = sp.getString(NOW_STAUS,"");
                Now now = gson.fromJson(json,Now.class);
                setNowView(now);

                json = sp.getString(HOURLY,"");
                Hourly hourly = gson.fromJson(json,Hourly.class);
                setHourView(hourly);

                json = sp.getString(MULTDAY,"");
                Forecast forecast = gson.fromJson(json,Forecast.class);
                setMtdView(forecast);

                json = sp.getString(LIFECOC,"");
                Lifestyle lifestyle = gson.fromJson(json,Lifestyle.class);
                setLifeView(lifestyle);

                json = sp.getString(AIRCOC,"");
                AirNow airNow = gson.fromJson(json,AirNow.class);
                setAirView(airNow);

            }catch (Exception e){
                e.printStackTrace();
            }

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
                    editor.putString(NOW_STAUS, new Gson().toJson(dataObject));
                    editor.commit();
                    setNowView(dataObject);
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
        HeWeather.getWeatherForecast(getActivity(), "CN101200101", Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherForecastBeanListener() {

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
                    editor.putString(MULTDAY, new Gson().toJson(dataObject));
                    editor.commit();

                    setMtdView(dataObject);

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

//                Log.i("weihuan2", " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus())) {
                    //此时返回数据
                    // 保存数据
                    String json = new Gson().toJson(dataObject);
                    editor.putString(HOURLY, json);
                    editor.commit();

                    setHourView(dataObject);

                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i("125", "failed code: " + code);
                }
            }
        });


        /**
         * 获得生活质量数据
         */
        HeWeather.getWeatherLifeStyle(getActivity(), "CN101200101", Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherLifeStyleBeanListener() {
            @Override
            public void onError(Throwable e) {
                Log.i("123", "Weather life onError: ", e);
            }

            @Override
            public void onSuccess(Lifestyle dataObject) {
                Log.i("weihuan", " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus())) {
                    //此时返回数据
                    editor.putString(LIFECOC, new Gson().toJson(dataObject));
                    editor.commit();

                    setLifeView(dataObject);
                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i("125", "failed code: " + code);
                }
            }
        });


        /**
         * 获得空气质量数据
         */
        HeWeather.getAirNow(getActivity(), "CN101200101", Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultAirNowBeansListener() {
            @Override
            public void onError(Throwable e) {
                Log.i("123", "Weather life onError: ", e);
            }

            @Override
            public void onSuccess(AirNow dataObject) {
                Log.i("weihuan", " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus())) {
                    //此时返回数据
                    editor.putString(AIRCOC, new Gson().toJson(dataObject));
                    editor.commit();

                    setAirView(dataObject);

                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i("125", "failed code: " + code);
                }
            }
        });

    }

    public void setAirView(AirNow dataObject) {
        AirNowCity airNowCity = dataObject.getAir_now_city();
        tv_air_pm10.setText(airNowCity.getPm10());
        tv_air_pm25.setText(airNowCity.getPm25());
        tv_air_no2.setText(airNowCity.getNo2());
        tv_air_so2.setText(airNowCity.getNo2());
        tv_air_o3.setText(airNowCity.getO3());
        tv_air_co.setText(airNowCity.getCo());

        arc_progress.setBottomText(airNowCity.getQlty());
        arc_progress.setMax(500);
        int p = Integer.parseInt(airNowCity.getAqi());
        arc_progress.setProgress(p);
        arc_progress.setSuffixText("");
    }

    public void setLifeView(Lifestyle dataObject) {
        //1-衣服
        //3-运动
        //5-紫外线
        //2-感冒
        List<LifestyleBase> lifestyles = dataObject.getLifestyle();
        tv_tip_fever.setText(lifestyles.get(2).getBrf());
        tv_tip_zwx.setText(lifestyles.get(5).getBrf());
        tv_tips_cloth.setText(lifestyles.get(1).getBrf());
        tv_tips_sport.setText(lifestyles.get(3).getBrf());
    }

    public void setHourView(Hourly dataObject) {
        List<HourlyBase> hourlyBases = dataObject.getHourly();
        // 整点
        tv_zd_time1.setText(hourlyBases.get(0).getTime().substring(11));
        tv_zd_time2.setText(hourlyBases.get(1).getTime().substring(11));
        tv_zd_time3.setText(hourlyBases.get(2).getTime().substring(11));
        tv_zd_time4.setText(hourlyBases.get(3).getTime().substring(11));
        tv_zd_time5.setText(hourlyBases.get(4).getTime().substring(11));
        tv_zd_time6.setText(hourlyBases.get(5).getTime().substring(11));


        Glide.with(getActivity()).load(dic.get(hourlyBases.get(0).getCond_txt())).into(iv_zd_time1);
        Glide.with(getActivity()).load(dic.get(hourlyBases.get(1).getCond_txt())).into(iv_zd_time2);
        Glide.with(getActivity()).load(dic.get(hourlyBases.get(2).getCond_txt())).into(iv_zd_time3);
        Glide.with(getActivity()).load(dic.get(hourlyBases.get(3).getCond_txt())).into(iv_zd_time4);
        Glide.with(getActivity()).load(dic.get(hourlyBases.get(4).getCond_txt())).into(iv_zd_time5);
        Glide.with(getActivity()).load(dic.get(hourlyBases.get(5).getCond_txt())).into(iv_zd_time6);

        tv_zd_wendu1.setText(hourlyBases.get(0).getTmp() + "℃");
        tv_zd_wendu2.setText(hourlyBases.get(1).getTmp() + "℃");
        tv_zd_wendu3.setText(hourlyBases.get(2).getTmp() + "℃");
        tv_zd_wendu4.setText(hourlyBases.get(3).getTmp() + "℃");
        tv_zd_wendu5.setText(hourlyBases.get(4).getTmp() + "℃");
        tv_zd_wendu6.setText(hourlyBases.get(5).getTmp() + "℃");
    }

    public void setMtdView(Forecast dataObject) {
        List<ForecastBase> forecastBases = dataObject.getDaily_forecast();
        Glide.with(getActivity()).load(dic.get(forecastBases.get(0).getCond_txt_d())).into(iv_mtd1);
        Glide.with(getActivity()).load(dic.get(forecastBases.get(1).getCond_txt_d())).into(iv_mtd2);
        Glide.with(getActivity()).load(dic.get(forecastBases.get(2).getCond_txt_d())).into(iv_mtd3);

        tv_d1_maxWendu.setText(forecastBases.get(0).getTmp_max() + "℃");
        tv_d2_maxWendu.setText(forecastBases.get(1).getTmp_max() + "℃");
        tv_d3_maxWendu.setText(forecastBases.get(2).getTmp_max() + "℃");

        tv_d1_minWendu.setText(forecastBases.get(0).getTmp_min() + "℃");
        tv_d2_minWendu.setText(forecastBases.get(1).getTmp_min() + "℃");
        tv_d3_minWendu.setText(forecastBases.get(2).getTmp_min() + "℃");

        tv_mtd_cond1.setText(forecastBases.get(0).getCond_txt_d());
        tv_mtd_cond2.setText(forecastBases.get(1).getCond_txt_d());
        tv_mtd_cond3.setText(forecastBases.get(2).getCond_txt_d());
    }

    public void setNowView(Now dataObject){
        NowBase now = dataObject.getNow();
        Update update = dataObject.getUpdate();
        tv_now_wendu.setText(now.getTmp());
        tv_now_wind_dir.setText(now.getWind_dir());
        tv_now_st.setText(now.getCond_txt());
        tv_lastUpdateTime.setText(tv_lastUpdateTime.getText() + update.getLoc());
    }

}
