package cecs429.queries;

import java.util.ArrayList;
import java.util.HashSet;
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

	/**
	 * Gets the postings for the wildcard term, uses an OrQuery to combine the results
	 */
	@Override
	public List<Posting> getPostings(Index index, IntermediateTokenProcessor proc) {
		mTerm = proc.processToken(mTerm).get(0);
		String tempTerm = "$" + mTerm + "$";
		List<String> kGrams = divideKGrams(tempTerm);
		List<Query> queries;
		
		
		// Adds all terms from kGramIndex to the Arraylist terms
		List<String> terms = new ArrayList<>();
		for(String s : kGrams)
		{
			terms.addAll(index.getIndex().getPostings(s));
		}
		
		queries = filterResults(terms, kGrams);
		
		List<Posting> results = new ArrayList<Posting>();
		//TODO: Or together posting finds from filtered Terms
		OrQuery orResults = new OrQuery(queries);
		
		return orResults.getPostings(index, proc);
		
	}
	
	
	
	
	/**
	 * Filters results returned from parsing the kGramIndex
	 * @param terms
	 * @return List of Queries (termLiteral) that fit the wildCard
	 */
	 private List<Query> filterResults(List<String> terms, List<String> kGrams)
	 {
		 List<String> filteredResults = new ArrayList<>();
		 
		 for(String term : terms)
		 {
			 int lastIndex = -1;
			 boolean addable = true;
			 
			 
			 for(int i = 0; i < kGrams.size(); i++)
			 {
				 // Checks to see if kGram is inside term, as well as further along than last kGram
				 if(term.indexOf(kGrams.get(i)) > lastIndex)
				 {
					 lastIndex = term.indexOf(kGrams.get(i));
				 }
				 else
				 {
					 addable = false;
					 break;
				 }
				 
				 // If all Kgrams exist inside the term, add the term
				
			 }
			 if(addable)
				 filteredResults.add(term);
			 
		 }
		 
		 // Removes duplicate words
		 HashSet<String> set = new HashSet<>(filteredResults);
		 List<Query> queries = new ArrayList<>();
		 for(String s : set)
		 {
			 queries.add(new TermLiteral(s));
		 }
		 return queries;
	 }
	
	/**
	 * Divides the term into a list of KGrams stored in the class
	 * only for use by the getPostings method
	 * Max Kgram length can be 3, anything less will just search for kGrams with just the term
	 * Takes out the wildcard characters of each kgram
	 * @param term
	 */
	private List<String> divideKGrams(String term)
	{
		List<String> terms = new ArrayList<>();
		
		if(term.length() <= 3)
		{
			term.replaceAll("\\*","");
			terms.add(term);
			return terms;
		}
		for(int i = 0; i < mTerm.length()-3; i++)
		{
			String s = mTerm.substring(i, i+3);
			s.replaceAll("\\*", "");
			boolean addable = true;
			//Checks kGram against existing kGrams to see if it is contained in one already, no duplicates
			for(String existingTerm: terms)
			{
				if(existingTerm.contains(s))
					addable = false;
			}
			if(addable)
				terms.add(s);
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
