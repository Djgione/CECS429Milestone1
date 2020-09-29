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
/**
 *
 * @author Kermite
 */
public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter: build index");
        System.out.println("or stem");
        System.out.println("or vocab");
        System.out.println("or type query after building index");
        System.out.println("or q to quit");
        String str = scanner.nextLine();
       Indexer indexer = null;
        while (true) {
            str=scanner.nextLine();
            if (str.equals("q")) {
                System.exit(0);
            } else if (str.equals("Build index")) {
                System.out.println("enter directory path");
                //String path = scanner.nextLine();
                
                
                indexer=new Indexer(Paths.get("C:\\Users\\Kermite\\Documents\\MobyDick10Chapters").toAbsolutePath());

            } else if (str.equals("stem")) {
                //TODO implement stemmer code here;
            } else if (str.equals("vocab")) {
                //TODO print first 1000 words of vocab in sorted manner one term per line
            } else {
               System.out.print(String.valueOf(indexer.query("\"whale not\""))); 
            }

        }

    }

	
}
