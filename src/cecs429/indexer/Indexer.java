/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.indexer;

import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.index.BiWordIndex;
import cecs429.index.Index;
import cecs429.index.KGramIndex;
import cecs429.index.PositionalInvertedIndex;
import cecs429.index.Posting;
import cecs429.queries.BiWordQuery;
import cecs429.queries.BooleanQueryParser;
import cecs429.queries.Query;
import cecs429.queries.WildcardLiteral;
import cecs429.text.EnglishTokenStream;
import cecs429.text.IntermediateTokenProcessor;
import cecs429.text.TokenProcessor;
import cecs429.text.TokenStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author kabir
 */
public class Indexer {
    private DocumentCorpus corpus;
    private Index index;
    TokenProcessor processor=new IntermediateTokenProcessor();
    private BiWordIndex biwordindex;
    BooleanQueryParser parser;
    KGramIndex kgramindex;
    

    public Indexer(Path path,String extension)
    {
        parser= new BooleanQueryParser();
        biwordindex=new BiWordIndex();
        kgramindex=new KGramIndex();
        
        if(extension.equals("json"))
        {
            corpus=DirectoryCorpus.loadJsonDirectory(path, ".json");
        }
        else if(extension.equals("txt"))
        {
            corpus=DirectoryCorpus.loadTextDirectory(path, ".txt");
        }
        index=index(corpus);
    }
    private Index index(DocumentCorpus corpus)
    {
        Index pInvertedIndex=new PositionalInvertedIndex(corpus.getCorpusSize());
        HashSet<String> noDupes = new HashSet<>();
        for(Document doc:corpus.getDocuments())
        {
            int pos=0;
            TokenStream stream=new EnglishTokenStream(doc.getContent());
            String s1="";
            String s2="";
            int i=0;
            for(String str : stream.getTokens())
            {
                
            	noDupes.add(str.toLowerCase());
                //System.out.print(str);
                for(String s: processor.processToken(str))
                {
                    if(i==0)
                    {
                        s1=s;
                        i++;
                    }
                    else
                    {
                        s2=s;
                        biwordindex.addTerm(s1+" "+s2, doc.getId());
                        s1=s2;
                    }
                    //System.out.println(s + "docid=" + doc.getId());
                    pInvertedIndex.addTerm(s,doc.getId(),pos++);
                    //kgramindex.addTerm(s,0,0);
                }
            }
        }
        
        
        for(String s: noDupes)
        {
        	kgramindex.addTerm(s, 0, 0);
        }
      pInvertedIndex.setIndex(kgramindex);
      biwordindex.setIndex(kgramindex);
      //pInvertedIndex.print();
        return pInvertedIndex; 
    }
    public DocumentCorpus getCorpus()
    {
        return corpus;
    }
    //Omar added this
    public Index getIndex() {return index;}
    
    public List<String> getVocab1000()
    {
        List<String> thousandTerms = new ArrayList<String>();
        List<String> allTerms = getVocabulary();

        for(int i = 0; i < 999; i++)
        {
            thousandTerms.add(allTerms.get(i));
        }

        return thousandTerms;
    }
public List<String> getVocabulary()
    {
        return index.getVocabulary();
    }
    
    public List<Posting> query(String query)
    {                          
        List<Posting> p;
        Query q = parser.parseQuery(query);
        if(q.isBiWord())
        	p=q.getPosting(biwordindex);
        else if(q.getnegative() && q.getClass() != WildcardLiteral.class) 
        	return notmerge(index.getPostings(), q.getPostings(index, new IntermediateTokenProcessor()));
        else p= q.getPostings(index,new IntermediateTokenProcessor());
        
        
        return p;               
    }
    
    private List<Posting> notmerge(List<Posting> list1, List<Posting> list2) {
        List<Posting> result=new ArrayList<>();
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

}
