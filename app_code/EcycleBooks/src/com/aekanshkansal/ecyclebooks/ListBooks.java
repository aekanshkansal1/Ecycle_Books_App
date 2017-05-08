package com.aekanshkansal.ecyclebooks;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListBooks extends Activity {

	String id,bookName,author;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_books);
		Intent i=getIntent();
		String arr[]=i.getStringArrayExtra("bookName");
		id=arr[0];        //getting Book id
		bookName=arr[1];    //getting Book Name
		author=arr[2];   //geting Author
		TextView tv1=(TextView)findViewById(R.id.textView1);
		tv1.setText("Book Name- "+bookName);
		TextView tv2=(TextView)findViewById(R.id.textView2);
		tv2.setText("Author-    "+author);
		
		new NetCheck().execute();
	}

	//--------------------------------------------------------------------------------------------------------------------
	
	public void searchAtServer()
	{
		new getListFromServer().execute();
	}
	
	private class NetCheck extends AsyncTask<String, Void, Boolean>
	{
		private ProgressDialog nDialog;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		nDialog=new ProgressDialog(ListBooks.this);
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
			searchAtServer();
		}
		else
		{
			MyGlobal.showAlert(ListBooks.this,"Notice","No Internet Connection");			
		}
	}
	
	}
	
	class getListFromServer extends AsyncTask<String, Void, JSONObject>	{
		//creating JSONParser class object
		JSONParser jparser=new JSONParser();
		
		private ProgressDialog pDialog;

		//url for login on server
		private static final String POST_URL="http://special11.com/EcycleBooks/listbooks.php";

		//This adapter is just used to get the data and store whole data.
		//This is not used to show data as we will need to create custom adapter
		List<PostModel> postlist=new ArrayList<PostModel>();

		//Object of Custom Adapter
		CustomListAdapter adapter;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog=new ProgressDialog(ListBooks.this);
			pDialog.setTitle("Finding Posts");
			pDialog.setMessage("Connecting...");
			pDialog.setIndeterminate(false); //true means indeterminate i.e.a spinner rotate don't know how much time is taken and false stands for determinate.
			pDialog.setCancelable(true);     //progessdialog is cancelable on clicking outside box
			pDialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			
			HashMap<String, String> parameters=new HashMap<String, String>();
			parameters.put("bookid", id);
			JSONObject json=jparser.getJSONFromUrl(POST_URL, "POST", parameters);
			return json;
		}
		
		@Override
		protected void onPostExecute(JSONObject json) {
			ListView lv1=(ListView)findViewById(R.id.listView1);
			if(json!=null)
			{
				boolean error=Boolean.parseBoolean(json.optString("error"));
				String status=json.optString("status");
				if(error)
				{
					String errmsg=json.optString("error_msg");
					MyGlobal.showAlert(ListBooks.this,"Notice",errmsg);			
				}
				else
				{
					//Get data and show in list view
					if(status.equals("success"))
					{
						try
						{
							//getting books array
							JSONArray postarr=json.getJSONArray("posts");
							//traversing array and getting the data
							for(int i=0;i<postarr.length();i++)
							{
								//getting JSON Object corresponding to all books one by one
								JSONObject details=postarr.getJSONObject(i);
								PostModel p=new PostModel();
								//getting the data from object and storing as BookModel Object
								p.userid=details.optString("userid");
								p.booktype=details.optString("booktype");
								p.mrp=details.optString("mrp");
								p.price=details.optString("price");
								p.edition=details.optString("edition");
								p.city=details.optString("city");
								p.college=details.optString("college");
								postlist.add(p);
							}							
						}
						catch(Exception ex)
						{
							Log.e("Error"," Error in parsing Json Array Book"+ex);
						}
					}
					else
					{
						postlist.clear();
						Toast.makeText(ListBooks.this, "No Data Found", Toast.LENGTH_LONG).show();
					}
					//Setting data
					adapter=new CustomListAdapter(ListBooks.this, postlist);
					lv1.setAdapter(adapter);
				}
				
			}
			if(pDialog !=null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}
			
			lv1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id)
			{
				Log.e("Hello","onclick");
				Intent p=new Intent(ListBooks.this,NeedBookActivity.class);
				String us=String.valueOf(adapter.getItemId(position));					
				//Getting value of Object in array
				String postSelect[]={us,bookName,author}; 				
				//sending data to next page
				p.putExtra("post", postSelect);
				startActivity(p);

			}
			});
		}
		
		
	}

	//--------------------------------------------------------------------------------------------------------------------
	
	
	
	
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
	        SessionManager sm=new SessionManager(ListBooks.this);
	        sm.logoutSharedPreference();
        }
		return super.onOptionsItemSelected(item);
	}
}
