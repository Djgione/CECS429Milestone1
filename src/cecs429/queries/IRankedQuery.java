package cecs429.queries;

import java.util.List;
import java.util.PriorityQueue;
import cecs429.index.Posting;
import cecs429.index.Index;
import cecs429.weights.Accumulator;

public interface IRankedQuery {

	/**
	 * Returns a list of accumulators and their doc id's
	 * @param terms
	 * @return
	 */
	List<Accumulator> query(List<String> terms, Index index);
//	PriorityQueue<Accumulator> getQueue();
	
}
