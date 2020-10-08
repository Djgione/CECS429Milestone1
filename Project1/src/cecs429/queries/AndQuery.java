package cecs429.queries;

import cecs429.index.BiWordIndex;
import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.text.IntermediateTokenProcessor;
import cecs429.text.TokenProcessor;

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
		List<Posting> result = new ArrayList<>();
                
                System.out.print(mChildren.get(0).getnegative() +" "+mChildren.get(0).toString());
                for(Posting p : mChildren.get(0).getPostings(index,proc))
                {
                    result.add(p);
                    
                }

                for(int i=1;i<mChildren.size();i++)
                {   if(i==1 && mChildren.get(0).getnegative())
                    {
                         result =notmerge(mChildren.get(i).getPostings(index,proc),result);
                    }
                    else if(i==1 && mChildren.get(1).getnegative()==true)
                    {
                        result = notmerge(result,mChildren.get(i).getPostings(index,proc));
                    }
                    else if(i>1 && mChildren.get(i).getnegative()==true)
                    {
                         result = notmerge(result,mChildren.get(i).getPostings(index,proc));
                    }
                    else
                    {
                        result=merge(result,mChildren.get(i).getPostings(index,proc));
                    }
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

    @Override
    public void negative(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getnegative() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<Posting> notmerge(List<Posting> list1, List<Posting> list2) {
        List<Posting> result=new ArrayList();
        int i=0;
        int j=0;
        while(i<list1.size() && j<list2.size())
        {
            if(list1.get(i).getDocumentId()==list2.get(j).getDocumentId())
            {
                i++;
                j++;
            }
            else if(list1.get(i).getDocumentId()<list2.get(j).getDocumentId())
            {
                result.add(list1.get(i));
                i++;
            }
            else if(list1.get(i).getDocumentId()>list2.get(j).getDocumentId())
            {
                j++;
            }
        }
        while(i<list1.size())
        {
            result.add(list1.get(i++));
        }
        return result;
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
