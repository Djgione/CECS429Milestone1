/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.queries;

import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.text.IntermediateTokenProcessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author kabir
 */
public class AndNotQuery implements Query {
    private List<Query> mChildren;
    
    public AndNotQuery(Collection<Query> children)
    {
       mChildren = new ArrayList(children);
    }
    @Override
    public List<Posting> getPostings(Index index) {
        return null;
    }

    @Override
    public List<Posting> getPostings(Index index, IntermediateTokenProcessor proc) {
        List<Posting> result=new ArrayList();
        for(Posting p : mChildren.get(0).getPostings(index))
                {
                    result.add(p);
                }
                for(int i=1;i<mChildren.size();i++)
                {
                    result=merge(result,mChildren.get(i).getPostings(index));
                }
                return result;
    }
    private List<Posting> merge(List<Posting> list1,List<Posting> list2)
    {
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
            if(list1.get(i).getDocumentId()<list2.get(j).getDocumentId())
            {
                result.add(list1.get(i));
                i++;
            }
            if(list1.get(i).getDocumentId()>list2.get(j).getDocumentId())
            {
                j++;
            }
        }
        while(i<list1.size())
        {
            result.add(list1.get(i));
        }
        return result;
    }
    
    
}
