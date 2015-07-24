package example.zxing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public  class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private String toast;
    SwipeRefreshLayout mSwipeView;
    JSONArray jArray=new JSONArray();
    ArrayAdapter<String> arrayAdapter;
    private static final String LOGIN_URL = "http://techrecruit.site40.net/rec_retrieve.php";
    JSONParser jsonParser = new JSONParser();
    ListView lv;
    View view;
    List<String> lvArray = new ArrayList<String>();
    public HomeFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_home, container, false);
        mSwipeView =(SwipeRefreshLayout)view.findViewById(R.id.swipe_viewhome);

        lv = (ListView) view.findViewById(R.id.list_viewhome);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.

        mSwipeView.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light);
        new GetList().execute();

        mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                new GetList().execute();
                Log.d("ads","in refresh");
                mSwipeView.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onRefresh() {

//        lvArray=new ArrayList<String>();
//        arrayAdapter=null;
//        jArray=new JSONArray();
        new GetList().execute();
    }



    class GetList extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                // params.add(new BasicNameValuePair("email", username));
                // params.add(new BasicNameValuePair("password", password));
                JSONObject j=(JSONObject)new JSONArrayParser().getJsonObject(LOGIN_URL);

                Log.d("sasd",j+"");
                Log.d("asd",j.get("data")+"");
                JSONArray j1= (JSONArray) j.get("data");
                Log.d("asd",j1+"");
                lvArray=new ArrayList<String>();
                for (int i = 0; i < j1.length() ; i++) {
                    JSONObject jObj= (JSONObject) j1.get(i);
                    lvArray.add(jObj.get("fname").toString());
                }

                 arrayAdapter = new ArrayAdapter<String>(
                        view.getContext(),
                        android.R.layout.simple_list_item_1,
                        lvArray );
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        lv.setAdapter(arrayAdapter);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {



        }

    }

}




