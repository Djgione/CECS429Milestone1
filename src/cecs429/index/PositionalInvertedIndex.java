package cecs429.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.HashMap.*;
import java.util.HashSet;

public class PositionalInvertedIndex implements Index {
	private final HashMap<String, List<Posting>> mMap;
	private KGramIndex index;
	private TreeMap<Integer,Double> documentWeights;
	private int maxDoc;
	
	
	public PositionalInvertedIndex(int num)
	{
		maxDoc = num;
		mMap = new HashMap<>();
	}
	
	public void setIndex(KGramIndex index)
	{
		this.index = index;
	}
	
	public KGramIndex getIndex()
	{
		return index;
	}
	
	public void setDocumentWeights(TreeMap<Integer,Double> map)
	{
		documentWeights = map;
	}
	
	public TreeMap<Integer,Double> getDocumentWeights()
	{
		return documentWeights;
	}
	
	public void addTerm(String term, int documentId, int position)
	{
		//if the term does not exist in the index
		if(mMap.get(term) == null)
		{
                    List<Posting> list = new ArrayList<>();
                    list.add(new Posting(documentId, position));
                    mMap.put(term, list);
		}
		// If it does exist in the index
		else
		{
                    // If the documentId is contained in the posting
                    int last = mMap.get(term).size()-1;
                    if(mMap.get(term)
                       .get(last)
                       .getDocumentId() == documentId)
                    {
                        // Adds the position of the term to the end of the list of positions in the correct document posting
                        mMap.get(term).get(last).getPositions().add(position);
                    }
                    // If the List of Postings exists, but the Correct posting does not
                    else
                    {
                        Posting temp = new Posting(documentId, position);
                        mMap.get(term).add(temp);
                    }
		}
	}

	@Override
	public List<Posting> getPostings(String term) {
            // TODO Auto-generated method stub
		if(mMap.get(term)== null)
			return new ArrayList<Posting>();
        return mMap.get(term);
	}
    
	
	public List<Posting> getPostings()
	{
//		List<Posting> posts = new ArrayList<>();
//		for(int i = 0; i < maxDoc; i++)
//		{
//			posts.add(new Posting(i));
//		}
//		return posts;
		HashSet<Posting> allPostings = new HashSet<>();
		for(Entry<String,List<Posting>> entry: mMap.entrySet())
		{
			allPostings.addAll(entry.getValue());
		}
		return new ArrayList<Posting>(allPostings);
	}

	@Override
	public List<String> getVocabulary() {
            // TODO Auto-generated method stub
            List<String> vocab = new ArrayList<>();
            vocab.addAll(mMap.keySet());
            Collections.sort(vocab);
            return vocab;
	}

    @Override
    public void print() {
        for(String s:mMap.keySet())
        {
            System.out.print(s+" ");
            for(Posting p: mMap.get(s))
            {
                System.out.print("docid: "+p.getDocumentId());
                for(Integer i:p.getPositions())
                {
                    System.out.print(i+" ");
                }
                System.out.println();
            }
        }
    }

    @Override
    public List<Integer> getDocIds(String term) {
        List<Posting> postings = mMap.get(term);
        List<Integer> list=new ArrayList();
        for(Posting p:postings)
        {
            list.add(p.getDocumentId());
        }
        return list;
    }

//	@Override
//	public void setDocumentWeights(TreeMap<Integer, Double> map) {
//		// TODO Auto-generated method stub
//		
//	}

//	@Override
//	public TreeMap<Integer, Double> getDocumentWeights(String path) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
