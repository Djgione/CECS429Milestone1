package cecs429.queries;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import cecs429.index.Index;
import cecs429.index.Pair;
import cecs429.index.Posting;
import cecs429.text.Constants;
import cecs429.weights.Accumulator;
import cecs429.weights.AccumulatorComparator;

public class Okapi_BM25_RankedQuery extends IRankedQuery{

	private PriorityQueue<Accumulator> queue;

	public Okapi_BM25_RankedQuery()
	{
		queue = new PriorityQueue<>(1,new AccumulatorComparator());
		queue.clear();
	}
	public Okapi_BM25_RankedQuery(String path) throws FileNotFoundException
	{
		super(path);
		queue = new PriorityQueue<>(1,new AccumulatorComparator());
		queue.clear();

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


			List<Pair> postingForTerm = index.getDocIdPairs(term);


			if(postingForTerm.size() == 0)
				continue;

			double wqt = Math.max(0.1, Math.log(
					( docAmount - postingForTerm.size() +.5) /
					(postingForTerm.size() +.5 ) 
					) 
					);



			// ForEach doc d in postings of t
			for(Pair p : postingForTerm)
			{
				int totalLength = readTotalLength();
				int docLength = readLength(p.getDocId());
				double wdt = (2.2 - p.getTftd()) / (
						(1.2) * (.25 + (.75 * (docLength/totalLength)  ) + p.getTftd())
						);
				if(accList.containsKey(p.getDocId())) 
				{
					accList.computeIfPresent(p.getDocId(), (key,value) -> value + (wqt * wdt) );
				}
				else {
					accList.put(p.getDocId(),
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
							( e.getValue() / readWeight(e.getKey() ) )
							) );
		}

		for(int i = 0; i < Constants.DOCS_RETURNED; i++)
		{
			results.add(queue.poll());
		}

		return results;
	}

}
