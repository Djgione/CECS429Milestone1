package cecs429.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import cecs429.weights.DocumentValuesModel;

import java.util.HashMap.*;
import java.util.HashSet;

public class PositionalInvertedIndex implements Index {
	int totalTokens;
	private final HashMap<String, List<Posting>> mMap;
	private KGramIndex index;
	private int maxDoc;
	private DocumentValuesModel model;
	HashSet<Integer> docs=new HashSet();
	public int totalDocs;
	public HashMap<String,Integer> termfreq =new HashMap<String,Integer>();
	public static HashMap<String,Integer> corpfreq =new HashMap<String,Integer>();
	
	public PositionalInvertedIndex(int num)
	{
		totalTokens = 0;
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
	
	
	public void addTerm(String term, int documentId, int position)
	{
		totalTokens++;
		docs.add(documentId);
		//if the term does not exist in the index
		if(mMap.get(term) == null)
		{
				corpfreq.put(term,corpfreq.getOrDefault(term,0)+1);
				termfreq.put(term,termfreq.getOrDefault(term,0)+1);
                List<Posting> list = new ArrayList<>();
                list.add(new Posting(documentId, position));
                mMap.put(term, list);
		}
		// If it does exist in the index
		else
		{
                    // If the documentId is contained in the posting
                    int last = mMap.get(term).size()-1;
                    corpfreq.put(term,corpfreq.getOrDefault(term,0)+1);
                    termfreq.put(term,termfreq.getOrDefault(term,0)+1);
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
    public List<Pair> getDocIds(String term) {
        List<Posting> postings = mMap.get(term);
        List<Pair> list=new ArrayList<>();
        if(postings == null) return new ArrayList<>();
        for(Posting p:postings)
        {
            list.add(new Pair(p.getDocumentId(),p.getPositions().size()));
        }
        return list;
    }

	@Override
	public void setDocumentValuesModel(DocumentValuesModel model) {
		this.model = model;
	}

	@Override
	public DocumentValuesModel getDocumentValuesModel() {
		// TODO Auto-generated method stub
		return model;
	}
//
//	@Override
//	public int getDocCount() {
//		// TODO Auto-generated method stub
//		return maxDoc;
//	}


	public int getTf(String s)
	{
		
		if(termfreq.get(s)!=null)
		{
			return termfreq.get(s);
		}
		else return 0;
	}
	public int getCf(String s)
	{
		return corpfreq.get(s);

	}
	public HashSet<Integer> getAllDocs()
	{
		return docs;
	}
	
//
	@Override
	public int getDocCount() {
		// TODO Auto-generated method stub
		return docs.size();
	}

    public int getDocFreq(String term)
    {
    	
    	List<Posting> postings = mMap.get(term);
    	if(postings==null)return 0;
    	return postings.size();
    	
    }

}
