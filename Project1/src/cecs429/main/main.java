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
import cecs429.index.Posting;
import cecs429.index.PositionalInvertedIndex;
import cecs429.text.BasicTokenProcessor;
import cecs429.text.EnglishTokenStream;
import java.util.*;

import java.nio.file.Paths;
import java.util.HashSet;
/**
 *
 * @author Kermite
 */
public class main {
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
	
}
