package whut.com.myapp.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import whut.com.myapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountFragment extends Fragment implements View.OnClickListener{
    private Button btn_startTimer;
    private Button btn_endTimer;
    private CircleTimerView ctv_timer;

    // 表示当前状况 0 表示停止，1 表示正在暂停，2 表示运行
    private int status;

    Button getBtn_startTimer(){
        return btn_startTimer;
    }

    CountFragment getCountFragment(){
        return this;
    }

    public CountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_count, container, false);
        initView(view);

        return  view;
    }

    public void initView(View view){
        btn_startTimer = (Button)view.findViewById(R.id.btn_startTimer);
        btn_endTimer = (Button)view.findViewById(R.id.btn_endTimer);
        ctv_timer = (CircleTimerView)view.findViewById(R.id.ctv_timer);


        btn_startTimer.setOnClickListener(this);
        btn_endTimer.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_startTimer:
                if(status != 2){
                    if(ctv_timer.getCurrentTime()==0) return;
                    status = 2;
                    btn_startTimer.setBackgroundResource(R.drawable.pausetimer);
                    ctv_timer.startTimer();
                }
//                    ctv_timer.setCurrentTime(0);

                else if(status == 2) {
                    btn_startTimer.setBackgroundResource(R.drawable.starttimer);
                    status = 1;
                    ctv_timer.pauseTimer();
                }
                break;

            case R.id.btn_endTimer:
                status = 0;
                ctv_timer.setCurrentTime(0);
                btn_startTimer.setBackgroundResource(R.drawable.starttimer);

                ctv_timer.pauseTimer();
//                ctv_timer.
                break;
        }

    }
}
