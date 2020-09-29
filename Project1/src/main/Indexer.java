/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.index.Index;
import cecs429.index.PositionalInvertedIndex;
import cecs429.text.EnglishTokenStream;
import cecs429.text.IntermediateTokenProcessor;
import cecs429.text.TokenProcessor;
import cecs429.text.TokenStream;
import java.nio.file.Path;

/**
 *
 * @author kabir
 */
public class Indexer {
    private DocumentCorpus corpus;
    private Index index;
    TokenProcessor processor=new IntermediateTokenProcessor(); 

    public Indexer(Path path)
    {
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
                System.out.print(str);
                for(String s: processor.processToken(str))
                {
                    pInvertedIndex.addTerm(s,doc.getId(),pos++);
                }
            }
            
        }
        System.out.print(String.valueOf(pInvertedIndex.getVocabulary()));
        return pInvertedIndex;
    }
    
}
