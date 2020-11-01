/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.index;

import cecs429.queries.Query;
import cecs429.text.IntermediateTokenProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author kabir
 */
public class BiWordIndex implements Index{
    private HashMap<String,List<Posting>> biwordindex;
    private KGramIndex index;
    public BiWordIndex()
    {
        biwordindex=new HashMap();
    }
    public void addTerm(String string,int documentId)
    {
        
        if(!biwordindex.containsKey(string))
        {
            biwordindex.put(string, new ArrayList());
        }
        if(biwordindex.get(string).size()==0)biwordindex.get(string).add(new Posting(documentId));
        if(biwordindex.get(string).size()>0 && biwordindex.get(string).get(biwordindex.get(string).size()-1).getDocumentId()!=documentId)biwordindex.get(string).add(new Posting(documentId));
        
    }
    public List<String> getVocabulary()
    {
        return new ArrayList<String>(biwordindex.keySet());
    }
    public void print()
    {
        for(String s:biwordindex.keySet())
        {
            System.out.println(s+" "+String.valueOf(biwordindex.get(s)));
            
        }
    }

    public List<Posting> getPostings(String term) {
    	if(biwordindex.get(term) == null)
    		return new ArrayList<Posting>();
        return biwordindex.get(term);
    }
    
	public List<Posting> getPostings()
	{
		List<Posting> allPostings = new ArrayList<>();
		for(Entry<String,List<Posting>> entry: biwordindex.entrySet())
		{
			allPostings.addAll(entry.getValue());
		}
		return allPostings;
	}

    @Override
    public void addTerm(String s, int id, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	@Override
	public void setIndex(KGramIndex index) {
		this.index = index;
		
	}
	@Override
	public KGramIndex getIndex() {
		// TODO Auto-generated method stub
		return index;
	}
	@Override
	public void setDocumentWeights(TreeMap<Integer, Double> map) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public TreeMap<Integer, Double> getDocumentWeights() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public List<Integer> getDocIds(String term) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
  
}
