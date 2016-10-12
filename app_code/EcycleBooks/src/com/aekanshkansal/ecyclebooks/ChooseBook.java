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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChooseBook extends Activity {

	Button search;
	String keyword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_book);
	//	ImageButton add=(ImageButton)findViewById(R.id.imageButton1);
		Button add=(Button)findViewById(R.id.imageButton1);
		add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent previous=getIntent();
				String type=previous.getStringExtra("type");
				Intent i=new Intent(ChooseBook.this,AddBook.class);
				i.putExtra("type", type);
				startActivity(i);
			}
		});
		search=(Button)findViewById(R.id.button1);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText ed=(EditText)findViewById(R.id.editText1);
				keyword=ed.getText().toString();
				if(keyword.length()>=2)
				{
				new NetCheck().execute();
				}
				else
				{
					MyGlobal.showAlert(ChooseBook.this,"Notice","Keyword should be atleast 2 characters");				
				}
			}
		});
	}

	public void searchAtServer()
	{
		new getBooksFromServer().execute();
	}
	
	private class NetCheck extends AsyncTask<String, Void, Boolean>
	{
		private ProgressDialog nDialog;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		nDialog=new ProgressDialog(ChooseBook.this);
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
			MyGlobal.showAlert(ChooseBook.this,"Notice","No Internet Connection");			
		}
	}
	
	}
	
	class getBooksFromServer extends AsyncTask<String, Void, JSONObject>	{
		//creating JSONParser class object
		JSONParser jparser=new JSONParser();
		
		private ProgressDialog pDialog;
		//url for login on server
		private static final String SEARCH_URL="http://yoursite_webservice/searchbook.php";
		//This adapter is just used to get the data and store whole data.
		//This is not used to show data as we will need to create custom adapter
		List<BookModel> booklist=new ArrayList<BookModel>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog=new ProgressDialog(ChooseBook.this);
			pDialog.setTitle("Searching");
			pDialog.setMessage("Connecting...");
			pDialog.setIndeterminate(false); //true means indeterminate i.e.a spinner rotate don't know how much time is taken and false stands for determinate.
			pDialog.setCancelable(true);     //progessdialog is cancelable on clicking outside box
			pDialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			
			HashMap<String, String> parameters=new HashMap<String, String>();
			parameters.put("keyword", keyword);
			JSONObject json=jparser.getJSONFromUrl(SEARCH_URL, "POST", parameters);
			return json;
		}
		
		@Override
		protected void onPostExecute(JSONObject json) {
			//This list is used to create ArrayAdapter with String. It store author and book seperated by \n so need not use custom
			//adpater as both field store as single String
			ArrayList<String> arr=new ArrayList<String>();
			ListView lv=(ListView)findViewById(R.id.listView1);
			ArrayAdapter<String> arradapter;
			
			if(json!=null)
			{
				boolean error=Boolean.parseBoolean(json.optString("error"));
				String status=json.optString("status");
				if(error)
				{
					String errmsg=json.optString("error_msg");
					MyGlobal.showAlert(ChooseBook.this,"Notice",errmsg);			
				}
				else
				{
					//Get data and show in list view
					if(status.equals("success"))
					{
						try
						{
							//getting books array
							JSONArray bookarr=json.getJSONArray("books");
							//traversing array and getting the data
							for(int i=0;i<bookarr.length();i++)
							{
								//getting JSON Object corresponding to all books one by one
								JSONObject details=bookarr.getJSONObject(i);
								BookModel b=new BookModel();
								//getting the data from object and storing as BookModel Object
								b.bookId=details.optString("bookid");
								b.bookName=details.optString("bookname");
								b.bookAuthor=details.optString("author");
								b.bookPublication=details.optString("publication");
								booklist.add(b);
								arr.add(b.bookName+"\n"+"By-"+b.bookAuthor);
							}							
						}
						catch(Exception ex)
						{
							Log.e("Error"," Error in parsing Json Array Book"+ex);
						}
					}
					else
					{
						booklist.clear();
						arr.clear();
						Toast.makeText(ChooseBook.this, "No Data Found", Toast.LENGTH_LONG).show();
					}
					//Setting data
					//We can also set two items in list using simple_list_item2 type but this is easy way to set item and subitem
					arradapter=new ArrayAdapter<String>(ChooseBook.this,android.R.layout.simple_list_item_1,arr);
					lv.setAdapter(arradapter);
				}
				
			}
			if(pDialog !=null && pDialog.isShowing())
			{
				pDialog.dismiss();
			}
			
			//when clicked on List Item
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent i=getIntent();
					String type=i.getStringExtra("type");
					//Getting the object at the position clicked in booklist
					BookModel bm=booklist.get(position);
					//Getting value of Object in array
					String bookSelect[]={bm.getBookId(),bm.getBookName(),bm.getBookAuthor(),bm.getBookPublication()}; 
					Intent nextPage;
					if(type.equals("sell"))
					{
						nextPage=new Intent(ChooseBook.this,SellBookActivity.class);
					}
					else if(type.equals("donate"))
					{
						nextPage=new Intent(ChooseBook.this,DonateBookActivity.class);
					
					}
					else
					{
						nextPage=new Intent(ChooseBook.this,ListBooks.class);						
					}
					//sending data to next page
					nextPage.putExtra("bookName", bookSelect);
					startActivity(nextPage);
				}
			});
			
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
	        SessionManager sm=new SessionManager(ChooseBook.this);
	        sm.logoutSharedPreference();
        }
		return super.onOptionsItemSelected(item);
	}
}
