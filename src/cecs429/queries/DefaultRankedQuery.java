package cecs429.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.text.Constants;
import cecs429.weights.Accumulator;
import cecs429.weights.AccumulatorComparator;

public class DefaultRankedQuery implements IRankedQuery {

	private PriorityQueue<Accumulator> queue;
	
	public DefaultRankedQuery()
	{
		queue = new PriorityQueue<>(10, new AccumulatorComparator());
	}
	
	@Override
	public List<Accumulator> query(List<String> terms, Index index) {
		// TODO Auto-generated method stub
		List<Accumulator> results = new ArrayList<>();
		
		//Null check
		if(terms.size() == 0)
			return results;
		
		
		Map<Integer,Double> accList = new HashMap<>();
		//ForEach term t in query
		for(String term : terms)
		{
			
			int maxDocs = index.getPostings().size();						
			List<Posting> postingForTerm = index.getPostings(term);
			
			double wqt = Math.log(1 + (maxDocs /postingForTerm.size()));
			
			
			
			// ForEach doc d in postings of t
			for(Posting p : postingForTerm)
			{
				
				double wdt = 1 + Math.log(p.getPositions().size());
				if(accList.containsKey(p.getDocumentId())) 
				{
					accList.computeIfPresent(p.getDocumentId(), (key,value) -> value + (wqt * wdt) );
				}
				else {
				accList.put(p.getDocumentId(),
									(wqt * wdt) );
				}
			}

		}
		
		
		for(Entry<Integer,Double> e : accList.entrySet())
		{
			queue.add(
					new Accumulator( 
							e.getKey(),
							( e.getValue() / index.getDocumentValuesModel().getDocWeights().get(e.getKey() ) )
							) );
		}
		
		for(int i = 0; i < Constants.DOCS_RETURNED; i++)
		{
			results.add(queue.poll());
		}
		
		return results;
	}


}
