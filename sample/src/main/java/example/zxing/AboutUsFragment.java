package example.zxing;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public  class AboutUsFragment extends Fragment {
    private String toast;
    public AboutUsFragment() {
    }
//    public AboutUsFragment(Activity activity) {
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutus, container, false);

        return view;
    }


}

