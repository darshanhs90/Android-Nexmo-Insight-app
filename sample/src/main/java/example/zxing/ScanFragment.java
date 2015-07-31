package example.zxing;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public  class ScanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private String toast,clickedNumber;
    String countryName,time;
    SwipeRefreshLayout mSwipeView;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    ListView lv;
    Boolean response,webRedirect=false,callNumber=false;
    View view;
    private int lastExpandedPosition = -1;
    View childView;
    String phone;
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;
    String LOGIN_URL1 = "https://api.nexmo.com/number/lookup/json?api_key=638c2b46&api_secret=60539549&number=";
    String URL;
    String LOGIN_URL2 = "https://api.nexmo.com/number/lookup/json?api_key=638c2b46&api_secret=60539549&number=";
    HospitalListAdapter expListAdapter;
    ArrayList<String[]> arrayList;
    JSONArray jArray=new JSONArray();
    ArrayAdapter<String> arrayAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mSwipeView =(SwipeRefreshLayout)view.findViewById(R.id.swipe_viewhome);
        webRedirect=false;


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
    private void createGroupList() {

    }

    private void createCollection() {
        // preparing laptops collection(child)


        laptopCollection = new LinkedHashMap<String, List<String>>();

        for (int i=0;i<groupList.size();i++) {
            loadChild(arrayList.get(i));
            //load child correesponding to its index

            laptopCollection.put(groupList.get(i), childList);
        }
    }

    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
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
                groupList = new ArrayList<String>();
                arrayList=new ArrayList<String[]>();
                int counter=0;
                ContentResolver cr = view.getContext().getContentResolver(); //Activity/Application android.content.Context
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if(cursor.moveToFirst())
                {
                    do
                    {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                        {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                            while (pCur.moveToNext())
                            {
                                String str[]=new String[4];
                                String name=pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                groupList.add(name);
                                String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                str[0]=contactNumber;
                                str[1]="Check Format";
                                str[2]="Call Number";
                                str[3]="Recharge Number";
                                arrayList.add(counter,str);
                                counter++;
                                Log.d("asd", name);
                                Log.d("asd",(counter-1)+"");
                                Log.d("asd", contactNumber);
                                break;
                            }
                            pCur.close();
                        }

                    } while (cursor.moveToNext()) ;
                }
                if(arrayList.size()==0){
                    String str[]=new String[1];
                    groupList.add("No numbers to display");
                    str[0]="No numbers to display";
                    arrayList.add(str);
                }

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        createCollection();
                        expListView = (ExpandableListView) view.findViewById(R.id.laptop_list);
                        expListAdapter = new HospitalListAdapter(getActivity(), groupList, laptopCollection);

                        expListView.setAdapter(expListAdapter);

                        //setGroupIndicatorToRight();

                        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                            @Override
                            public void onGroupExpand(int groupPosition) {
                                if (lastExpandedPosition != -1
                                        && groupPosition != lastExpandedPosition) {
                                    expListView.collapseGroup(lastExpandedPosition);
                                }
                                lastExpandedPosition = groupPosition;
                            }
                        });

                        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                            public boolean onChildClick(ExpandableListView parent, View v,
                                                        int groupPosition, int childPosition, long id) {

                                String selected = (String) expListAdapter.getChild(
                                        groupPosition, childPosition);
                                Log.d("asd",childPosition+"");
                                String str[]=arrayList.get(groupPosition);
                                String number=str[0];
                                Log.d("asd",number);
                                number=number.replace(" ","");
                                number=number.replace("-","");
                                clickedNumber=number;
                                Log.d("follow",childPosition+"");
                                if(childPosition!=0){
                                    if(childPosition==1)
                                    {
                                        URL=LOGIN_URL1+number;
                                    }
                                    else if(childPosition==2)
                                    {
                                        URL=LOGIN_URL2+number;
                                        callNumber=true;
                                    }
                                    else{
                                        //start activity
                                        URL=LOGIN_URL1+number;
                                        webRedirect=true;
                                    }

                                    View groupView=expListView.getChildAt(groupPosition);
                                    groupView.setBackgroundColor(Color.BLUE);
                                    childView=groupView;
                                    Log.d("child", childView + "");
                                    new GetResponse().execute();
                                   //Toast.makeText(getActivity().getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            }
                        });
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
    class GetResponse extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Image ....");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                response=true;
                Log.d("response",URL);
                JSONObject j=(JSONObject)new JSONArrayParser().getJsonObject(URL);
                Log.d("response", j + "");
                String responseString=j.getString("status_message");
                if(!responseString.contains("Success"))
                {
                    response=false;
                }

                if(webRedirect && response){
                    webRedirect=false;
                    String carrierName=((JSONObject)j.get("current_carrier")).getString("name");
                    carrierName=carrierName.replace(" ","%20");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q="+carrierName+"+recharge"));
                    startActivity(browserIntent);
                }
                if(callNumber && response){
                    countryName=j.getString("country_name");
                    phone=j.getString("international_format_number");
                    new GetResponseNew().execute();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response) {
                                childView.setBackgroundColor(Color.GREEN);
                            Toast.makeText(getActivity().getBaseContext(), "Number Format Valid", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            childView.setBackgroundColor(Color.RED);
                            Toast.makeText(getActivity().getBaseContext(), "Number Format Invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();


        }

    }
    class GetResponseNew extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                response=true;
                countryName=countryName.replace(" ","%20");
                URL="https://api.worldweatheronline.com/free/v2/tz.ashx?q="+countryName+"&format=json&key=00752734b02136413e0b9e18f9d6a";
                Log.d("asd",URL);
                JSONObject j=(JSONObject)new JSONArrayParser().getJsonObject(URL);
                Log.d("asd",j+"");
                JSONObject data=((JSONObject)j.get("data"));
                JSONArray timeZone=((JSONArray)data.get("time_zone"));
                JSONObject timeObj=((JSONObject)timeZone.get(0));
                time=timeObj.getString("localtime");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Nexmo Caller")
                            .setMessage(" The timezone at the receivers country is "+time+" .Do you want to call?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:"+phone));
                                    startActivity(callIntent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    Toast.makeText(getActivity(), "Call Cancelled", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

        }

    }
}




