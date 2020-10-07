package cecs429.queries;

import cecs429.index.BiWordIndex;
import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.text.IntermediateTokenProcessor;

import java.util.List;

/**
 * A TermLiteral represents a single term in a subquery.
 */
public class TermLiteral implements Query {
	private String mTerm;
	boolean negative=false;
	public TermLiteral(String term) {
		mTerm = term;
	}
	
	public String getTerm() {
		return mTerm;
	}
	
	@Override
	public List<Posting> getPostings(Index index,IntermediateTokenProcessor processor) {
            mTerm=processor.processToken(mTerm).get(0);
            return index.getPostings(mTerm);
	}
	
	@Override
	public String toString() {
		return mTerm;
	}

    @Override
    public List<Posting> getPostings(Index index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
        @Override
    public boolean getnegative()
    {
        return negative;
    }
    public void negative(boolean b) {
        this.negative=b;
    }

    @Override
    public boolean isBiWord() {
        return false;
    }

    @Override
    public void setBiWord() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Posting> getPosting(BiWordIndex biwordindex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
