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
import android.widget.Toast;


public class SignUpActivity extends Activity {

	String mailid="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		Button signupbtn=(Button)findViewById(R.id.button1);
		//getting mail id when clicked on signup and register at Server.
		signupbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText mailedit=(EditText)findViewById(R.id.editText1);
				mailid=mailedit.getText().toString();
				//validation
				if(mailid.isEmpty())
				{
					MyGlobal.showAlert(SignUpActivity.this, "Notice","Please enter Email-Id");
				}
				//Not working
				//	else if(!android.util.Patterns.EMAIL_ADDRESS.matcher("mailid").matches())  //using android built in pattern to validate mailid
				else if(!mailid.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
				{
					MyGlobal.showAlert(SignUpActivity.this,"Notice","Please Enter valid E-mail Id");
				}
				else
				{
				//checking if Net is on or not. If net is on then only try to send data to server.
				new NetCheck().execute();
				}
			}
		});
	}
		
	//method to register mail id on server
	public void registerAtServer()
	{
		//creating object of Aysnctask class in method and calling methods of this class using the object.
		SendRegisterAtServer obj=new SendRegisterAtServer();
		//passing mailid to execute methods of AsyncTask. This mail id is passed to doInBackground method after onPreExceute() complete excution.
		obj.execute(mailid);
	}

	private class NetCheck extends AsyncTask<String, Void, Boolean>
	{
		private ProgressDialog nDialog;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		nDialog=new ProgressDialog(SignUpActivity.this);
		nDialog.setTitle("Checking Network");
		nDialog.setMessage("Loading...");
		nDialog.setIndeterminate(false);
		nDialog.setCancelable(true);
		nDialog.show();
	}
	
	//Future Scope Global network check and Global Process dialogs.
	@Override
		protected Boolean doInBackground(String... params) {
		//This method is used to check internet connection pinging google public DNS Server 8.8.8.8
//		Toast.makeText(SignUpActivity.this, "Net Check Background method", Toast.LENGTH_SHORT).show();
			// TODO Auto-generated method stub
			//If net on then return true else false
		try
		{
			/*
			 * Ping works on different devices differently so not used. It is based on Original Equipment Manufaturer.
			Runtime runtime=Runtime.getRuntime();
				Process ipProcess=runtime.exec("/system/bin/ping -c 1 8.8.8.8");
				int exitValue=ipProcess.waitFor();
				System.out.println("exit value is"+exitValue);
				return (exitValue==0);*/
			//Try Connecting to google
			//Using connectivity manager to check the network connection status use NETWORK_STATE_ACCESS permission.
			/*ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo=cm.getActiveNetworkInfo();
			if(netInfo!=null && netInfo.isConnected())
			{*/
			URL url = new URL("http://www.google.com");
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setConnectTimeout(3000);
            urlc.connect();
            if (urlc.getResponseCode() == 200) {
                return true;
			}
			//}
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
			registerAtServer();
		}
		else
		{			
			MyGlobal.showAlert(SignUpActivity.this,"Notice","No Internet Connection");
		}
	}
	
	}
	
	//FUTURE Scope: Using doInBackground with out passing parameter
	
	//AsyncTask is abstract class in android used to do operations that takes time and it is kept away from the  main thread.
	//AsyncTask generics would be params(input type of doInBackground method),progress(Input type of onProgressUpdate method which is called on calling publishProgress(y))
	//and result(This is input type of onPostExecute()/This is result of background operation ie return type of doInBackground here JSONObject type)
	class SendRegisterAtServer extends AsyncTask<String, Void, JSONObject>	{
		//creating JSONParser class object
		JSONParser jparser=new JSONParser();
		
		//creating progress dialog to show progress
		private ProgressDialog pDialog;
		//url for register on server
		private static final String REGISTER_URL="http://special11.com/EcycleBooks/register.php";
		
		//This method is executed before doInBackground
		//ProgressDialog is shown to users here
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog=new ProgressDialog(SignUpActivity.this);
			pDialog.setTitle("Register");
			pDialog.setMessage("Registering...");
			pDialog.setIndeterminate(false); //true means indeterminate i.e.a spinner rotate don't know how much time is taken and false stands for determinate.
			pDialog.setCancelable(true);     //progessdialog is cancelable on clicking outside box
			pDialog.show();
		}
		
		//This method is executed when executing AsyncTask automatically
		//This method is used for making request to URL
		//Toast create error if used in this
		@Override
		protected JSONObject doInBackground(String... params) {
			
			//mailid will be in params(String array) so getting it from there.				
			String mailidlocal=params[0];

			//Creating the Hashmap
			HashMap<String, String> parameters=new HashMap<String, String>();
			parameters.put("mailid", mailidlocal);
			//requesting the server using getJSONFromUrl and getting JSONObject containing JSON data which would be
			//parsed in onPostExecute()
			JSONObject json=jparser.getJSONFromUrl(REGISTER_URL, "POST", parameters);
			return json;
		}
		
		//executed after doInBackground().Input parameter is Output from doInBackground method. Used for parsing JSON data. JSONObject passed here.
		@Override
		protected void onPostExecute(JSONObject json) {			
			//sending JSONObject for parsing or parse here simply and show response in Toast
			if(json!=null)
			{
				//return the string if exist else returns empty string. No Exception is thrown like getString if no string exist.
				boolean error=Boolean.parseBoolean(json.optString("error"));
				String status=json.optString("status");
				if(error)
				{
					String errmsg=json.optString("error_msg");
					MyGlobal.showAlert(SignUpActivity.this,"Error",errmsg);					
				}
				else
				{
					if(status.equals("success"))
					{
						//Future Scope Way to go to gmail directly
						Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT);
						AlertDialog.Builder ab=new AlertDialog.Builder(SignUpActivity.this);
						ab.setTitle("Directions");
						ab.setMessage("Your Password is sent to your E-mail Id.\nPlease login using it and change your password.");
						ab.setPositiveButton("OK",new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Intent i=new Intent(SignUpActivity.this,SignInActivity.class);
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
	
    //FUTURE SCOPE: Creating Global Menu
	   @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds all items from main.xml in menu folder(as got from R.menu.main) to the action bar.
	        getMenuInflater().inflate(R.menu.main, menu);
	        //getting Items from menu
	        MenuItem logout=menu.findItem(R.id.action_logout);
	        MenuItem signup=menu.findItem(R.id.action_signup);
	        //If user already logged In so can't reach SignUpActivity so User is not logged in.Hence disable signup and logout button.
        	signup.setVisible(false);
        	signup.setEnabled(false);
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
	        if(id==R.id.action_signin)
	        {
	        	Intent i=new Intent(SignUpActivity.this,SignInActivity.class);
	        	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(i);
	        }
	        return super.onOptionsItemSelected(item);
	    } 
}
