package cecs429.index;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cecs429.weights.DocumentValuesModel;

/**
 * An Index can retrieve postings for a term from a data structure associating terms and the documents
 * that contain them.
 */
public interface Index {
	/**
	 * Retrieves a list of Postings of documents that contain the given term.
	 */
	List<Posting> getPostings(String term) ;
	
	
	void setDocumentValuesModel(DocumentValuesModel model);
	DocumentValuesModel getDocumentValuesModel();
	
	/**
	 * Retrieves a list of all postings
	 * @return
	 */
	List<Posting> getPostings();
	
	List<Pair> getDocIds(String term);
	/**
	 * A (sorted) list of all terms in the index vocabulary.
	 */
	List<String> getVocabulary();

    public void addTerm(String s, int id, int i);


    public void print();

        
    public void setIndex(KGramIndex index);
    
        
    public KGramIndex getIndex();
    

	public int getDocFreq(String s);

	int getDocCount();

	int getTf(String s);

	HashSet<Integer>getAllDocs();
    


}
