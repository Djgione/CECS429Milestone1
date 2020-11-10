package cecs429.weights;

public class Accumulator {

	private int docId;
	private double aValue;
	public Accumulator(int docId, double aValue) {
		super();
		this.docId = docId;
		this.aValue = aValue;
	}
	public int getDocId() {
		return docId;
	}
	public double getaValue() {
		return aValue;
	}
	public void setaValue(double aValue) {
		this.aValue = aValue;
	}
	
	
}
