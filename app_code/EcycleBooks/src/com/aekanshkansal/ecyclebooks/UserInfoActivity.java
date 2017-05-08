package com.aekanshkansal.ecyclebooks;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserInfoActivity extends Activity {

	String name,pass,confirm,contact,city,school,userid;
	boolean passvalid=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		Intent i=getIntent();
		userid=i.getStringExtra("ecyclebooks.userid");
		final EditText passedit=(EditText)findViewById(R.id.editText2);
		passedit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				pass=passedit.getText().toString();
				TextView tv3=(TextView)findViewById(R.id.textView3);
				if(pass.isEmpty())
				{
					tv3.setText("Please Enter Password");
				}
				else if(pass.length()<6 || pass.length()>32)
				{
					tv3.setText("Password should be of 6-32 characters");
				}
				else
				{
					tv3.setText("Valid Password");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		final EditText confirmedit=(EditText)findViewById(R.id.editText3);
		confirmedit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				TextView tv4=(TextView)findViewById(R.id.textView4);
				confirm=confirmedit.getText().toString();
				if(confirm.isEmpty())
				{
					tv4.setText("Please reenter Password");					
				}
				else if(!confirm.equals(pass))
				{
					tv4.setText("Passwords do not match");					
				}
				else
				{
					tv4.setText("Password matched");
					passvalid=true;
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		Button submit=(Button)findViewById(R.id.button1);
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Validation
				EditText nameedit=(EditText)findViewById(R.id.editText1);
				EditText contactedit=(EditText)findViewById(R.id.editText4);
				EditText cityedit=(EditText)findViewById(R.id.editText5);
				EditText schooledit=(EditText)findViewById(R.id.editText6);

				name=nameedit.getText().toString();
				contact=contactedit.getText().toString();
				city=cityedit.getText().toString();
				school=schooledit.getText().toString();
				if(name.isEmpty() || contact.isEmpty() || city.isEmpty())
				{
					MyGlobal.showAlert(UserInfoActivity.this,"Notice","Please Enter Mandatory Fields");
				}
				else if(!passvalid)
				{
					MyGlobal.showAlert(UserInfoActivity.this,"Notice","Password Fields are not valid");
				}
				else if(contact.length()!=10 || (!contact.startsWith("9") && !contact.startsWith("8") && !contact.startsWith("7")))
				{
					MyGlobal.showAlert(UserInfoActivity.this,"Notice","Please Enter Valid Contact Number");
				}
				else
				{
					if(school.isEmpty())
					school="NA";
				new NetCheck().execute();
				}


			}
		});
		
	}

	public void updateInfo()
	{
		new SendAccountInfoAtServer().execute();
	}
	
	private class NetCheck extends AsyncTask<String, Void, Boolean>
	{
		private ProgressDialog nDialog;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		nDialog=new ProgressDialog(UserInfoActivity.this);
		nDialog.setTitle("Checking Network");
		nDialog.setMessage("Loading...");
		nDialog.setIndeterminate(false);
		nDialog.setCancelable(true);
		nDialog.show();
	}
	
	//Future Scope Global network check and Global Process dialogs.
	@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
		try
		{
			URL url = new URL("http://www.google.com");
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setConnectTimeout(3000);
            urlc.connect();
            if (urlc.getResponseCode() == 200) {
                return true;
			}
		}
			catch(Exception ex)
			{
				Log.e("Runtime problem", "exception in netcheck code"+ex);
			}
			return false;
		}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		nDialog.dismiss();
		if(result==true)
		{
			updateInfo();
		}
		else
		{			
			MyGlobal.showAlert(UserInfoActivity.this,"Notice","No Internet Connection");
		}
	}
	}
	
	class SendAccountInfoAtServer extends AsyncTask<String, Void, JSONObject>	{
		JSONParser jparser=new JSONParser();
		
		private ProgressDialog pDialog;
		private static final String PROFILE_URL="http://special11.com/EcycleBooks/accountinfo.php";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog=new ProgressDialog(UserInfoActivity.this);
			pDialog.setTitle("Updating");
			pDialog.setMessage("Saving Information...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
						
			HashMap<String, String> parameters=new HashMap<String, String>();
			parameters.put("userid", userid);
			parameters.put("name", name);
			parameters.put("password", pass);
			parameters.put("contact", contact);
			parameters.put("city", city);
			parameters.put("school", school);			
			JSONObject json=jparser.getJSONFromUrl(PROFILE_URL, "POST", parameters);
			return json;
		}
		
		@Override
		protected void onPostExecute(JSONObject json) {	
			if(json!=null)
			{
				boolean error=Boolean.parseBoolean(json.optString("error"));
				String status=json.optString("status");
				if(error)
				{
					String errmsg=json.optString("error_msg");
					MyGlobal.showAlert(UserInfoActivity.this,"Notice",errmsg);			
				}
				else
				{
					if(status.equals("success"))
					{
							//login in sharedpreference and Go To Home Activity
							SessionManager sm=new SessionManager(UserInfoActivity.this);
							sm.loginSharedPreference(userid);
							Intent i=new Intent(UserInfoActivity.this,MainActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(i);
					}
				}
				
			}
			//stopping progress dialog
			if(pDialog !=null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
