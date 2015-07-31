package example.zxing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public  class LocationFragment extends Fragment{
    private String toast;
    SwipeRefreshLayout mSwipeView;
    JSONArray jArray=new JSONArray();
    ArrayAdapter<String> arrayAdapter;
    JSONParser jsonParser = new JSONParser();
    ListView lv;
    View view;
    String name="";
    TextView tvInfo;
    ProgressDialog pDialog;
    String map;
    String phone="";
    Bitmap bitmap;
    LinearLayout llName,llPhone,llButton,llButtonLocation;
    ImageView ivQR;
    EditText etPhone,etName;
    String LOGIN_URL1 = "https://api.nexmo.com/number/lookup/json?api_key=638c2b46&api_secret=60539549&number=";
    SharedPreferences sharedPreferences;
    Boolean editable,response;
    List<String> lvArray = new ArrayList<String>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);

        if(!phone.contentEquals(""))
        {
            new GetResponse().execute();
        }
        Button bnClick= (Button) view.findViewById(R.id.bnClick);
        llButtonLocation= (LinearLayout) view.findViewById(R.id.llButtonLocation);
        Button bnClickLocation=(Button) view.findViewById(R.id.bnClickLocation);
        tvInfo= (TextView) view.findViewById(R.id.tvInfo);
        tvInfo.setVisibility(View.INVISIBLE);
        if(phone.contentEquals("")){
            bnClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    EditText etPhone = (EditText) view.findViewById(R.id.etPhone);
                    phone = etPhone.getText().toString();

                    if (!phone.contentEquals("")) {
                        Log.d("asd", phone);
                        new GetResponse().execute();
                    } else {
                        Toast.makeText(getActivity(), "Enter Phone Number", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        bnClickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(intent);
            }
        });





        return view;
    }

    class GetResponse extends AsyncTask<String, String, String> {
        JSONObject j;
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
                Log.d("asdNew",phone);
                phone=phone.replace(" ","");
                phone=phone.replace("-","");
                String URL=LOGIN_URL1+phone;
                j=(JSONObject)new JSONArrayParser().getJsonObject(URL);
                Log.d("response", j + "");
                String responseString=j.getString("status_message");
                if(!responseString.contains("Success"))
                {
                    response=false;
                }

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if(response){
                            pDialog.dismiss();
                            try {
                                tvInfo.setVisibility(View.VISIBLE);
                                String countryName = ((JSONObject)j.get("current_carrier")).getString("country");
                                Locale loc = new Locale("",countryName);
                                loc.getDisplayCountry();
                                llButtonLocation.setVisibility(View.VISIBLE);
                                map = "http://maps.google.co.in/maps?q=" + loc.getDisplayCountry();
                                String text="International Format Number : "+j.getString("international_format_number");
                                String text1=" National Format Number : "+j.getString("national_format_number");
                                String text2=" Country Name : "+j.getString("country_name");
                                String text3=" Carrier Name : "+((JSONObject)j.get("current_carrier")).getString("name");


                                tvInfo.setText(text+text1+text2+text3);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else{
                            pDialog.dismiss();
                            Toast.makeText(getActivity(),"Number is in Invalid Format",Toast.LENGTH_SHORT).show();
                        }
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




