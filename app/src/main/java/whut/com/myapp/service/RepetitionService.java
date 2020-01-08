package whut.com.myapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import whut.com.myapp.R;
import whut.com.myapp.UI.MainUIActivity;

public class RepetitionService extends IntentService {
    MediaPlayer mediaPlayer;

    public static final String TAG = "RepetitionService";


    public RepetitionService() {
        super("RepetitionService");
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        做一些逻辑，由于IntentService 会进行异步处理，
//        所以这里可以直接写耗时逻辑，不会占用主线程耗时，不需要再开启异步线程，
//        onHandleIntent 执行完后， Service会自动销毁；
        mediaPlayer =MediaPlayer.create(this, R.raw.music_cd);
        mediaPlayer.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
            }
        }, 3000);

    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        mediaPlayer.stop();
    }
}