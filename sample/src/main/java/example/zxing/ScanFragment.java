package example.zxing;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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


public  class ScanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private String toast;
    SwipeRefreshLayout mSwipeView;
    JSONArray jArray=new JSONArray();
    ArrayAdapter<String> arrayAdapter;
    private static final String LOGIN_URL = "http://techrecruit.site40.net/rec_retrieve.php";
    JSONParser jsonParser = new JSONParser();
    ListView lv;
    View view;
    List<String> lvArray = new ArrayList<String>();
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

                ContentResolver cr = view.getContext().getContentResolver(); //Activity/Application android.content.Context
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if(cursor.moveToFirst())
                {
                    lvArray = new ArrayList<String>();
                    do
                    {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                        {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                            while (pCur.moveToNext())
                            {
                                Log.d("asd", pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                                Log.d("asd", pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED)));

                                String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                lvArray.add(contactNumber);
                                Log.d("asd", contactNumber);
                                break;
                            }
                            pCur.close();
                        }

                    } while (cursor.moveToNext()) ;
                }
                if(lvArray.size()==0)
                    lvArray.add("No numbers to display");
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




