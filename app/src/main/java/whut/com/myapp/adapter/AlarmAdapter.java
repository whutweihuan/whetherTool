package whut.com.myapp.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import whut.com.myapp.R;
import whut.com.myapp.bean.AddAlarmItem;

public class AlarmAdapter extends BaseAdapter {
    List<AddAlarmItem> mAddAlarmArrayList;
    Context ctx;

    public AlarmAdapter(List<AddAlarmItem> mAAddAlarmItem, Context ctx) {
        this.mAddAlarmArrayList = mAAddAlarmItem;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return mAddAlarmArrayList == null?0:mAddAlarmArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAddAlarmArrayList == null?null:mAddAlarmArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(ctx, R.layout.alarm_item_layout,null);
            viewHolder.alarmTime = (TextView)convertView.findViewById(R.id.tv_alarm_time);
            viewHolder.iv_alarm_use = (ImageView)convertView.findViewById(R.id.iv_alarm_use);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        AddAlarmItem item = mAddAlarmArrayList.get(position);
        viewHolder.alarmTime.setText(item.getAlarmTime());
        if(item.getUse()==0){
            viewHolder.iv_alarm_use.setImageResource(R.drawable.btn_close);
        }else {
            viewHolder.iv_alarm_use.setImageResource(R.drawable.btn_open);
        }

        return convertView;
    }

    class ViewHolder{
        ImageView iv_alarm_use;
        TextView alarmTime;
    }

}
