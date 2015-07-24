
package example.zxing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
	// LogCat tag
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnLogin;
	private Button btnLinkToRegister;
	private EditText inputEmail;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	Activity activity;
	JSONParser jsonParser = new JSONParser();
	private static final String LOGIN_URL = "http://techrecruit.site40.net/droidlogin.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	int x=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		activity=this;
		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);



		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				Log.d("asd",x+"before");
				//new AttemptLogin().execute();
				Log.d("asd", x + "after");
				Intent intent = new Intent(activity, PostLoginActivity.class);
				intent.putExtra("email", "hsdars@gmail.com");
				startActivity(intent);

			}

		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://www.google.com"));
				startActivity(intent);
			}
		});

	}

	/**
	 * function to verify login details in mysql db
	 * */
	private void checkLogin(final String email, final String password) {
		// Tag used to cancel the request
		String tag_string_req = "req_login";

		pDialog.setMessage("Logging in ...");
		pDialog.show();

	}



	class AttemptLogin extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
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
			String username =inputEmail.getText().toString();
			String password =inputPassword.getText().toString();
			try {

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("email", username));
				params.add(new BasicNameValuePair("password", password));

				Log.d("request!", "starting");

				JSONObject json = jsonParser.makeHttpRequest(
						LOGIN_URL, "POST", params);
				//Toast.makeText(getApplicationContext(),json+"",Toast.LENGTH_SHORT).show();
// check your log for json response
				Log.d("Login attempt", json.toString());

// json success tag
				success = json.getInt("value");
				Log.d("asd",success+"");
				x=success;
				if(x==1) {
					Intent intent = new Intent(activity, PostLoginActivity.class);
					intent.putExtra("email",username);
					startActivity(intent);
				}
				else{
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							//update ui here

							Toast.makeText(getApplicationContext(), "Error while logging", Toast.LENGTH_SHORT).show();
							// display toast here
						}
					});
					}
				Log.d("asd",x+"");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {

			pDialog.dismiss();
//			if (file_url != null){
//				Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
//
//			}

		}

	}
}
