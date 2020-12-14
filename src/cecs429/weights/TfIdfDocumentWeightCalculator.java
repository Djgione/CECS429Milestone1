package cecs429.weights;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TfIdfDocumentWeightCalculator implements IDocumentWeightCalculator {


	public TfIdfDocumentWeightCalculator()
	{
		
	}

	@Override
	public List<Double> calculate(DocumentValuesModel model) {
		// TODO Auto-generated method stub
		Map<Integer, Map<String, Integer>> map = model.getMap();
		List<Double> results = new ArrayList<>();
		if(map.isEmpty())
			return results;
		
		int i = 0;
		
		do {
			
			List<Double> termWeights = new ArrayList<>();
			for(int value: map.get(i).values())
			{
				termWeights.add(value +.0);
			}
			
			double documentWeight = 0;
			
			for(Double d : termWeights)
			{
				documentWeight += Math.pow(d,2);
			}
			
			documentWeight = Math.sqrt(documentWeight);
			System.out.println("Document " + i + " Weight: " + documentWeight);
			results.add(documentWeight);
			i++;
			
		}while(i < map.size());		
		
		return results;
	}

}
