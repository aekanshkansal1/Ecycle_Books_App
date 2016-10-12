package com.aekanshkansal.ecyclebooks;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CustomPagerAdapter extends PagerAdapter{
Context mycontext;
LayoutInflater myLayoutInflator;
int[] myResource = {
		R.drawable.cover1,
		R.drawable.cover2,
		R.drawable.cover3,
		R.drawable.cover4
};

public CustomPagerAdapter(Context context)
{
	//getting context of class
	mycontext=context;
	//getting layout inflator of class
	myLayoutInflator=(LayoutInflater)mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
}

//return number of pages to be displayed in view pager
@Override
public int getCount() {
	// TODO Auto-generated method stub
	return myResource.length;
}


@Override
public boolean isViewFromObject(View view, Object object) {
	// TODO Auto-generated method stub
	return view==((LinearLayout)object);
}

//To set the view for next item
@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		//setting the pager_item.xml on container
		View itemView=myLayoutInflator.inflate(R.layout.pager_item, container,false);
		//setting the image in pager_item as per position of viewPager.
		ImageView imgView=(ImageView)itemView.findViewById(R.id.img_pager_item);
		imgView.setImageResource(myResource[position]);
		//adding the view got by inflate() method to Main View
		container.addView(itemView);
		return itemView;
	}

//To destroy view for previous item
@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView((LinearLayout)object);
	}
}
