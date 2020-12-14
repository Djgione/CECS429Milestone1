package classifications;

import java.util.ArrayList;
import java.util.List;

import cecs429.documents.Document;

public class DocumentVector {
	private Document dataDoc;
	private List<Double> wdtComponents = new ArrayList<Double>();
	
	public DocumentVector(Document doc, List<Double> comps)
	{
		dataDoc = doc;
		wdtComponents = comps;
	}
	
	public Document getDataDoc() {return dataDoc;}
	
	public List<Double> getComponents() {return wdtComponents;}
	
}