//This Class returns the JSON Object(Storing the Json data) From requested URL and also post data to URL if required.
//call getJSONfromUrl method and pass the url, request type(GET/POST) and hashmap of data to be sent.
package com.aekanshkansal.ecyclebooks;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
static JSONObject jObj=null;
HttpURLConnection conn;

//constructor
public JSONParser()
{
	
}

//This method returns the JSONObject(containing data) from the URL hit by parameter url.
public JSONObject getJSONFromUrl(String url,String method,HashMap<String, String>params)
{	
	//If we need to pass data to Server.
	if(params!=null)
	{
		
		//To encode data to StringBuilder from HashMap recieved WAY1
		/*
		StringBuilder sentData=new StringBuilder();
		boolean first=true;
		//To get the key and value from map we use entrySet()
		for(Map.Entry<String, String> entry:params.entrySet())
		{
		if(first)
		{
			first=false;
		}
		else
		{
			sentData.append("&");
		}
		sentData.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
		sentData.append("=");
		sentData.append(URLEncoder.encode(entry.getValue(), "UTF-8"));			
		}  
		*/
		
		//Encoding data to StringBuilder from HashMap WAY2
		StringBuilder sentData=new StringBuilder();
		boolean first=true;
		//for 1st parameter don't append '&' symbol 
		for(String key:params.keySet())
		{
			try
			{
			if(!first)
			{
				sentData.append("&");
			}
			first=false;
            sentData.append(key).append("=")
            .append(URLEncoder.encode(params.get(key), "UTF-8"));
			}catch(UnsupportedEncodingException ex){}
		}
	
		//If data is sent using POST method
		if(method.equals("POST"))
		{
		//HTTP Request using HttpURLConnection
		try
		{			
			URL urlObj = new URL(url);				//creating a url object
			conn = (HttpURLConnection)urlObj.openConnection();			//returns a object of type URLConnection. Method of URL class.
			conn.setDoInput(true);		//setting that URLConnection will accept input
			conn.setDoOutput(true);				//setting that URLConnection will accept Output
			conn.setReadTimeout(10000);		//setting maxm time for readout
			conn.setRequestMethod("POST");		//Setting Request method as post
			conn.setConnectTimeout(15000);		//setting maxm connection time
			conn.connect();		//connecting to URl
			//Writing data to Server WAY1
			/*
		 
			//getting OutputStream Object to be used with BufferedWriter
			//returns an OutputStream Object to the URl using getOutputStream() method of URL class
			OutputStream outstream=conn.getOutputStream();
			//Creating a BufferedWriter that will write to URL using OutputStream obtained from conn.getOutputStream() 
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outstream, "UTF-8"));
			//Getting data from Hashmap in StringBuilder
			writer.write(sentData.toString());
			writer.flush();
			writer.close();
			outstream.close();
		
		 	*/

				//Writing data to Server Way2
				DataOutputStream writer=new DataOutputStream(conn.getOutputStream());
				writer.writeBytes(sentData.toString());
				writer.flush();
				writer.close();
			}
			catch(Exception ex){ Log.e("Connection Exception","Exception is "+ex);}		
		
		}
		else if(method.equals("GET"))
		{
			//If request method is get parameter appended in URL
			if(sentData.length()!=0)
			{
				url += "?"+sentData.toString();
			}
			try
			{
				URL urlObj=new URL(url);
				conn=(HttpURLConnection) urlObj.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("GET");
				conn.connect();
			}
			catch(Exception ex){Log.e("Connection Exception","Exception is "+ex);}
		}		
	}
	
	//Getting Result from Server
	try
	{
		BufferedReader reader=new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getInputStream())));
		StringBuilder result=new StringBuilder();
		String line;
		while((line=reader.readLine())!=null)
		{
			result.append(line);
		}
		Log.d("json",result.toString());
		conn.disconnect();
		
		//Converting String to JSON Object
		jObj=new JSONObject(result.toString());
	}
	catch(Exception ex){
		Log.e("Reading Json Exception", "exception in Reading from server"+ex);	
	}
	//returning JSONObject
	return jObj;
}
}