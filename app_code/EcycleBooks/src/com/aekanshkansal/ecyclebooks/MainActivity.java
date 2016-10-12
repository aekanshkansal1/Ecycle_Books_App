package com.aekanshkansal.ecyclebooks;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

	SessionManager sm;
	CustomPagerAdapter myCustomPagerAdapter; 
	ViewPager myViewPager;
	int count=0;   //For timer
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCustomPagerAdapter=new CustomPagerAdapter(this);
        myViewPager=(ViewPager)findViewById(R.id.pager);
        myViewPager.setAdapter(myCustomPagerAdapter);
        //Automated Sliding
        //setting current image or item
        myViewPager.setCurrentItem(0);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                    	if(count>=3)
                    	{
                    		count=0;
                    	}
                    	else
                    	{
                    		count++;
                    	}
                    	myViewPager.setCurrentItem(count);
                    }
				});
			}
		}, 2000, 2000);          //takes three argument. Task, delay for first task and period
        sm=new SessionManager(this);
        Button b1=(Button)findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sm.isLoggedIn())
				{
				Intent i=new Intent(MainActivity.this,ChooseBook.class);
				i.putExtra("type", "sell");
				startActivity(i);
				}
				else
				{
					MyGlobal.showAlert(MainActivity.this,"Notice","Please Login to continue");			
				}
			}
		});
        Button b2=(Button)findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sm.isLoggedIn())
				{
				Intent i=new Intent(MainActivity.this,ChooseBook.class);
				i.putExtra("type", "donate");
				startActivity(i);
				}
				else
				{
					MyGlobal.showAlert(MainActivity.this,"Notice","Please Login to continue");			
				}			
			}
		});
        
        Button b3=(Button)findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sm.isLoggedIn())
				{
				Intent i=new Intent(MainActivity.this,ChooseBook.class);
				i.putExtra("type", "need");
				startActivity(i);
				}
				else
				{
					MyGlobal.showAlert(MainActivity.this,"Notice","Please Login to continue");			
				}								
			}
		});
    }
    
    //FUTURE SCOPE: Creating Global Menu
	   @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds all items from main.xml in menu folder(as got from R.menu.main) to the action bar.
	        getMenuInflater().inflate(R.menu.main, menu);
	        //getting Items from menu
	        MenuItem signin=menu.findItem(R.id.action_signin);
	        MenuItem signup=menu.findItem(R.id.action_signup);
	        MenuItem logout=menu.findItem(R.id.action_logout);
	        SessionManager sm=new SessionManager(MainActivity.this);
	        //If user is logged only logout button will be shown and if not logged in Sign In and Sign Up will be shown. So showing and removing menu items
	        if(sm.isLoggedIn())
	        {
	        	signin.setVisible(false);
	        	signin.setEnabled(false);
	        	signup.setEnabled(false);
	        	signup.setVisible(false);
	        }
	        else
	        {
	        	logout.setEnabled(false);
	        	logout.setVisible(false);
	        }
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
	        	Intent i=new Intent(MainActivity.this,SignInActivity.class);
	        	startActivity(i);
	        }
	        if(id==R.id.action_logout)
	        {
		        SessionManager sm=new SessionManager(MainActivity.this);
		        sm.logoutSharedPreference();
	        }
	        if(id==R.id.action_signup)
	        {
	        	Intent i=new Intent(MainActivity.this,SignUpActivity.class);
	        	startActivity(i);	        	
	        }
	        return super.onOptionsItemSelected(item);
	    }    
    
}