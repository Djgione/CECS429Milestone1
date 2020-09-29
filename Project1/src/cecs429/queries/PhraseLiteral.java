package cecs429.queries;

import cecs429.index.Index;
import cecs429.index.Posting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a phrase literal consisting of one or more terms that must occur in sequence.
 */
public class PhraseLiteral implements Query {
	// The list of individual terms in the phrase.
	private List<Query> mChildren = new ArrayList<>();
	
	/**
	 * Constructs a PhraseLiteral with the given individual phrase terms.
	 */
	public PhraseLiteral(Collection<Query> children) {
		mChildren.addAll(children);
	}
	
	@Override
	public List<Posting> getPostings(Index index) {
		
		// TODO: program this method. Retrieve the postings for the individual terms in the phrase,
		// and positional merge them together.
                int i,j,a,b = 0;
                
                
                return null;
	}
        
	
	@Override
	public String toString() {
		return "\"" + mChildren.stream().map(c -> c.toString()).collect(Collectors.joining(" "))+ "\"";
	}
}
