package cecs429.queries;

import cecs429.index.BiWordIndex;
import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.text.IntermediateTokenProcessor;
import cecs429.text.TokenProcessor;

import java.util.ArrayList;
import java.util.Arrays;
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
    	List<Posting> result;
    	if(mChildren.get(0).indexOf('*') >= 0)
    	{
    		Query query = new WildcardLiteral(mChildren.get(0));
    		result = query.getPostings(index, processor);
    	}
    	else
    	{
    		result = index.getPostings(processor.processToken(mChildren.get(0)).get(0));
    	}
        
//        System.out.println(processor.processToken(mChildren.get(0)).get(0));
//        for(Posting p:result)
//        {
//            System.out.print(p.getDocumentId()+"pos=");
//            for(int i: p.getPositions())
//            {
//                System.out.print(i+" ");
//            }
//            System.out.println();
//            
//        }
//        Sysbutem.out.println();
        for(int i=1; i<mChildren.size();i++)
        {
            List<Posting> mChild;
            if(mChildren.get(i).contains("*"))
            {
            	Query query = new WildcardLiteral(mChildren.get(i));
            	mChild = query.getPostings(index, processor);
            }
            else 
            {
            	mChild = index.getPostings(processor.processToken(mChildren.get(i)).get(0));
            }
          
            // System.out.println(processor.processToken(mChildren.get(i)).get(0));

//            for(Posting p:mChild)
//        {
//            System.out.print(p.getDocumentId()+"pos=");
//             for(int j: p.getPositions())
//            {
//                System.out.print(j+" ");
//            }
//             System.out.println();
//        }        
//            System.out.println();

            result=merge(result,mChild,i);
        }
        return result;
        // TODO: program this method. Retrieve the postings for the individual terms in the phrase,
        // and positional merge them together.
        

        
    }
    public List<Posting> merge(List<Posting> list1, List<Posting> list2,int dis)
    {
        List<Posting> result=new ArrayList();
        int i=0;
        int j=0;
        while(i<list1.size() && j<list2.size())
        {
            if(list1.get(i).getDocumentId()==list2.get(j).getDocumentId())
            {
             //todo write merger logic by getting positions and checking if they are dis apart
                List<Integer> matchpositions=new ArrayList();
                List<Integer> l1pos=list1.get(i).getPositions();
                List<Integer> l2pos=list2.get(j).getPositions();
//                System.out.println(String.valueOf(l1pos));
//                System.out.println(String.valueOf(l2pos));
                int l1=0;
                int l2=0;
                while(l1<l1pos.size() && l2<l2pos.size())
                {
                    if(l2pos.get(l2)-l1pos.get(l1)==dis)
                    {
                       // System.out.println(l1pos.get(l1)+"position in L1");
                        matchpositions.add(l1pos.get(l1));
                        l1++;
                        l2++;
                    }
                    else if(l1pos.get(l1)<l2pos.get(l2))
                    {
                        l1++;
                    }
                    else l2++;
                }
                if(!matchpositions.isEmpty())
                {
                   System.out.print(String.valueOf(matchpositions)+" MATCHbu");
                    result.add(new Posting(list1.get(i).getDocumentId(),matchpositions));
                }
                i++;
                j++;
            }
            else if(list1.get(i).getDocumentId()<list2.get(j).getDocumentId())i++;
            else j++;
            
        }
        return result;
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
        return false;
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
