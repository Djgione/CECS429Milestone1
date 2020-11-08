package cecs429.queries;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import cecs429.weights.Accumulator;
import cecs429.weights.AccumulatorComparator;

public class DefaultRankedQuery implements IRankedQuery {

	private PriorityQueue<Accumulator> queue;
	
	public DefaultRankedQuery()
	{
		queue = new PriorityQueue<>(10, new AccumulatorComparator());
	}
	
	@Override
	public List<String> query(List<String> terms) {
		// TODO Auto-generated method stub
		List<String> results = new ArrayList<>();
		
		//Null check
		if(terms.size() == 0)
			return results;
		
		
		
		
		
		
		
		
		
		
		return results;
	}


}
