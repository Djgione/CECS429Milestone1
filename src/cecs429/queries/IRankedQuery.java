package cecs429.queries;

import java.util.List;
import java.util.PriorityQueue;

import cecs429.weights.Accumulator;

public interface IRankedQuery {

	/**
	 * Returns a list of strings containing the appropriate document titles and accumulator value to print for the user
	 * @param terms
	 * @return
	 */
	List<String> query(List<String> terms);
//	PriorityQueue<Accumulator> getQueue();
	
}
