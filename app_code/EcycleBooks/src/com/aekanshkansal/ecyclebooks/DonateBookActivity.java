package com.aekanshkansal.ecyclebooks;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DonateBookActivity extends Activity {

	String bookid,edition;
	EditText bkedition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donate_book);
		Intent i=getIntent();
		String bookSelect[]=i.getStringArrayExtra("bookName");
		bookid=bookSelect[0];
		EditText bkname=(EditText)findViewById(R.id.EditText1);
		EditText bkauthor=(EditText)findViewById(R.id.EditText2);
		EditText bkpublication=(EditText)findViewById(R.id.EditText3);
		bkedition=(EditText)findViewById(R.id.EditText4);
		Button donate=(Button)findViewById(R.id.button1);
		
		bkname.setText(bookSelect[1]);
		bkauthor.setText(bookSelect[2]);
		bkpublication.setText(bookSelect[3]);

		donate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edition=bkedition.getText().toString();
				if(edition.isEmpty() || edition.length()>4)
				{
					MyGlobal.showAlert(DonateBookActivity.this,"Notice","Invalid Edition");
				}
				else
				{
					new NetCheck().execute();
				}
			}
		});
	}

	
	public void bookDonate()
	{
		new DonateBookServer().execute();
	}
	
	private class NetCheck extends AsyncTask<String, Void, Boolean>
	{
		private ProgressDialog nDialog;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		nDialog=new ProgressDialog(DonateBookActivity.this);
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
			bookDonate();
		}
		else
		{			
			MyGlobal.showAlert(DonateBookActivity.this,"Notice","No Internet Connection");
		}
	}
	}
	
	class DonateBookServer extends AsyncTask<String, Void, JSONObject>	{
		JSONParser jparser=new JSONParser();
		
		private ProgressDialog pDialog;
		private static final String POST_DONATE_URL="http://yoursite_webservice/donatebook.php";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog=new ProgressDialog(DonateBookActivity.this);
			pDialog.setTitle("Posting");
			pDialog.setMessage("Saving Information...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			
			HashMap<String, String> parameters=new HashMap<String, String>();
			SessionManager sm=new SessionManager(DonateBookActivity.this);
			String userid=sm.getUserDetail();
			parameters.put("userid", userid);
			parameters.put("bookid", bookid);
			parameters.put("edition", edition);		
			JSONObject json=jparser.getJSONFromUrl(POST_DONATE_URL, "POST", parameters);
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
					MyGlobal.showAlert(DonateBookActivity.this,"Notice",errmsg);			
				}
				else
				{
					if(status.equals("success"))
					{
						AlertDialog.Builder ab=new AlertDialog.Builder(DonateBookActivity.this);
						ab.setTitle("Success");
						ab.setMessage("Your Book is posted for donation");
						ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Intent i=new Intent(DonateBookActivity.this,MainActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(i);
							}
						});
						ab.show();
						
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
	        SessionManager sm=new SessionManager(DonateBookActivity.this);
	        sm.logoutSharedPreference();
        }
		return super.onOptionsItemSelected(item);
	}
}
