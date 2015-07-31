package example.zxing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public  class RoamingFragment extends Fragment{
    private String toast;
    SwipeRefreshLayout mSwipeView;
    JSONArray jArray=new JSONArray();
    ArrayAdapter<String> arrayAdapter;
    JSONParser jsonParser = new JSONParser();
    ListView lv;
    View view;
    String name="";
    ProgressDialog pDialog;
    String phone="";
    Bitmap bitmap;
    LinearLayout llName,llPhone,llButton;
    ImageView ivQR;
    EditText etPhone,etName;
    String LOGIN_URL1 = "https://api.nexmo.com/number/lookup/json?api_key=638c2b46&api_secret=60539549&number=";
    SharedPreferences sharedPreferences;
    Boolean editable,response;
    List<String> lvArray = new ArrayList<String>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_roaming, container, false);
        Cursor c = getActivity().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        c.moveToFirst();

        sharedPreferences=getActivity().getSharedPreferences("Profile", getActivity().MODE_PRIVATE);
        phone=sharedPreferences.getString("phonenum", "");
        if(phone.contentEquals("")) {
            TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            try {
                if(mPhoneNumber!=null)
                    phone = mPhoneNumber;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!phone.contentEquals(""))
        {
            new GetResponse().execute();
        }
        Button bnClick= (Button) view.findViewById(R.id.bnClick);
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
                            String carrierName= null;
                            try {
                                carrierName = ((JSONObject)j.get("current_carrier")).getString("name");

                                String URL="";
                                if(carrierName.toLowerCase().contains("t-mobile")){
                                    URL="http://www.t-mobile.com/optional-services/roaming.html";
                                }
                                else if(carrierName.toLowerCase().contains("t-mobile")){
                                    URL="http://www.t-mobile.com/optional-services/roaming.html";
                                }else if(carrierName.toLowerCase().contains("at&t")){
                                    URL="http://www.att.com/shop/wireless/international/roaming.html";
                                }else if(carrierName.toLowerCase().contains("verizon")){
                                    URL="http://www.verizonwireless.com/landingpages/international-travel/";
                                }else if(carrierName.toLowerCase().contains("sprint")){
                                    URL="http://support.sprint.com/support/international/roaming#!/";
                                }else if(carrierName.toLowerCase().contains("airtel")){
                                    URL="http://www.airtel.in/mobile/postpaid/roaming/international";
                                }else{
                                    URL="http://www.google.com/search?q=roaming charges "+carrierName+" usa";
                                }
                                URL=URL.replace(" ","%20");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                                startActivity(browserIntent);
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




