package example.zxing;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public  class ApplicantsFragment extends Fragment {
    private String toast;
    SwipeRefreshLayout mSwipeView;
    public ApplicantsFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applicants, container, false);
        mSwipeView =(SwipeRefreshLayout)view.findViewById(R.id.swipe_viewapplicants);
        mSwipeView.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light);

        mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Your action here
                mSwipeView.setRefreshing(false);
            }
        });
        return view;
    }

    public void onRefresh() {
        Toast.makeText(getActivity(), "Refreshing", Toast.LENGTH_SHORT).show();
    }

}

