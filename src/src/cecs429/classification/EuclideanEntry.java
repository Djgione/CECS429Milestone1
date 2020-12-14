package cecs429.classification;

import cecs429.documents.Document;

public class EuclideanEntry {
	private Document doc;
	private double distance;
	
	public EuclideanEntry(Document d, double dist)
	{
		doc = d;
		distance = dist;
	}
	
	public Document getDoc() {return doc;}
	
	public double getEuclideanDistance() {return distance;}
	
}
