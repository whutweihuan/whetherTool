package whut.com.myapp.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import whut.com.myapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhetherFragment extends Fragment {


    public WhetherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_whether, container, false);
    }

}
