package com.aekanshkansal.ecyclebooks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MyGlobal {
	//creating context variable to get the context of activity from where called.
	//Context mycontext;
	
	//initializing context
	/*public MyGlobal(Context context)
	{
		this.mycontext=context;
	}*/

	//Global Alert Dialog
	public static void showAlert(Context context,String title,String msg)
	{
		AlertDialog.Builder ab=new AlertDialog.Builder(context);
		ab.setTitle(title);
		ab.setMessage(msg);
		ab.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		ab.show();
	}
	
}
