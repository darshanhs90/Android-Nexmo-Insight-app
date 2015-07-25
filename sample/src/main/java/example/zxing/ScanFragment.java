package example.zxing;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public  class ScanFragment extends Fragment {
    private String toast;
    View view;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
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

        return view;
    }

    public void scanFromFragment() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    private void displayToast() {
        if (getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
            Log.d("asd", semail);
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

                Cursor c = getActivity().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
                c.moveToFirst();
                Log.d("asd", c.getString(c.getColumnIndex("display_name")));
                c.close();



                String str=result.getContents();
                String strArray[]=str.split("Name");
                String name="";
                if(strArray.length>=2) {
                    name = strArray[1].substring(1, strArray[1].indexOf(",Phone"));
                    Log.d("asd", strArray[1].substring(1, strArray[1].indexOf(",Phone")));
                }
                strArray=str.split("Phone");
                String phone="";
                if(strArray.length>=2) {
                    phone = strArray[1].substring(1);
                    Log.d("asd", strArray[1].substring(1));
                }
                if(!phone.contentEquals("")) {
                    Intent addContactIntent = new Intent(Contacts.Intents.Insert.ACTION, Contacts.People.CONTENT_URI);
                    if(!name.contentEquals(""))
                        addContactIntent.putExtra(Contacts.Intents.Insert.NAME, name);
                    addContactIntent.putExtra(Contacts.Intents.Insert.PHONE, phone);
                    startActivity(addContactIntent);
                }
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }


}