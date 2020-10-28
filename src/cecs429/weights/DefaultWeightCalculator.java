package cecs429.weights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DefaultWeightCalculator implements IWeightCalculator{

	//private Map<Integer,Map<String,Integer>> documentTermFrequencies;


//	public void setDocumentTermFrequencies(Map<Integer, Map<String, Integer>> documentTermFrequencies) {
//		this.documentTermFrequencies = documentTermFrequencies;
		
	
	

	public DefaultWeightCalculator() {
		super();
	}

	public TreeMap<Integer,Double> calculate(Map<Integer,Map<String,Integer>> documentTermFrequencies)
	{

		TreeMap<Integer,Double> map = new TreeMap<>();
		if(documentTermFrequencies.isEmpty())
			return map;
		

		int i = 1;
		// Retrieving the internal map with loop
		do {
			List<Double> termWeights = new ArrayList<>();
			
			// Retreiving Integer values from internal map
			for(Integer value : documentTermFrequencies.get(i).values()) 
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
			map.put(i, documentWeight);
			
			i++;
		}while(i < documentTermFrequencies.size() +1);

		return map;
	}
}
