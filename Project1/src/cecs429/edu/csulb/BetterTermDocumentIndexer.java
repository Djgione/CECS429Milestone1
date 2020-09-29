package edu.csulb;

import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.index.PositionalInvertedIndex;
import cecs429.text.BasicTokenProcessor;
import cecs429.text.EnglishTokenStream;
import java.util.*;

import java.nio.file.Paths;
import java.util.HashSet;

public class BetterTermDocumentIndexer {
	public static void main(String[] args) {
            
            Scanner input = new Scanner(System.in);
            
            System.out.print("Enter a folder/directory to index: ");
            String directory = input.next();
            DocumentCorpus corpus = DirectoryCorpus.loadJsonDirectory(Paths.get(directory).toAbsolutePath(), ".json");
            Index index = indexCorpus(corpus);
            // We aren't ready to use a full query parser; for now, we'll only support single-term queries.

            BasicTokenProcessor bsp = new BasicTokenProcessor();
            System.out.print("Enter term: ");
            String query = input.next(); // user enters term to srch for
            
            //query = bsp.processToken(query);

            while(!query.equals("quit"))
            {
                for (Posting p : index.getPostings(query)) 
                {
                    System.out.println("Document " + corpus.getDocument(p.getDocumentId()).getTitle());
                }

                System.out.println();
                System.out.print("Enter term: ");
                query = input.next(); // user enters term to srch for
                //query = bsp.processToken(query);
            }
		
	}
	
	private static Index indexCorpus(DocumentCorpus corpus) {
            HashSet<String> vocabulary = new HashSet<>();
            BasicTokenProcessor processor = new BasicTokenProcessor();

            // First, build the vocabulary hash set.
            
            // TODO:
            // Get all the documents in the corpus by calling GetDocuments().
            // Iterate through the documents, and:
            // Tokenize the document's content by constructing an EnglishTokenStream around the document's content.
            // Iterate through the tokens in the document, processing them using a BasicTokenProcessor,
            //		and adding them to the HashSet vocabulary.
            for(Document d : corpus.getDocuments())
            {
                EnglishTokenStream tokenDoc = new EnglishTokenStream(d.getContent());
                               
                Iterable<String> tokens = tokenDoc.getTokens();
                
                
                for(String content : tokens)
                {
                    //vocabulary.add(processor.processToken(content));                  
                }
                
                
            }
            
            // TODO:
            // Constuct a TermDocumentMatrix once you know the size of the vocabulary.
            // THEN, do the loop again! But instead of inserting into the HashSet, add terms to the index with addPosting.
            /*TermDocumentIndex tDocMatrix = new TermDocumentIndex(vocabulary, vocabulary.size());
            
            for(Document d : corpus.getDocuments())         
            {
                EnglishTokenStream tokenDoc = new EnglishTokenStream(d.getContent());

                Iterable<String> tokens = tokenDoc.getTokens();
                
                for(String comp : tokens)
                {
                    comp = processor.processToken(comp);
                    
                    tDocMatrix.addTerm(comp, d.getId());                                
                }
            }*/
        
	}
}
