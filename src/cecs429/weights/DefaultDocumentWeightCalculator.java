package cecs429.weights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DefaultDocumentWeightCalculator implements IDocumentWeightCalculator{

	public DefaultDocumentWeightCalculator() {

	}

	public List<Double> calculate(DocumentValuesModel model)
	{
		Map<Integer, Map<String, Integer>> documentTermFrequencies = model.getMap();
		List<Double> map = new ArrayList<>();
		if(documentTermFrequencies.isEmpty())
			return map;


		int i = 0;

		// Retrieving the internal map with loop
		do {
			List<Double> termWeights = new ArrayList<>();

			// Retrieving Integer values from internal map
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
			
			
			map.add(documentWeight);

			i++;
		}while(i < documentTermFrequencies.size() );

		return map;
	}
}
