package example.zxing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public  class ScanFragment extends Fragment {
    private String toast;
    View view;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://techrecruit.site40.net/insert.php";
    private static final String LOGIN_URL1 = "http://techrecruit.site40.net/sendresume.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String semail = "";
    int x = 0;
    String link="";
    public ScanFragment() {
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

        ImageButton scan = (ImageButton) view.findViewById(R.id.scan_from_fragment);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromFragment();
            }
        });

        ImageButton plus = (ImageButton) view.findViewById(R.id.plusbutton);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AttemptLogin().execute();
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
            Log.d("asd",semail);
            new AttemptLogin2().execute();
            WebView wv = (WebView) view.findViewById(R.id.pdfview);
            wv = (WebView) view.findViewById(R.id.pdfview);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.loadUrl("https://docs.google.com/viewer?url=" + link);
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
                semail = result.getContents().trim();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }


    class AttemptLogin extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
// TODO Auto-generated method stub
// Check for success tag
            int success;

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("semail", semail));
                params.add(new BasicNameValuePair("remail", getActivity().getIntent().getExtras().get("email").toString()));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);
                //Toast.makeText(getApplicationContext(),json+"",Toast.LENGTH_SHORT).show();
// check your log for json response
                Log.d("Login attempt", json.toString());

// json success tag
                success = json.getInt("value");
                Log.d("asd", success + "");
                x = success;
                if (x == 1) {
                    Log.d("asd", x + "");
                }
                Log.d("asd", x + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
//			if (file_url != null){
//				Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
//
//			}

        }

    }



    class AttemptLogin2 extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
// TODO Auto-generated method stub
// Check for success tag
            int success;

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("semail", semail));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL1, "POST", params);
                //Toast.makeText(getApplicationContext(),json+"",Toast.LENGTH_SHORT).show();
// check your log for json response
                Log.d("Login attempt", json.toString());

// json success tag
                link = json.get("resumelink").toString();
                Log.d("asd", link + "");
                if(link==null)
                    link="https://s3-us-west-2.amazonaws.com/hackathonutd/a.pdf";
                if (x == 1) {
                    Log.d("asd", x + "");
                }
                Log.d("asd", x + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
//			if (file_url != null){
//				Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
//
//			}

        }

    }

}