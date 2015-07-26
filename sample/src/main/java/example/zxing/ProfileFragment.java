package example.zxing;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import org.json.JSONArray;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public  class ProfileFragment extends Fragment{
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
    SharedPreferences sharedPreferences;
    Boolean editable;
    List<String> lvArray = new ArrayList<String>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        Cursor c = getActivity().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        c.moveToFirst();
        llName= (LinearLayout) view.findViewById(R.id.llName);
        llPhone= (LinearLayout) view.findViewById(R.id.llPhone);
        llButton= (LinearLayout) view.findViewById(R.id.llButton);
        Button bnClick= (Button) view.findViewById(R.id.bnClick);
        ivQR= (ImageView) view.findViewById(R.id.ivQR);
        sharedPreferences=getActivity().getSharedPreferences("Profile", getActivity().MODE_PRIVATE);
        name=sharedPreferences.getString("name", "");
        phone=sharedPreferences.getString("phonenum", "");
        editable=sharedPreferences.getBoolean("editable",true);
        if(editable==true) {
            llName.setVisibility(View.VISIBLE);
            llPhone.setVisibility(View.VISIBLE);
            llButton.setVisibility(View.VISIBLE);
            ivQR.setVisibility(View.INVISIBLE);
        }
        bnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                EditText etName=(EditText)view.findViewById(R.id.etName);
                name=etName.getText().toString();
                EditText etPhone=(EditText)view.findViewById(R.id.etPhone);
                phone=etPhone.getText().toString();
                loadImageView();
            }
        });
        if(editable==true) {
            try {
                name = c.getString(c.getColumnIndex("display_name"));
                if(name==null)
                    name="";
            } catch (Exception e) {
                e.printStackTrace();
            }
            c.close();
            TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            try {
                if(mPhoneNumber!=null)
                   phone = mPhoneNumber;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(editable && (name.contentEquals("") || phone.contentEquals(""))){
            Toast.makeText(getActivity(),"Name and Phone Number Not Set in the Phone",Toast.LENGTH_SHORT).show();
            llName.setVisibility(View.VISIBLE);
            llPhone.setVisibility(View.VISIBLE);
            llButton.setVisibility(View.VISIBLE);
        }
        else {
            Log.d("phonnumber3",phone);
            loadImageView();
        }
        return view;
    }
    public void loadImageView(){
        ImageView ivQR= (ImageView) view.findViewById(R.id.ivQR);
        if(!name.contentEquals("") && !phone.contentEquals("")){
            new LoadImage().execute("http://api.qrserver.com/v1/create-qr-code/?color=000000&bgcolor=FFFFFF&data=darshanhs&qzone=1&size=600x600&align=center");
        }
        else{
            Toast.makeText(getActivity(),"Enter Name and Phone Number",Toast.LENGTH_SHORT).show();
        }

    }
    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                ivQR.setImageBitmap(image);
                pDialog.dismiss();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("name", name);
                editor.putString("phonenum", phone);
                editor.putBoolean("editable", false);
                editor.commit();
                ivQR.setVisibility(View.VISIBLE);
                llPhone.setVisibility(View.INVISIBLE);
                llName.setVisibility(View.INVISIBLE);
                llButton.setVisibility(View.INVISIBLE);

            }else{

                pDialog.dismiss();
                Toast.makeText(getActivity(), "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }
}




