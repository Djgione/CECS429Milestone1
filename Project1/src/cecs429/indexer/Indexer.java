/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.indexer;

import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.index.Index;
import cecs429.index.PositionalInvertedIndex;
import cecs429.index.Posting;
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
    BooleanQueryParser parser;

    public Indexer(Path path)
    {
        parser= new BooleanQueryParser();
        
//        if(path.endsWith(".json"))
//        {
//            corpus=DirectoryCorpus.loadJsonDirectory(path, ".json");
//        }
//        else if(path.endsWith(".txt"))
//        {
            corpus=DirectoryCorpus.loadTextDirectory(path, ".txt");
//        }
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
            for(String str : stream.getTokens())
            {
                for(String s: processor.processToken(str))
                {
                    pInvertedIndex.addTerm(s,doc.getId(),pos++);
                }
            }

        }

        /*List<Posting> posting= pInvertedIndex.getPostings("whale");
        for(Posting p:posting)
        {
            System.out.println(p.getDocumentId() +" "+String.valueOf(p.getPositions()) );
        }
            //System.out.println(String.valueOf(pInvertedIndex.getPostings("whale")));
        //System.out.print(String.valueOf(pInvertedIndex.getVocabulary()));*/
        return pInvertedIndex; 
    }
    public List<Posting> query(String query)
    {                          
        Query q = parser.parseQuery(query);
        
        List<Posting> p = q.getPostings(index);

        
        return p;
                
    }

}