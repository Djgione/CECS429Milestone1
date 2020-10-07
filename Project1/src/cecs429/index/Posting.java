package cecs429.index;

import java.util.ArrayList;
import java.util.List;

/**
 * A Posting encapulates a document ID associated with a search query component.
 */
public class Posting {
	private int mDocumentId;
	private List<Integer> mPositions;
	
	public Posting(int documentId, int position) {
		mDocumentId = documentId;
		mPositions = new ArrayList<>();
		mPositions.add(position);
	}
        public Posting(int documentId)
        {
            mDocumentId=documentId;
        }

    public Posting(int documentId, List<Integer> matchpositions) {
		mDocumentId = documentId;
                System.out.println(String.valueOf(matchpositions));
                mPositions = new ArrayList<>();
                mPositions.addAll(matchpositions);
    }
	
	public int getDocumentId() {
		return mDocumentId;
	}
	
	public List<Integer> getPositions()
	{
		return mPositions;
	}
	
}
