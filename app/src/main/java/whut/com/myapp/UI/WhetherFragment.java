package whut.com.myapp.UI;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNowCity;
import interfaces.heweather.com.interfacesmodule.bean.basic.Update;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import whut.com.myapp.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhetherFragment extends Fragment implements View.OnClickListener {
    // 用于缓存保存数据的字符串索引
    private final String NOW_STAUS = "NOW_STATUS";
    private final String HOURLY = "HOURLY";
    private final String MULTDAY = "MULTDAY";
    private final String LIFECOC = "LIFECOC";
    private final String AIRCOC = "AIRCOC";

    // 映射天天气图片
    Map<String, Integer> dic = new HashMap<>();

    // 缓存
    SharedPreferences sp = null;
    SharedPreferences.Editor editor = null;

    // 下拉刷新
    SwipeRefreshLayout swp_rlt;

    // 保存地名 CN 码（和风天气地名的ID）
    String locationCN = "CN101200101";


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

    RelativeLayout rlt_tips_sport;
    RelativeLayout rlt_tip_zwx;
    RelativeLayout rlt_tips_cloth;
    RelativeLayout rlt_tip_fever;


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

        initView(view);


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,new String[]{"1","2","3"});
//        DialogPlus dialog = DialogPlus.newDialog(getActivity())
//                .setAdapter(adapter)
//                .setContentHolder(new ListHolder())
//                .setOnItemClickListener(new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
//                    }
//                })
//                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
//                .create();
//        dialog.show();


        return view;
    }

    public void initView(View view) {
        dic.put("阴", R.drawable.more_cloud);
        dic.put("多云", R.drawable.more_cloud);
        dic.put("晴间多云", R.drawable.more_cloud);
        dic.put("少云", R.drawable.more_cloud);
        dic.put("晴", R.drawable.sun_zwx);
        dic.put("雨", R.drawable.rain);
        dic.put("小雨", R.drawable.rain);
        dic.put("大雨", R.drawable.rain);
        dic.put("中雨", R.drawable.rain);
        dic.put("极端降雨", R.drawable.rain);
        dic.put("毛毛雨/细雨", R.drawable.rain);
        dic.put("暴雨", R.drawable.rain);
        dic.put("大暴雨", R.drawable.rain);
        dic.put("特大暴雨", R.drawable.rain);
        dic.put("冻雨", R.drawable.rain);
        dic.put("小到中雨", R.drawable.rain);
        dic.put("中到大雨", R.drawable.rain);
        dic.put("大到暴雨", R.drawable.rain);
        dic.put("暴雨到大暴雨", R.drawable.rain);
        dic.put("雪", R.drawable.snow);
        dic.put("小雪", R.drawable.snow);
        dic.put("中雪", R.drawable.snow);
        dic.put("大雪", R.drawable.snow);
        dic.put("暴雪", R.drawable.snow);
        dic.put("雨雪天气", R.drawable.snow);
        dic.put("阵雨夹雪", R.drawable.snow);
        dic.put("雨夹雪", R.drawable.snow);
        dic.put("阵雪", R.drawable.snow);
        dic.put("小到中雪", R.drawable.snow);
        dic.put("中到大雪", R.drawable.snow);
        dic.put("大到暴雪", R.drawable.snow);
        dic.put("霾", R.drawable.frog);
        dic.put("薄雾", R.drawable.frog);
        dic.put("雾", R.drawable.frog);
        dic.put("浓雾", R.drawable.frog);
        dic.put("强浓雾", R.drawable.frog);
        dic.put("中度霾", R.drawable.frog);
        dic.put("重度霾", R.drawable.frog);
        dic.put("严重霾", R.drawable.frog);
        dic.put("大雾", R.drawable.frog);
        dic.put("特强浓雾", R.drawable.frog);


        swp_rlt = (SwipeRefreshLayout) view.findViewById(R.id.swp_rlt);
        swp_rlt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                editor.putString("first", "yes");
                editor.commit();
                initWhetherData();
                swp_rlt.setRefreshing(false);
            }
        });

        tv_cityname = (TextView) view.findViewById(R.id.cityname);
        tv_now_wendu = (TextView) view.findViewById(R.id.now_wendu);
        tv_now_st = (TextView) view.findViewById(R.id.now_st);
        tv_now_wind_dir = (TextView) view.findViewById(R.id.tv_now_wind_dir);
        tv_lastUpdateTime = (TextView) view.findViewById(R.id.tv_lastUpdateTime);

        tv_cityname.setOnClickListener(this);

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

        rlt_tips_sport = (RelativeLayout) view.findViewById(R.id.rlt_tips_sport);
        rlt_tip_zwx = (RelativeLayout) view.findViewById(R.id.rlt_tips_zwx);
        rlt_tips_cloth = (RelativeLayout) view.findViewById(R.id.rlt_tips_cloth);
        rlt_tip_fever = (RelativeLayout) view.findViewById(R.id.rlt_tips_fever);

        rlt_tips_sport.setOnClickListener(this);
        rlt_tip_zwx.setOnClickListener(this);
        rlt_tips_cloth.setOnClickListener(this);
        rlt_tip_fever.setOnClickListener(this);

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
        final String first = sp.getString("first", "yes");
        // 含有数据，不是第一次
        if (first.equals("no")) {
            try {
                Gson gson = new Gson();
                String json = sp.getString(NOW_STAUS, "");
                Now now = gson.fromJson(json, Now.class);
                setNowView(now);

                json = sp.getString(HOURLY, "");
                Hourly hourly = gson.fromJson(json, Hourly.class);
                setHourView(hourly);

                json = sp.getString(MULTDAY, "");
                Forecast forecast = gson.fromJson(json, Forecast.class);
                setMtdView(forecast);

                json = sp.getString(LIFECOC, "");
                Lifestyle lifestyle = gson.fromJson(json, Lifestyle.class);
                setLifeView(lifestyle);

                json = sp.getString(AIRCOC, "");
                AirNow airNow = gson.fromJson(json, AirNow.class);
                setAirView(airNow);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }

        // 第一次运行，现在不是了
        editor.putString("first", "no");
        editor.commit();

        /**
         * 获得当前时间的状况
         */
        HeWeather.getWeatherNow(getActivity(), locationCN, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable e) {
                Log.i("123", "Weather Now onError: ", e);
            }

            @Override
            public void onSuccess(Now dataObject) {
                Log.i("weihuan", " Weather Now onSuccess: " + new Gson().toJson(dataObject));
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
        HeWeather.getWeatherForecast(getActivity(), locationCN, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherForecastBeanListener() {

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
        HeWeather.getWeatherHourly(getActivity(), locationCN, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherHourlyBeanListener() {
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
        HeWeather.getWeatherLifeStyle(getActivity(), locationCN, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherLifeStyleBeanListener() {
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
        HeWeather.getAirNow(getActivity(), locationCN, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultAirNowBeansListener() {
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


        Glide.with(getActivity()).load(dic.get(hourlyBases.get(0).getCond_txt())).error(R.drawable.more_cloud).into(iv_zd_time1);
        Glide.with(getActivity()).load(dic.get(hourlyBases.get(1).getCond_txt())).error(R.drawable.more_cloud).into(iv_zd_time2);
        Glide.with(getActivity()).load(dic.get(hourlyBases.get(2).getCond_txt())).error(R.drawable.more_cloud).into(iv_zd_time3);
        Glide.with(getActivity()).load(dic.get(hourlyBases.get(3).getCond_txt())).error(R.drawable.more_cloud).into(iv_zd_time4);
        Glide.with(getActivity()).load(dic.get(hourlyBases.get(4).getCond_txt())).error(R.drawable.more_cloud).into(iv_zd_time5);
        Glide.with(getActivity()).load(dic.get(hourlyBases.get(5).getCond_txt())).error(R.drawable.more_cloud).into(iv_zd_time6);

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

    public void setNowView(Now dataObject) {
        NowBase now = dataObject.getNow();
        Update update = dataObject.getUpdate();
        tv_now_wendu.setText(now.getTmp());
        tv_now_wind_dir.setText(now.getWind_dir());
        tv_now_st.setText(now.getCond_txt());
        tv_cityname.setText(dataObject.getBasic().getLocation());
//        Date currentTime = Calendar.getInstance().getTime();
//        tv_lastUpdateTime.setText(currentTime.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        tv_lastUpdateTime.setText(sdf.format(new Date()));

    }

    @Override
    public void onClick(View v) {
        //1-衣服
        //3-运动
        //5-紫外线
        //2-感冒

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String json = sp.getString(LIFECOC, "");
        Lifestyle lifestyle = new Gson().fromJson(json, Lifestyle.class);
        List<LifestyleBase> lifes = lifestyle.getLifestyle();
        switch (v.getId()) {
            case R.id.rlt_tips_cloth:
                builder.setTitle("衣物");
                builder.setPositiveButton("确定", null);
                builder.setMessage(lifes.get(1).getTxt());
                builder.show();
                break;
            case R.id.rlt_tips_sport:
                builder.setTitle("运动");
                builder.setPositiveButton("确定", null);
                builder.setMessage(lifes.get(3).getTxt());
                builder.show();
                break;
            case R.id.rlt_tips_zwx:
                builder.setTitle("紫外线");
                builder.setPositiveButton("确定", null);
                builder.setMessage(lifes.get(5).getTxt());
                builder.show();
                break;
            case R.id.rlt_tips_fever:
                builder.setTitle("感冒");
                builder.setPositiveButton("确定", null);
                builder.setMessage(lifes.get(2).getTxt());
                builder.show();
                break;

            // 选择城市
            case R.id.cityname:
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        R.layout.whether_city_item,
                        new String[]{"自动定位", "上海", "北京", "深圳", "广州", "成都", "杭州", "重庆", "武汉", "苏州", "西安", "天津", "南京", "郑州", "长沙", "沈阳", "青岛", "宁波", "无锡"
                        });
                final String allCN[] = new String[]{"", "CN101020100", "CN101010100", "CN101280601", "CN101280101", "CN101270101", "CN101210101", "CN101040100", "CN101200101", "CN101190401", "CN101050311", "CN101030100", "CN101190101", "CN101180101", "CN101250101", "CN101070101", "CN101120201", "CN101210401", "CN101190201"};

                // 获取屏幕宽度
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = (int) (displayMetrics.widthPixels * 0.6);

                DialogPlus dialog = DialogPlus.newDialog(getActivity())
                        .setAdapter(adapter)
                        .setContentBackgroundResource(R.color.touming_hei)
                        .setGravity(Gravity.CENTER)
                        .setContentWidth(width)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                if (position == 0) {
                                    locationByGPS();
                                    dialog.dismiss();
                                    return;
                                }
                                locationCN = allCN[position];
                                editor.putString("first", "yes");
                                editor.commit();
                                initWhetherData();
                                dialog.dismiss();
                                return;
                            }
                        })
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();

                break;
        }
    }

    public void locationByGPS() {
        HeWeather.getSearch(getActivity(), new HeWeather.OnResultSearchBeansListener() {
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "没有定位权限", Toast.LENGTH_SHORT);
                return;
            }

            @Override
            public void onSuccess(Search dataObject) {
                if (Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus())) {
                    locationCN = dataObject.getBasic().get(0).getCid();
                    editor.putString("first", "yes");
                    editor.commit();
                    initWhetherData();

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
