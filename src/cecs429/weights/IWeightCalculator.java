package cecs429.weights;

import java.util.Map;
import java.util.TreeMap;

public interface IWeightCalculator {
	
	
	//void setDocumentTermFrequencies(Map<Integer,Map<String,Integer>> map);
	TreeMap<Integer, Double> calculate(Map<Integer,Map<String,Integer>> map);
}
