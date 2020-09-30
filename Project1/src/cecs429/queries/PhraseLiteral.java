package cecs429.queries;

import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.text.IntermediateTokenProcessor;
import cecs429.text.TokenProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a phrase literal consisting of one or more terms that must occur in sequence.
 */
public class PhraseLiteral implements Query {
	// The list of individual terms in the phrase.
	private List<String> mChildren = new ArrayList<>();
	
	/**
	 * Constructs a PhraseLiteral with the given individual phrase terms.
     * @param children
	 */
	public PhraseLiteral(Collection<String> children) {
		mChildren.addAll(children);
                
	}
	
	@Override
	public String toString() {
		return "\"" + mChildren.stream().map(c -> c.toString()).collect(Collectors.joining(" "))+ "\"";
	}

    public List<Posting> getPostings(Index index, IntermediateTokenProcessor processor)
    {
        List<Posting> result = index.getPostings(processor.processToken(mChildren.get(0)).get(0));
        for(int i=1; i<mChildren.size();i++)
        {
            List<Posting> mChild = index.getPostings(processor.processToken(mChildren.get(i)).get(i));
            
            result=merge(result,mChild,i);
        }
        // TODO: program this method. Retrieve the postings for the individual terms in the phrase,
        // and positional merge them together.
        

        return null;
    }
    public List<Posting> merge(List<Posting> list1, List<Posting> list2,int dis)
    {
        return null;
    }

    @Override
    public List<Posting> getPostings(Index index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
