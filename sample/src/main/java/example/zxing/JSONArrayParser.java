package example.zxing;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.apache.http.params.HttpConnectionParams.*;

/**
 * Created by hasudhakar on 6/30/15.
 */
public class JSONArrayParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public JSONArrayParser() {
    }

    /**
     * This method is used to parse the Url by taking the url as parameter
     *
     * @param url
     * @return
     */
    public Object getJsonObject(String url) throws IOException {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpParams httpParams = new BasicHttpParams();
            setStaleCheckingEnabled(httpParams, false);
            setConnectionTimeout(httpParams, 15000);
            setSoTimeout(httpParams, 15000);
            httpGet.setParams(httpParams);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                json = EntityUtils.toString(httpEntity);
                try {
                    jObj = new JSONObject(json);
                } catch (Exception e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                if (json.startsWith("[")) {
                    // We have a JSONArray
                    try {
                        jObj = new JSONObject();
                        jObj.put("data", new JSONArray(json));
                    } catch (JSONException e) {
                        Log.d("JSON Parser",
                                "Error parsing JSONArray " + e.toString());
                    }
                    return jObj;
                }
                // return JSON String
                return jObj;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
