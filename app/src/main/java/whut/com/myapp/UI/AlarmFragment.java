package whut.com.myapp.UI;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;


import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import whut.com.myapp.R;
import whut.com.myapp.adapter.AlarmAdapter;
import whut.com.myapp.bean.AddAlarmItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment implements View.OnClickListener {
    private Button btn_add_alarm;
    private AlarmAdapter alarmAdapter;
    private ListView lv_alarm_list;
    private TextView tv_current_time;
    private TimePicker time_picker;


    // 缓存
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String TAG = "AlarmList";


    Handler handler = new Handler();
    Runnable timedTask = new Runnable() {
        @Override
        public void run() {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String format = sdf.format(date);

            tv_current_time.setText(format);
            handler.postDelayed(timedTask, 1000);
        }
    };

    public AlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        sp = getActivity().getSharedPreferences("Alarm", Context.MODE_PRIVATE);
        editor = sp.edit();

        initView(view);


        return view;
    }


    public void initView(View view) {
        btn_add_alarm = (Button) view.findViewById(R.id.btn_add_alarm);
        btn_add_alarm.setOnClickListener(this);
        lv_alarm_list = (ListView) view.findViewById(R.id.lv_alarm_list);
        tv_current_time = (TextView) view.findViewById(R.id.tv_current_time);

        // 采用 handler 刷新时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String format = sdf.format(date);
        tv_current_time.setText(format);
        handler.post(timedTask);


//        ArrayList<AddAlarmItem> addAlarmlist  = new ArrayList<>();
//        addAlarmlist.add(new AddAlarmItem("06:10",1));
//        addAlarmlist.add(new AddAlarmItem("12:10",0));
//        addAlarmlist.add(new AddAlarmItem("21:10",1));
//        String json = new Gson().toJson(addAlarmlist);
//
//        editor.putString(TAG,json);
//        editor.commit();

        AddAlarmItem[] temp = new Gson().fromJson(sp.getString(TAG, ""), AddAlarmItem[].class);
        List<AddAlarmItem> addAlarmlist = Arrays.asList(temp);

        alarmAdapter = new AlarmAdapter(addAlarmlist, getActivity());
        lv_alarm_list.setAdapter(alarmAdapter);

        time_picker = (TimePicker)view.findViewById(R.id.time_picker);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 添加闹钟
            case R.id.btn_add_alarm:
                selectTime();

                break;
        }

    }

    public void selectTime(){
        time_picker.setIs24HourView(true);
        // 初始化时间选择器的小时
        //noinspection deprecation
        time_picker.setCurrentHour(0);
        // 初始化时间选择器的分钟
        //noinspection deprecation
        time_picker.setCurrentMinute(0);

        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                // 保存闹钟实例的小时
//                mAlarmClock.setHour(hourOfDay);
//                // 保存闹钟实例的分钟
//                mAlarmClock.setMinute(minute);
//                // 计算倒计时显示
//                displayCountDown();


            }

        });
    }


}
