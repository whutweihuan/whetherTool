package whut.com.myapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import whut.com.myapp.UI.RingActivity;

/**
 * Created by Administrator on 2017/2/22.
 */

public class RingReceived extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.example.myapp.RING".equals(intent.getAction())){
            Log.i("test","闹钟响了");
            //跳转到Activity
            Intent intent1=new Intent(context,RingActivity.class);
            //广播跳转到activity.必须给intent设置标志位flags

            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);

        }
    }
}



