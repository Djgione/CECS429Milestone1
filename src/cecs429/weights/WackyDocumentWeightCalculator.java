package cecs429.weights;

import java.util.ArrayList;
import java.util.List;

public class WackyDocumentWeightCalculator implements IDocumentWeightCalculator {

	@Override
	public List<Double> calculate(DocumentValuesModel model) {
		// TODO Auto-generated method stub
		List<Double> results = new ArrayList<>();
		
		if(model.getMap().size()==0)
			return results;
		for(int i = 0; i < model.getByteSizes().size(); i++)
		{
			results.add(Math.sqrt(model.getByteSizes().get(i)));
		}
		
		return results;
	}

}
