package whut.com.myapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import whut.com.myapp.UI.AlarmFragment;
import whut.com.myapp.UI.CountFragment;
import whut.com.myapp.UI.MoreFragment;
import whut.com.myapp.UI.WhetherFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 4;
    WhetherFragment whetherFragment = null;
    AlarmFragment alarmFragment=null;
    CountFragment countFragment=null;
    MoreFragment moreFragment = null;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        whetherFragment = new WhetherFragment();
        alarmFragment = new AlarmFragment();
        countFragment = new CountFragment();
        moreFragment = new MoreFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:return whetherFragment;
            case 1:return alarmFragment;
            case 2:return countFragment;
            case 3:return moreFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
