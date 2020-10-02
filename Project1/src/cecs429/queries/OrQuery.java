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
 * An OrQuery composes other Query objects and merges their postings with a union-type operation.
 */
public class OrQuery implements Query {
	// The components of the Or query.
	private List<Query> mChildren;
	
	public OrQuery(Collection<Query> children) {
		
                mChildren = new ArrayList<Query>(children);
	}
	
	@Override
	public List<Posting> getPostings(Index index) {
		List<Posting> result = new ArrayList();
		
                for(Posting p : mChildren.get(0).getPostings(index))
                {
                    result.add(p);
                }
                for(int i=1;i<mChildren.size();i++)
                {
                    result=merge(result,mChildren.get(i).getPostings(index));
                }
		// TODO: program the merge for an OrQuery, by gathering the postings of the composed Query children and
		// unioning the resulting postings.
		
		return result;
	}
	
        public List<Posting> merge(List<Posting> list1, List<Posting> list2)
        {
            List<Posting> result = new ArrayList<>();
            int i = 0;
            int j = 0;
            
            while(i<list1.size() && j<list2.size())
            {
                int d1=list1.get(i).getDocumentId();
                int d2=list2.get(j).getDocumentId();
                if(d1==d2)
                {
                    result.add(list1.get(i));
                    i++;
                    j++;
                }
                else if(d1<d2)
                {
                    result.add(list1.get(i));
                    i++;
                }
                else if(d2 < d1)
                {
                    result.add(list2.get(j));
                    j++;
                }
            }
            while(i<list1.size())
            {
                result.add(list1.get(i++));
            }
            while(j<list2.size())
            {
                result.add(list1.get(j++));
            }
            return result;
        }

        
	@Override
	public String toString() {
		// Returns a string of the form "[SUBQUERY] + [SUBQUERY] + [SUBQUERY]"
		return "(" +
		 String.join(" + ", mChildren.stream().map(c -> c.toString()).collect(Collectors.toList()))
		 + " )";
	}

    public List<Posting> getPostings(Index index, IntermediateTokenProcessor proc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
