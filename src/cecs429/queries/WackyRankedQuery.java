package cecs429.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.text.Constants;
import cecs429.weights.Accumulator;
import cecs429.weights.AccumulatorComparator;

public class WackyRankedQuery implements IRankedQuery {

	
private PriorityQueue<Accumulator> queue;
	
	public WackyRankedQuery()
	{
		queue = new PriorityQueue<>(1,new AccumulatorComparator());
		queue.clear();
	}
	@Override
	public List<Accumulator> query(List<String> terms, Index index) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				List<Accumulator> results = new ArrayList<>();
				
				//Null check
				if(terms.size() == 0)
					return results;
				int maxDocs = index.getDocumentValuesModel().getDocLengths().size();	
				
				Map<Integer,Double> accList = new HashMap<>();
				//ForEach term t in query
				for(String term : terms)
				{
					
									
					List<Posting> postingForTerm = index.getPostings(term);
					
					if(postingForTerm.size() == 0)
						continue;
//					System.out.println("PostingForTermSize: " + postingForTerm.size());
//					System.out.println("Max doc - postingForTermSize: " + (maxDocs - postingForTerm.size()));
//					System.out.println("Whole log: " + Math.log( (maxDocs - postingForTerm.size() ) / postingForTerm.size() ) );
					double wqt = Math.max(0, Math.log( (maxDocs - postingForTerm.size() ) / postingForTerm.size() ) );
//					System.out.println("wqt: " + wqt);
//					
//					System.out.println();
					
					// ForEach doc d in postings of t
					for(Posting p : postingForTerm)
					{
						
						double wdt = 1 + Math.log(p.getPositions().size()) / 
								(1 + Math.log( index.getDocumentValuesModel().
										getDocAverageTFDs().get(p.getDocumentId()) ));
						
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
				
				if(accList.size() == 0)
					return results;
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
