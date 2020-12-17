package cecs429.queries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cecs429.index.BiWordIndex;
import cecs429.index.DiskKgramIndex;
import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.index.iKgramIndex;
import cecs429.text.IntermediateTokenProcessor;
import cecs429.weights.Accumulator;

public class WildcardLiteral implements Query {
	private String mTerm;
	private boolean mNegative;


	/**
	 * WildCardLiteral is a literal term that contains a wildcard '*' character
	 * @param term
	 */
	public WildcardLiteral(String term)
	{
		mTerm = term;

	}
	
	public WildcardLiteral(String term, boolean neg)
	{
		mTerm = term;
		mNegative = neg;
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
		if(mTerm.length() == 1 && mTerm.charAt(0)=='*')
		{
			return index.getPostings();
		}
		mTerm = mTerm.toLowerCase();
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

		if(queries.size()==0)
			return new ArrayList<Posting>();
		Query results;
		if(mNegative)
		{
			results = new AndQuery(queries);
		}
		else 
		{
			results = new OrQuery(queries);
		}
		return results.getPostings(index, proc);

	}
	
	public List<String> getKGrams(DiskKgramIndex index)
	{
		if(mTerm.length() == 1 && mTerm.charAt(0)=='*')
		{
			try 
			{
				return index.getPostings();
			}catch(Exception e)
			{
				e.printStackTrace();
				return new ArrayList<String>();
			}
		}
		mTerm = mTerm.toLowerCase();
		String tempTerm = "$" + mTerm + "$";
		List<String> kGrams = divideKGrams(tempTerm);
		
		List<String> terms = new ArrayList<>();
		try 
		{
			for(String s : kGrams)
			{

				terms.addAll(index.getPostings(s));

			}
			
			return returnKGramResults(terms, kGrams);
			
			
		} catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<String>();
		}
		
	}
	
	public List<String> returnKGramResults(List<String> terms, List<String> kGrams)
	{
		List<String> filteredResults = new ArrayList<>();

		
		for(String term : terms)
		{
			
			boolean addable = true;


			for(int i = 0; i < kGrams.size(); i++)
			{
				// Checks to see if kGram is inside term, as well as further along than last kGram
				if(!term.contains(kGrams.get(i)))
				{
					addable = false;
					break; 
				}

				// If all Kgrams exist inside the term, add the term

			}
			if(addable)
			{
				term = term.substring(1,term.length()-1);
				filteredResults.add(term);
			
			}
		}
		
		return filteredResults;
	}




	/**
	 * Filters results returned from parsing the kGramIndex
	 * @param terms
	 * @return List of Queries (termLiteral) that fit the wildCard
	 */
	private List<Query> filterResults(List<String> terms, List<String> kGrams)
	{
		List<String> filteredResults = returnKGramResults(terms,kGrams);
		
		if(filteredResults.size() == 0)
		{
			return new ArrayList<Query>();
		}
		HashSet<String> set = new HashSet<>(filteredResults);
		List<Query> queries = new ArrayList<>();
		for(String s : set)
		{
			
			queries.add(new TermLiteral(s,mNegative));
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
		System.out.println(term);
		if(term.length() <= 3)
		{
			term = removeAsterisk(term);
			if(term.equals("$"))
				return terms;
			terms.add(term);
			return terms;
		}
		for(int j = 3; j != 0; j--) {
			for(int i = 0; i < term.length()-(j-1); i++)
			{
				
				String s = term.substring(i, i+j);
				s = removeAsterisk(s);
				boolean addable = true;
				if(s.equals("$"))
					addable = false;
				//Checks kGram against existing kGrams to see if it is contained in one already, no duplicates
				for(int f = 0; f < terms.size(); f++)
				{
					if(terms.get(f).contains(s))
						addable = false;
					if(s.contains(terms.get(f)) && !s.equals(terms.get(f)))
						terms.remove(f);
					
				}
				if(addable)
					terms.add(s);
			}
		}
		return terms;

	}

	private String removeAsterisk(String s)
	{
		while(s.indexOf('*') >= 0) {
			if(s.indexOf('*') == 0 && s.length() > 1)
			{
				s = s.substring(1);
			}
			else if (s.length() == 1 && s.charAt(0) == '*')
			{
				return "";
			}
			else if(s.indexOf('*') == s.length()-1)
			{
				s = s.substring(0, s.length()-1);
			}
			else
			{
				s = s.substring(0,s.indexOf('*'));
			}
			
		}
		
		return s;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("mTerm: ");
		builder.append(mTerm);
		return builder.toString();
	}

	@Override
	public void negative(boolean b) {
		mNegative = b;

	}

	@Override
	public boolean getnegative() {
		// TODO Auto-generated method stub
		return mNegative;
	}

	@Override
	public boolean isBiWord() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBiWord() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Posting> getPosting(BiWordIndex biwordindex) {
		// TODO Auto-generated method stub
		return null;
	}

	


}
