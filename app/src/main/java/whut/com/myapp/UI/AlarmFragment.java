package whut.com.myapp.UI;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;


import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import whut.com.myapp.R;
import whut.com.myapp.broadcast.RingReceived;
import whut.com.myapp.service.RepetitionService;
import whut.com.myapp.adapter.AlarmAdapter;
import whut.com.myapp.bean.AddAlarmItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment implements View.OnClickListener {
    private Button btn_add_alarm;

    private ListView lv_alarm_list;
    private TextView tv_current_time;
    private TimePicker time_picker;
    private Button btn_clean_alarm;

    private static int  requestid = 1;

    //
    static List<AddAlarmItem> addAlarmlist = new ArrayList<>();
    static AlarmAdapter alarmAdapter;


    // 选择时间确定取消
    private Button btn_picktime_cancel;
    private Button btn_picktime_ok;

    // 选择时间布局
    RelativeLayout rlt_time_picker;
    RelativeLayout rlt_alarm_item_list;

//    int pickStatus = 0;

    // 缓存
    static SharedPreferences sp;
    static SharedPreferences.Editor editor;
    static String TAG = "AlarmList";

    // 选择的时间
    String pickTime = "00:00";


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

        // 注册广播
        RingReceived receiver = new RingReceived();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.myapp.RING");
        getActivity().registerReceiver(receiver, filter);

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
        btn_clean_alarm = (Button) view.findViewById(R.id.btn_clean_alarm);
        btn_clean_alarm.setOnClickListener(this);

        // 采用 handler 刷新时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String format = sdf.format(date);
        tv_current_time.setText(format);
        handler.post(timedTask);

        btn_picktime_cancel = (Button) view.findViewById(R.id.btn_picktime_cancel);
        btn_picktime_ok = (Button) view.findViewById(R.id.btn_picktime_ok);
        btn_picktime_cancel.setOnClickListener(this);
        btn_picktime_ok.setOnClickListener(this);

        rlt_time_picker = (RelativeLayout) view.findViewById(R.id.rlt_time_picker);
        rlt_alarm_item_list = (RelativeLayout) view.findViewById(R.id.rlt_alarm_item_list);


//        ArrayList<AddAlarmItem> addAlarmlist  = new ArrayList<>();
//        addAlarmlist.add(new AddAlarmItem("06:10",1));
//        addAlarmlist.add(new AddAlarmItem("12:10",0));
//        addAlarmlist.add(new AddAlarmItem("21:10",1));
//        String json = new Gson().toJson(addAlarmlist);
//
//        editor.putString(TAG,json);
//        editor.commit();

        AddAlarmItem[] temp = new Gson().fromJson(sp.getString(TAG, ""), AddAlarmItem[].class);
        List<AddAlarmItem> temp2 ;
        try {
            temp2 = Arrays.asList(temp);
            addAlarmlist = new ArrayList(temp2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            alarmAdapter = new AlarmAdapter(addAlarmlist, getActivity());
            lv_alarm_list.setAdapter(alarmAdapter);

            time_picker = (TimePicker) view.findViewById(R.id.time_picker);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 初始化闹铃
        for (int i = 0; addAlarmlist != null && i < addAlarmlist.size(); i++) {
            pickTime = addAlarmlist.get(i).getAlarmTime();
            setAlarm();
        }
        pickTime = "00:00";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 添加闹钟
            case R.id.btn_add_alarm:
                selectTime();

                break;
            case R.id.btn_picktime_cancel:
//                pickStatus = 0;
                rlt_time_picker.setVisibility(View.GONE);
                rlt_alarm_item_list.setVisibility(View.VISIBLE);
                return;
            case R.id.btn_picktime_ok:
//                pickStatus = 1;
//                Log.i("abcd",pickTime);
                rlt_time_picker.setVisibility(View.GONE);
                rlt_alarm_item_list.setVisibility(View.VISIBLE);
                setAlarm();

                addAlarmlist.add(new AddAlarmItem(pickTime, 1));
                alarmAdapter.setAlarmAdapter(addAlarmlist);
                alarmAdapter.notifyDataSetChanged();

                String json = new Gson().toJson(addAlarmlist);
                editor.putString(TAG,json);
                editor.commit();

                break;
            case R.id.btn_clean_alarm:
                cleanAlarm();
                break;


        }

    }

    public void selectTime() {
        rlt_alarm_item_list.setVisibility(View.GONE);
        rlt_time_picker.setVisibility(View.VISIBLE);
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
                String H = hourOfDay + "", M = minute + "";
                if (Integer.valueOf(hourOfDay) < 10) {
                    H = "0" + hourOfDay;
                }
                if (Integer.valueOf(minute) < 10) {
                    M = "0" + minute;
                }
                pickTime = H + ":" + M;
            }

        });
    }

    // 利用 AlarmManager 设置闹钟
    public void setAlarm() {

//        int time = 60 * 1000 * 30;//30分钟
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String pt = pickTime +":00";
        String curentTime = sdf.format(new Date());
        long time = distanceOfTime(pt, curentTime) + 3000;
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        // 传送一个位置，便于移除
        intent.putExtra("pos", "" + addAlarmlist.size());
//        intent.setType("text/plain");
        intent.setAction("com.example.myapp.RING");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0x101, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), ++requestid, intent, 0);


//        第一个参数表示闹钟类型，
//        第二个参数表示闹钟首次执行时间，
//        第三个参数表示闹钟两次执行的间隔时间，
//        第四个参数表示闹钟响应动作
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,time,time+1,pendingIntent);

        if (Build.VERSION.SDK_INT < 19) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent);
        }
    }

    //   a 和 b 的时间间隔, b 为参照物
    long distanceOfTime(String a, String b) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null, date2 = null;
        try {
            date1 = sdf.parse(a);
            date2 = sdf.parse(b);
            long dist = date1.getTime() - date2.getTime();
            if (dist < 0) {
                dist += (60 * 60 * 24 * 1000);
            }
            return dist;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0x0fffffff;
    }

    public void cleanAlarm() {
        addAlarmlist = new ArrayList<>();
        alarmAdapter.setAlarmAdapter(addAlarmlist);
        alarmAdapter.notifyDataSetChanged();
        String json =  new Gson().toJson(addAlarmlist);
        editor.putString(TAG,json);
        editor.commit();
        onResume();
    }
    @Override
    public void onResume() {
        super.onResume();

    }


}
