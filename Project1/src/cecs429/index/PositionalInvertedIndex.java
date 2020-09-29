package cecs429.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PositionalInvertedIndex implements Index {
	private final HashMap<String, List<Posting>> mMap;
	
	public PositionalInvertedIndex()
	{
		mMap = new HashMap<>();
                System.out.print("in pii");
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
		return mMap.get(term);
	}

	@Override
	public List<String> getVocabulary() {
		// TODO Auto-generated method stub
		List<String> vocab = new ArrayList<>();
		vocab.addAll(mMap.keySet());
		Collections.sort(vocab);
		return vocab;
	}

}
