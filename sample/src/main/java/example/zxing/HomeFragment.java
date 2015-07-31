package example.zxing;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public  class HomeFragment extends Fragment {
    private String toast;
    View view;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String phone;
    String name;

    Boolean response;
    int x = 0;
    String link="";
    String LOGIN_URL = "https://api.nexmo.com/number/lookup/json?api_key=638c2b46&api_secret=60539549&number=";
    SharedPreferences sharedPreferences;
    Boolean nexmoValid;
    public HomeFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayToast();
    }

    //src="http://docs.google.com/gview?embedded=true&url=https://drive.google.com/file/d/0B4vimDEQD19fMGVEejZKQko3TDQ/view?usp=sharing"
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan, container, false);
        sharedPreferences=getActivity().getSharedPreferences("Profile", getActivity().MODE_PRIVATE);
        nexmoValid=sharedPreferences.getBoolean("nexmoValid", true);
        ImageButton scan = (ImageButton) view.findViewById(R.id.scan_from_fragment);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromFragment();
            }
        });

        return view;
    }

    public void scanFromFragment() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    private void displayToast() {
        if (getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                toast = "Cancelled from fragment";
            } else {
                toast = "Scanned from fragment: " + result.getContents();
                String str = result.getContents();
                Log.d("asd", str);
                if (str.indexOf("Name") != -1 && str.indexOf("Phone") != -1) {
                    String strArray[] = str.split("Name");
                    name = "";
                    if (strArray.length >= 2) {
                        name = strArray[1].substring(1, strArray[1].indexOf(",Phone"));
                        Log.d("asd", strArray[1].substring(1, strArray[1].indexOf(",Phone")));
                    }
                    strArray = str.split("Phone");
                    phone = "";
                    if (strArray.length >= 2) {
                        phone = strArray[1].substring(1);
                        Log.d("asd", strArray[1].substring(1));
                    }
                    if (!phone.contentEquals("")) {
                        response = true;
                        Log.d("nexmo valid", nexmoValid + "");
                        if (nexmoValid) {
                            //validate number
                            Log.d("nexmo valid", "in nemxo valid");
                            new GetList().execute();
                        }
                    }
                }
                else{
                    Toast.makeText(getActivity(),"Invalid QR code text : "+result.getContents(),Toast.LENGTH_SHORT).show();
                }
            }

            // At this point we may or may not have a reference to the activity
            // displayToast();
        }
    }
    class GetList extends AsyncTask<String, String, String> {

        boolean failure = false;
        String country,notes;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {
            try {
                phone=phone.replace("+","");
                phone=phone.trim();
                String URL=LOGIN_URL+phone;
                JSONObject j=(JSONObject)new JSONArrayParser().getJsonObject(URL);
                Log.d("response", j + "");
                String responseString=j.getString("status_message");
                if(!responseString.contains("Success"))
                {
                    response=false;
                }
                else{
                    response=true;
                    country= ((JSONObject)j.get("current_carrier")).getString("country");
                    String previousCountry= "Previous Country : "+((JSONObject)j.get("original_carrier")).getString("country");
                    String carrierName= "Carrier Name : "+((JSONObject)j.get("original_carrier")).getString("name");
                    String countryCode= "Country Code : "+(j.getString("country_code_iso3"));
                    String internationalNumberFormat= "International Number Format : "+(j.getString("international_format_number"));
                    String nationalNumberFormat= "National Number Format : "+(j.getString("national_format_number"));
                    String currentCountry= "Current Country : "+((JSONObject)j.get("current_carrier")).getString("country");
                    notes=currentCountry+","+previousCountry+","+carrierName+","+countryCode+","+internationalNumberFormat+","+nationalNumberFormat;

                }

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if(response==false){
                            Toast.makeText(getActivity(),"Invalid Phone number : "+phone,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {

            if(response) {
                Intent addContactIntent = new Intent(Contacts.Intents.Insert.ACTION, Contacts.People.CONTENT_URI);
                if (!name.contentEquals(""))
                    addContactIntent.putExtra(Contacts.Intents.Insert.NAME, name);
                addContactIntent.putExtra(Contacts.Intents.Insert.PHONE, phone);
                addContactIntent.putExtra(Contacts.Intents.Insert.POSTAL,"Country : "+country);
                addContactIntent.putExtra(Contacts.Intents.Insert.NOTES,notes);


                startActivity(addContactIntent);
            }else{
                Toast.makeText(getActivity(),"Number is not in valid format",Toast.LENGTH_SHORT).show();
            }

        }

    }

}