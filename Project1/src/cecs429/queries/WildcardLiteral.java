package cecs429.queries;

import java.util.ArrayList;
import java.util.List;

import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.text.IntermediateTokenProcessor;

public class WildcardLiteral implements Query {
	private String mTerm;
	
	
	/**
	 * WildCardLiteral is a literal term that contains a wildcard '*' character
	 * @param term
	 */
	public WildcardLiteral(String term)
	{
		mTerm = term;
		
	}

	/**
	 * Not supported
	 */
	@Override
	public List<Posting> getPostings(Index index) {
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public List<Posting> getPostings(Index index, IntermediateTokenProcessor proc) {
		mTerm = proc.processToken(mTerm).get(0);
		mTerm = "$" + mTerm + "$";
		List<String> kGrams = divideKGrams(mTerm);
		
		
		// TODO: Implement using the KGramIndex to get the correct posting lists
		
		List<String>terms;
		
		
		
		
		
	}
	
	/**
	 * Filters results returned from parsing the kGramIndex
	 * @param terms
	 * @return List of Strings that fit the wildCard
	 */
	 private List<String> filterResults(List<String> terms)
	 {
		 
	 }
	
	/**
	 * Divides the term into a list of KGrams stored in the class
	 * only for use by the getPostings method
	 * Max Kgram length can be 3, anything less will just search for kGrams with just the term
	 * @param term
	 */
	private List<String> divideKGrams(String term)
	{
		List<String> terms = new ArrayList<>();
		
		if(term.length() <= 3)
		{
			terms.add(term);
			return terms;
		}
		for(int i = 0; i < mTerm.length()-3; i++)
		{
			terms.add(mTerm.substring(i, i+3));
		}
		
		return terms;
		
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("mTerm: ");
		builder.append(mTerm);
		return builder.toString();
	}
	

}
