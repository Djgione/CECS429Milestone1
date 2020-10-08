/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import cecs429.indexer.Indexer;
import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.index.PositionalInvertedIndex;
import cecs429.text.EnglishTokenStream;

import java.util.*;
import cecs429.indexer.Indexer;
import java.nio.file.Paths;
import java.util.HashSet;

import org.tartarus.snowball.SnowballStemmer;
/**
 *
 * @author Kermite
 */
public class main {
    public static void main(String[] args) {
       // System.out.print(Paths.get("").toAbsolutePath());
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter -> build index");
        System.out.println("         or stem");
        System.out.println("         or vocab");
        System.out.println("         or query");
        System.out.print("         or q to quit:");
        String str = scanner.nextLine();
       Indexer indexer = null;
        while (!str.equals("q")) 
        {
            if (str.equals("q")) {
                System.exit(0);
            } else if (str.equals("build index")) {
                System.out.println("enter directory path");
                //String path = scanner.nextLine();
                
                indexer=new Indexer(Paths.get("C:\\Users\\Daniel\\Desktop\\CECS 429\\CECS429Milestone1\\Project1\\src\\Parks10").toAbsolutePath(),"json");
                

            } else if (str.equals("stem")) {
            	try 
        		{
        			Class<?> stemClass = Class.forName("org.tartarus.snowball.ext." + "english" + "Stemmer");
        			SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();
        			stemmer.setCurrent("p*rk");
        			stemmer.stem();
        			System.out.println(stemmer.getCurrent());

        		}
            	catch(Exception e)
            	{
            		
            	}
            	
            } else if (str.equals("vocab")) {
                //TODO print first 1000 words of vocab in sorted manner one term per line
            } else if(str.equals("query")){
                DocumentCorpus dc=indexer.getCorpus();
                List<Posting> r = indexer.query("pa*k");
                for(Posting p:r)
                {
                    System.out.print(String.valueOf("Title"+dc.getDocument(p.getDocumentId()).getTitle()+" positions   " ));
//                    for(int i: p.getPositions())
//                    {
//                        System.out.print(i+" ");
//                    }
                    System.out.println();
                }
                
              // System.out.print(String.valueOf(r)); 
            }
            
//         System.out.println("\nenter build index");
//        System.out.println("         or stem");
//        System.out.println("         or vocab");
//        System.out.println("         or query");
//        System.out.print("         or q to quit:");
         str = scanner.nextLine();

        }

    }

	
}
