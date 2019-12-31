package whut.com.myapp.UI;


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
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import whut.com.myapp.MainActivity;
import whut.com.myapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhetherFragment extends Fragment {
    TextView tv_cityname;
    TextView tv_now_wendu;
    TextView tv_now_shidu;
    TextView tv_now_st;

    public WhetherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_whether, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {
        tv_cityname = (TextView) view.findViewById(R.id.cityname);
        tv_now_wendu = (TextView) view.findViewById(R.id.now_wendu);
        tv_now_st = (TextView)view.findViewById(R.id.now_st);
        tv_now_shidu = (TextView)view.findViewById(R.id.now_shidu);

        HeWeather.getWeatherNow(getActivity(), "CN101200101", Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {
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
}
