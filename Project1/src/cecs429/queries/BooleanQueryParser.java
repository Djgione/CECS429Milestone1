package cecs429.queries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parses boolean queries according to the base requirements of the CECS 429 project.
 * Does not handle phrase queries, NOT queries, NEAR queries, or wildcard queries... yet.
 */
public class BooleanQueryParser {
	/**
	 * Identifies a portion of a string with a starting index and a length.
	 */
	private static class StringBounds {
		int start;
		int length;
		
		StringBounds(int start, int length) {
			this.start = start;
			this.length = length;
		}
	}
	
	/**
	 * Encapsulates a Query and the StringBounds that led to its parsing.
	 */
	private static class Literal {
		StringBounds bounds;
		Query literalComponent;
		
		Literal(StringBounds bounds, Query literalComponent) {
			this.bounds = bounds;
			this.literalComponent = literalComponent;
		}
	}
	
	/**
	 * Given a boolean query, parses and returns a tree of Query objects representing the query.
     * @param query
     * @return 
	 */
	public Query parseQuery(String query) {
		int start = 0;
		
		// General routine: scan the query to identify a literal, and put that literal into a list.
		//	Repeat until a + or the end of the query is encountered; build an AND query with each
		//	of the literals found. Repeat the scan-and-build-AND-query phase for each segment of the
		// query separated by + signs. In the end, build a single OR query that composes all of the built
		// AND subqueries.
		
		List<Query> allSubqueries = new ArrayList<>();
		do {
			// Identify the next subquery: a portion of the query up to the next + sign.
			StringBounds nextSubquery = findNextSubquery(query, start);
			// Extract the identified subquery into its own string.
			String subquery = query.substring(nextSubquery.start, nextSubquery.start + nextSubquery.length);
			int subStart = 0;
			
                       // System.out.println("subquery: " + subquery);
			// Store all the individual components of this subquery.
                        
			List<Query> subqueryLiterals = new ArrayList<>(0);

			do {
				// Extract the next literal from the subquery.
				Literal lit = findNextLiteral(subquery, subStart);
				// Add the literal component to the conjunctive list.
				subqueryLiterals.add(lit.literalComponent);
				
				// Set the next index to start searching for a literal.
				subStart = lit.bounds.start + lit.bounds.length;
                                //System.out.println(lit.bounds.length);
				
			} while (subStart < subquery.length());
			
			// After processing all literals, we are left with a conjunctive list
			// of query components, and must fold that list into the final disjunctive list
			// of components.
			
			// If there was only one literal in the subquery, we don't need to AND it with anything --
			// its component can go straight into the list.
			if (subqueryLiterals.size() == 1) {
				allSubqueries.add(subqueryLiterals.get(0));
			}
			else {
				// With more than one literal, we must wrap them in an AndQuery component.
				allSubqueries.add(new AndQuery(subqueryLiterals));
			}
			start = nextSubquery.start + nextSubquery.length;
		} while (start < query.length());
		
		// After processing all subqueries, we either have a single component or multiple components
		// that must be combined with an OrQuery.
		if (allSubqueries.size() == 1) {
			return allSubqueries.get(0);
		}
		else if (allSubqueries.size() > 1) {
			return new OrQuery(allSubqueries);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Locates the start index and length of the next subquery in the given query string,
	 * starting at the given index.
	 */
	private StringBounds findNextSubquery(String query, int startIndex) {
		int lengthOut;
		
		// Find the start of the next subquery by skipping spaces and + signs.
		char test = query.charAt(startIndex);
		while (test == ' ' || test == '+') {
                    test = query.charAt(++startIndex);
		}
		
		// Find the end of the next subquery.
		int nextPlus = query.indexOf('+', startIndex + 1);
		
		if (nextPlus < 0) {
			// If there is no other + sign, then this is the final subquery in the
			// query string.
			lengthOut = query.length() - startIndex;
		}
		else {
			// If there is another + sign, then the length of this subquery goes up
			// to the next + sign.
		
			// Move nextPlus backwards until finding a non-space non-plus character.
			test = query.charAt(nextPlus);
			while (test == ' ' || test == '+') {
				test = query.charAt(--nextPlus);
			}
			
			lengthOut = 1 + nextPlus - startIndex;
		}
		
		// startIndex and lengthOut give the bounds of the subquery.
		return new StringBounds(startIndex, lengthOut);
	}
	
	/**
	 * Locates and returns the next literal from the given subquery string.
	 */
	private Literal findNextLiteral(String subquery, int startIndex) {
           int subLength = subquery.length();
		int lengthOut = 0;
                Query returnLiteral;
                List<String> children = new ArrayList();
            
                
                // "whlae not"  for  
		// Skip past white space.
		while (subquery.charAt(startIndex) == ' ') {
			++startIndex;
		}
                // 1.if start index is " then scan till next " and put everything in a phrase 
                // 2.set lengthout to the index of ending " +1
                if(subquery.charAt(startIndex)=='-')
                {
                    startIndex++;
                    int nextSkip = subquery.indexOf(' ', startIndex);
                    if (nextSkip < 0) {
                            // No more literals in this subquery.
                            lengthOut = subLength - startIndex;
                    }
                    else 
                    {
                        lengthOut = nextSkip - startIndex;
                        //System.out.println("startIndex final: " + startIndex);
                    }
                    if(subquery.indexOf('*', startIndex) != -1)
                    {
                    	returnLiteral = new WildcardLiteral(subquery.substring(startIndex, startIndex + lengthOut));
                        returnLiteral.negative(true);
                    }
                    else 
                    {
                    	returnLiteral = new TermLiteral(subquery.substring(startIndex, startIndex + lengthOut));
                        returnLiteral.negative(true);
                    }
                    
                    
                }
                else if(subquery.charAt(startIndex) == '\"')
                {
                    //"whale not" for
                    // Locate the next space/double quote to find the end of this literal.
                    startIndex++;
                    int nextSkip = subquery.indexOf('\"', startIndex);
                   
                        lengthOut = nextSkip +1;
                        String phrase=subquery.substring(startIndex, lengthOut-1);
                        System.out.println("startIndex final: " + startIndex);
                    
                    //lengthOut = subLength;
                    children.addAll(Arrays.asList(phrase.split(" ")));
                    if(children.size()==2 && phrase.indexOf('*') ==-1)
                    {
                        returnLiteral=new BiWordQuery(children.get(0)+" "+children.get(1));
                    }
                    else
                    {
                        returnLiteral = new PhraseLiteral(children);
                    }
                }
                else
                {
                    // Locate the next space/double quote to find the end of this literal.
                    int nextSkip = subquery.indexOf(' ', startIndex);
                    if (nextSkip < 0) {
                            // No more literals in this subquery.
                            lengthOut = subLength - startIndex;
                    }
                    else 
                    {
                        lengthOut = nextSkip - startIndex;
                        //System.out.println("startIndex final: " + startIndex);
                    }
                    
                    String newTerm = subquery.substring(startIndex, startIndex + lengthOut);
                    if(newTerm.indexOf('*') !=-1)
                    {
                    	returnLiteral = new WildcardLiteral(subquery.substring(startIndex, startIndex + lengthOut));
                    }
                    else
                    {
                    	returnLiteral = new TermLiteral(subquery.substring(startIndex, startIndex + lengthOut)); 
                    }
               }
                //System.out.println("startIndex begin: " + startIndex);

                
		// This is a term literal containing a single term or a 2
                // term phrase literal depending on if double quotes are in query or not.
		return new Literal(
		 new StringBounds(startIndex, lengthOut), returnLiteral);
		 
		
		/*
		TODO:
		Instead of assuming that we only have single-term literals, modify this method so it will create a PhraseLiteral
		object if the first non-space character you find is a double-quote ("). In this case, the literal is not ended
		by the next space character, but by the next double-quote character.
		 */
	}
        
	
}
