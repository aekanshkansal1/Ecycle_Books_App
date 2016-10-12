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
import android.widget.TextView;

public class NeedBookActivity extends Activity {

	String userid;
	TextView contact,mail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_need_book);
		TextView bname=(TextView)findViewById(R.id.textView1);
		TextView author=(TextView)findViewById(R.id.textView2);
		contact=(TextView)findViewById(R.id.textView3);
		mail=(TextView)findViewById(R.id.textView4);
		Intent i=getIntent();
		String details[]=i.getStringArrayExtra("post");
		userid=details[0];
		bname.setText("Book:   "+details[1]);
		author.setText("Author: "+details[2]);
		new NetCheck().execute();
	}

	
	public void getContact()
	{
		new GetContact().execute();
	}
	
	private class NetCheck extends AsyncTask<String, Void, Boolean>
	{
		private ProgressDialog nDialog;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		nDialog=new ProgressDialog(NeedBookActivity.this);
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
			getContact();
		}
		else
		{
			MyGlobal.showAlert(NeedBookActivity.this,"Notice","No Internet Connection");			
		}
	}
	
	}
	
	class GetContact extends AsyncTask<String, Void, JSONObject>	{
		//creating JSONParser class object
		JSONParser jparser=new JSONParser();
		
		private ProgressDialog pDialog;

		//url for login on server
		private static final String POST_URL="http://yoursite_webservice/getcontact.php";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog=new ProgressDialog(NeedBookActivity.this);
			pDialog.setTitle("Getting Details");
			pDialog.setMessage("Connecting...");
			pDialog.setIndeterminate(false); //true means indeterminate i.e.a spinner rotate don't know how much time is taken and false stands for determinate.
			pDialog.setCancelable(true);     //progessdialog is cancelable on clicking outside box
			pDialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			
			HashMap<String, String> parameters=new HashMap<String, String>();
			parameters.put("userid", userid);
			JSONObject json=jparser.getJSONFromUrl(POST_URL, "POST", parameters);
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
					MyGlobal.showAlert(NeedBookActivity.this,"Notice",errmsg);			
				}
				else
				{
					//Get data and show in list view
					if(status.equals("success"))
					{
						try
						{
							contact.setText(json.optString("contact"));
							mail.setText(json.optString("mailid"));
								//getting the data from object and storing as BookModel Object							
						}
						catch(Exception ex)
						{
							Log.e("Error"," Error in parsing Json Contact Info"+ex);
						}
					}
					
					
				}
				
			if(pDialog !=null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}
			
		}
		}
		
	}
			
			
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.choose_book, menu);
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
		        if(id==R.id.action_logout)
		        {
			        SessionManager sm=new SessionManager(NeedBookActivity.this);
			        sm.logoutSharedPreference();
		        }
				return super.onOptionsItemSelected(item);
			}		
	}

