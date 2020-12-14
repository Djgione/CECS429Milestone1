package cecs429.weights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DefaultDocumentWeightCalculator implements IDocumentWeightCalculator{

	private List<Double> knnWeights = new ArrayList<Double>();
	private Map<Integer, List<Double>> termWeightSets = new HashMap<Integer, List<Double>>();

	public DefaultDocumentWeightCalculator() {
	
	}

	public List<Double> getKnnWeights() {return knnWeights;}
	
	
	public Map<Integer, List<Double>> termWeightSets() {return termWeightSets;}
	
	public List<Double> calculate(DocumentValuesModel model)
	{
	    Map<Integer, Map<String, Integer>> documentTermFrequencies = model.getMap();
		List<Double> map = new ArrayList<>();
		List<Integer> docIds = new ArrayList<Integer>(documentTermFrequencies.keySet());
		
		if(documentTermFrequencies.isEmpty())
			return map;

		int i = 0;
		// Retrieving the internal map with loop
		do {
			List<Double> termWeights = new ArrayList<Double>();
			
			Integer docId = docIds.get(i);
			

			// Retreiving Integer values from internal map
			for(int value : documentTermFrequencies.get(i).values()) 
			{
				termWeights.add(1 + Math.log(value));
			
			}
						
			double documentWeight = 0;

			for(Double d : termWeights)
			{
				// Sum of all termWeights^2
				documentWeight += Math.pow(d, 2);				
			}
			
			documentWeight = Math.sqrt(documentWeight);
			List<Double> normalizedTermWeights = new ArrayList<Double>();
			// Retreiving Integer values from internal map
			for(Double wdt : termWeights) 
			{
				normalizedTermWeights.add(wdt/documentWeight);
			
			}
			termWeightSets.put(docId, normalizedTermWeights);
			map.add(documentWeight);
			
			i++;
		}while(i < documentTermFrequencies.size());

		return map;
	}
}
