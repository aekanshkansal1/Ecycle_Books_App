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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends Activity{

	String mailid="",password="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);
		Button signIn=(Button)findViewById(R.id.button1);
		signIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//validations
				EditText mailedit=(EditText)findViewById(R.id.editText1);
				EditText passedit=(EditText)findViewById(R.id.editText2);
				mailid=mailedit.getText().toString();
				password=passedit.getText().toString();
				if(mailid.isEmpty())
				{
					MyGlobal.showAlert(SignInActivity.this,"Notice","Please Enter E-mail Id");
				}
				else if(!mailid.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
				{
					MyGlobal.showAlert(SignInActivity.this,"Notice","Please Enter Valid E-mail Id");			
				}
				else if(password.isEmpty())
				{
					MyGlobal.showAlert(SignInActivity.this,"Notice","Please Enter Password");
				}
				else if(password.length()<6 || password.length()>32)
				{
					MyGlobal.showAlert(SignInActivity.this,"Notice","Password can be of 6 to 32 Characters Only");
				}
				else
				{
				new NetCheck().execute();
				}

			}
		});
	}
	
	
	public void loginAtServer()
	{
		new SendLoginAtServer().execute(mailid,password);
	}
	
	private class NetCheck extends AsyncTask<String, Void, Boolean>
	{
		private ProgressDialog nDialog;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		nDialog=new ProgressDialog(SignInActivity.this);
		nDialog.setTitle("Checking Network");
		nDialog.setMessage("Loading...");
		nDialog.setIndeterminate(false);
		nDialog.setCancelable(true);
		nDialog.show();
	}
	
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
			loginAtServer();
		}
		else
		{
			MyGlobal.showAlert(SignInActivity.this,"Notice","No Internet Connection");			
		}
	}
	
	}
	
	class SendLoginAtServer extends AsyncTask<String, Void, JSONObject>	{
		//creating JSONParser class object
		JSONParser jparser=new JSONParser();
		
		private ProgressDialog pDialog;
		//url for login on server
		private static final String LOGIN_URL="http://yoursite/EcycleBooks/login.php";
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog=new ProgressDialog(SignInActivity.this);
			pDialog.setTitle("Login");
			pDialog.setMessage("Authenticating...");
			pDialog.setIndeterminate(false); //true means indeterminate i.e.a spinner rotate don't know how much time is taken and false stands for determinate.
			pDialog.setCancelable(true);     //progessdialog is cancelable on clicking outside box
			pDialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			
			String mailidlocal=params[0];
			String passwordlocal=params[1];
			HashMap<String, String> parameters=new HashMap<String, String>();
			parameters.put("mailid", mailidlocal);
			parameters.put("password", passwordlocal);			
			JSONObject json=jparser.getJSONFromUrl(LOGIN_URL, "POST", parameters);
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
					MyGlobal.showAlert(SignInActivity.this,"Notice",errmsg);			
				}
				else
				{
					if(status.equals("success"))
					{
						String userstatus=json.optString("userstatus");    //getting user status
						String userid=json.optString("userid");  //getting user id						
						if(userstatus.equals("U"))
						{
							Intent i=new Intent(SignInActivity.this,UserInfoActivity.class);   //if status unverified go to account info
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
							i.putExtra("ecyclebooks.userid", userid);
							startActivity(i);
							//finish();
						}
						else
						{
							//login in sharedpreference and Go To Home Activity
							SessionManager sm=new SessionManager(SignInActivity.this);
							sm.loginSharedPreference(userid);
						}
					}
				}
				
			}
			if(pDialog !=null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}

		}
		
	}
    //FUTURE SCOPE: Creating Global Menu
	   @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.main, menu);
	        MenuItem signin=menu.findItem(R.id.action_signin);
        	signin.setEnabled(false);
        	signin.setVisible(false);
	        MenuItem logout=menu.findItem(R.id.action_logout);
	        //If user already logged In so can't reach SignInActivity so User is not logged in.Hence disable signin and logout button.
        	signin.setVisible(false);
        	signin.setEnabled(false);
        	logout.setEnabled(false);
        	logout.setVisible(false); 
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
	        if(id==R.id.action_signup)
	        {
	        	Intent i=new Intent(SignInActivity.this,SignUpActivity.class);
	        	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(i);
	        }
	        return super.onOptionsItemSelected(item);
	    } 

}
