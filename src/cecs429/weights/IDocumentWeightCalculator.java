package cecs429.weights;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface IDocumentWeightCalculator {
	
	
	//void setDocumentTermFrequencies(Map<Integer,Map<String,Integer>> map);
	 List<Double> calculate(DocumentValuesModel model);
	 
	List<Double> getKnnWeights();
	
	Map<Integer, List<Double>> termWeightSets();
}
