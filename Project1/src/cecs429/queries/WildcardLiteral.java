package cecs429.queries;

import java.util.List;

import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.text.IntermediateTokenProcessor;

public class WildcardLiteral implements Query {
	private String mTerm;
	
	public WildcardLiteral(String term)
	{
		mTerm = term;
	}

	@Override
	public List<Posting> getPostings(Index index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Posting> getPostings(Index index, IntermediateTokenProcessor proc) {
		// TODO Auto-generated method stub
		return null;
	}

}
