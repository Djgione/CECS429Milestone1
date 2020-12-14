package cecs429.classification;


import cecs429.documents.Document;


public class Category {
	private String category;
	private double score;
	private Document doc;
	public Category(String c, double s,Document d)
	{       
		score = s;
		doc = d;
		category = c;
	}
	
	public String getPath() {return category;}
	
	public double getScore() {return score;}
	
	public void setDataDoc(Document d)
	{
		doc = d;
	}
}
