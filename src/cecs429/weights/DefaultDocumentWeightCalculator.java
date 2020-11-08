package cecs429.weights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DefaultDocumentWeightCalculator implements IDocumentWeightCalculator{

	//private Map<Integer,Map<String,Integer>> documentTermFrequencies;


//	public void setDocumentTermFrequencies(Map<Integer, Map<String, Integer>> documentTermFrequencies) {
//		this.documentTermFrequencies = documentTermFrequencies;
		
	
	

	public DefaultDocumentWeightCalculator() {
	
	}

	public List<Double> calculate(DocumentValuesModel model)
	{
	    Map<Integer, Map<String, Integer>> documentTermFrequencies = model.getMap();
		List<Double> map = new ArrayList<>();
		if(documentTermFrequencies.isEmpty())
			return map;
		

		int i = 1;
		// Retrieving the internal map with loop
		do {
			List<Double> termWeights = new ArrayList<>();
			
			// Retreiving Integer values from internal map
			for(int value : documentTermFrequencies.get(i).values()) 
			{
				// 1 + ln(integer)
				termWeights.add(1 + Math.log(value));
			}
			
			
			double documentWeight = 0;

			for(Double d : termWeights)
			{
				// Sum of all termWeights^2
				documentWeight += Math.pow(d, 2);
			}
			
			documentWeight = Math.sqrt(documentWeight);
			System.out.println("Document " + i + " Weight: " + documentWeight);
			map.add(documentWeight);
			
			i++;
		}while(i < documentTermFrequencies.size() +1);

		return map;
	}
}
