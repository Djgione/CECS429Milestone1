package cecs429.weights;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Okapi_BM25_DocumentWeightCalculator implements IDocumentWeightCalculator {

	@Override
	public List<Double> calculate(DocumentValuesModel model) {
		// TODO Auto-generated method stub
		List<Double> results = new ArrayList<>();
		if(model.getMap().size()==0)
			return results;
		for(int i = 0; i < model.getMap().size(); i++) {
			results.add(1.0);
		}
		
		return results;
	}

	@Override
	public List<Double> getKnnWeights() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, List<Double>> termWeightSets() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
