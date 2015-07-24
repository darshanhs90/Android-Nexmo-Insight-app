package example.zxing;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;


public  class JobsFragment extends Fragment   {
    Button bnJobsSearch;


    private String toast;
    SwipeRefreshLayout mSwipeView;
    SearchView mSearchView;
     TextView textView1,textView2;
    public JobsFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);
        mSearchView=(SearchView)view.findViewById(R.id.searchViewCompany);
        textView1=(TextView)view.findViewById(R.id.textView1);
        textView2=(TextView)view.findViewById(R.id.textView2);
        bnJobsSearch=(Button)view.findViewById(R.id.bnJobsSearch);
        bnJobsSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              CharSequence str=  mSearchView.getQuery();
                Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
                textView1.setVisibility(View.GONE);
                textView2.setVisibility(View.VISIBLE);
                mSwipeView.setVisibility(View.VISIBLE);
                //make localhost call with this
            }
        });
        mSwipeView =(SwipeRefreshLayout)view.findViewById(R.id.swipe_viewjobs);
        mSwipeView.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light);

        mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

