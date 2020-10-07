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
import cecs429.text.EnglishTokenStream;
import cecs429.text.IntermediateTokenProcessor;
import cecs429.text.TokenProcessor;
import cecs429.text.TokenStream;
import java.nio.file.Path;
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
        Index pInvertedIndex=new PositionalInvertedIndex();
        System.out.print(corpus.getCorpusSize());
        
        for(Document doc:corpus.getDocuments())
        {
            
            int pos=0;
            TokenStream stream=new EnglishTokenStream(doc.getContent());
            String s1="";
            String s2="";
            int i=0;
            for(String str : stream.getTokens())
            {
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
                }
            }

        }
        for(String s:pInvertedIndex.getVocabulary())
        {
            kgramindex.addGram(s);
        }
        
      //build  biwordindex.print();
        

//        List<Posting> posting= pInvertedIndex.getPostings("whale");
//        for(Posting p:posting)
//        {
//            System.out.println(p.getDocumentId() +" "+String.valueOf(p.getPositions()) );
//        }
//            System.out.println(String.valueOf(pInvertedIndex.getPostings("whale")));
//        System.out.print(String.valueOf(pInvertedIndex.getVocabulary()));
       //build  kgramindex.print();
      //  biwordindex.print();
      pInvertedIndex.print();
        return pInvertedIndex; 
    }
    public DocumentCorpus getCorpus()
    {
        return corpus;
    }
    public List<Posting> query(String query)
    {                          
        List<Posting> p;
        Query q = parser.parseQuery(query);
        if(q.isBiWord())p=q.getPosting(biwordindex);
        else p= q.getPostings(index,new IntermediateTokenProcessor());
        
        
        return p;
                
    }

}