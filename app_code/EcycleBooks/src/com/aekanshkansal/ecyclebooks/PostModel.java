package com.aekanshkansal.ecyclebooks;

//Model for ListBooks
public class PostModel {

	String userid;
	String booktype;	
	String mrp;
	String price;
	String edition;
	String city;
	String college;
	
	public String getUserId()
	{
		return this.userid;
	}
	public String getBookType()
	{
		return this.booktype;
	}
	
	public String getMrp()
	{
		return this.mrp;
	}
	public String getPrice()
	{
		return this.price;
	}
	public String getEdition()
	{
		return this.edition;
	}
	public String getCity()
	{
		return this.city;
	}
	public String getCollege()
	{
		return this.college;
	}
	
}
