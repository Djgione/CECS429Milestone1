package cecs429.queries;

import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.text.IntermediateTokenProcessor;
import cecs429.text.TokenProcessor;
import java.util.ArrayList;
import java.util.Collection;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An AndQuery composes other Query objects and merges their postings in an intersection-like operation.
 */
public class AndQuery implements Query {
	private List<Query> mChildren;
        IntermediateTokenProcessor processor=new IntermediateTokenProcessor();
	
	public AndQuery(Collection<Query> children) {
		mChildren = new ArrayList<>(children);
	}
	
	@Override
	public List<Posting> getPostings(Index index, IntermediateTokenProcessor proc) {
		List<Posting> result = new ArrayList();
                
                
                for(Posting p : mChildren.get(0).getPostings(index,proc))
                {
                    result.add(p);
                }
                for(int i=1;i<mChildren.size();i++)
                {
                    result=merge(result,mChildren.get(i).getPostings(index,proc));
                }
		
/*            mChildren.forEach((Query q) -> {
                    processor.processToken(q.toString());
                }); */
            // TODO: program the merge for an AndQuery, by gathering the postings of the composed QueryComponents and
            // intersecting the resulting postings.
		
		return result;
	}
	public List<Posting> merge(List<Posting> list1, List<Posting> list2)
        {
            List<Posting> result=new ArrayList();
            int i=0;
            int j=0;
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
                    i++;
                }
                else
                {
                    j++;
                }
            }
            return result;
        }
	@Override
	public String toString() {
		return
		 String.join(" ", mChildren.stream().map(c -> c.toString()).collect(Collectors.toList()));
	}

    @Override
    public List<Posting> getPostings(Index index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
