package com.aekanshkansal.ecyclebooks;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter
{
	private Activity activity;
	private LayoutInflater inflater;
	private List<PostModel> posts;
	public CustomListAdapter(Activity act,List<PostModel> posts) {
		// TODO Auto-generated constructor stub
	this.activity=act;
	this.posts=posts;
	}

	//getting number of element to populate the list view with
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return posts.size();
	}

	//returns the object on the position
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return posts.get(position);
	}

	
	//when call getItem id it cowill return the userid which will be passed on to next activity
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Long.parseLong(posts.get(position).getUserId());
	}

	//return the view for each row
	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(inflater==null)
		{
			inflater=(LayoutInflater)activity.getLayoutInflater();
		}
		if(convertView==null)          //if the row is not reusable then convertview will be null so create a new View object for row
		{
			convertView=inflater.inflate(R.layout.list_item, null);
		}
		//getting all the TextView Object from convertview
		TextView sr=(TextView)convertView.findViewById(R.id.textView7);
		TextView booktype=(TextView)convertView.findViewById(R.id.textView1);
		TextView mrp=(TextView)convertView.findViewById(R.id.textView2);
		TextView price=(TextView)convertView.findViewById(R.id.textView3);
		TextView edition=(TextView)convertView.findViewById(R.id.textView4);
		TextView city=(TextView)convertView.findViewById(R.id.textView5);
		TextView college=(TextView)convertView.findViewById(R.id.textView6);
		
		//getting the object of PostModel for specific row
		PostModel obj=posts.get(position);
		
		//setting the data for row
		sr.setText("S.No "+position);
		booktype.setText("Book Type: "+obj.getBookType());
		mrp.setText("MRP: "+obj.getMrp());
		price.setText("Price: "+obj.getPrice());
		edition.setText("Edition: "+obj.getEdition());
		city.setText("City: "+obj.getCity());
		college.setText("College: "+obj.getCollege());
		
		return convertView;
	}

}
