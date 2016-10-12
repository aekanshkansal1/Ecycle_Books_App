//This class deals with session using SharedPreference
package com.aekanshkansal.ecyclebooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	
	SharedPreferences myshareddata;
	Editor ed;
	Context mycontext;
	
	private static final String PREF_NAME="MYPREFERENCE";
	private static final String KEY_ISLOGGEDIN="isloggedin";
	private static final String KEY_USERID="userid";
	
	public SessionManager(Context context)
	{
	this.mycontext=context;	
	//data will be store in DATA/data/[application package name]/shared_prefs/[application package name]_MYPREFERENCE.xml. see from file explorer.
	myshareddata=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);  //getting SharedPreference Object for file EcycleBooks.MYPREFERENCE
	}
	
	//To login user in shared preference
	public void loginSharedPreference(String userid)
	{
		ed=myshareddata.edit();
		//data will be store in DATA/data/[application package name]/shared_prefs/[application package name]_MYPREFERENCE.xml. see from file explorer.
		ed.putBoolean("isloggedin", true);
		ed.putString("userid", userid);
		ed.commit();
		Intent i=new Intent(mycontext,MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		mycontext.startActivity(i);
	}
	
	//To logout user in shared preference
	public void logoutSharedPreference()
	{
		ed=myshareddata.edit();
		ed.clear();
		ed.commit();
		Intent i=new Intent(mycontext,MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		mycontext.startActivity(i);
	}
	
	public boolean isLoggedIn()
	{
		//if no value present give value as false
		return myshareddata.getBoolean(KEY_ISLOGGEDIN, false);
	}
	
	public String getUserDetail()
	{
		//if no value present gives -1
		return myshareddata.getString(KEY_USERID,"-1");
	}

}
