package cecs429.weights;

import java.util.Map;

public interface IWeightCalculator {

	IWeightCalculator setDocumentTermFrequencies(Map<Integer,Map<String,Integer>> map);
	Map<Integer, Double> calculate();
}
