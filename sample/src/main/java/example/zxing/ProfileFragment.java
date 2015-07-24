package example.zxing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.InputStream;


public  class ProfileFragment extends Fragment {
    private String toast;

    public ProfileFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
       WebView wv =(WebView)view.findViewById(R.id.ivQR);
        wv.getSettings().setJavaScriptEnabled(true);
        String str=getActivity().getIntent().getExtras().get("email").toString();
        String doc="http://api.qrserver.com/v1/create-qr-code/?color=000000&bgcolor=FFFFFF&data="+str+"&qzone=1&margin=0&size=300x300&center=true";
       wv.loadUrl(doc);


        return view;
    }




}

