/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import cecs429.documents.DirectoryCorpus;
import cecs429.documents.JsonFileDocument;
import cecs429.index.DiskInvertedIndex;
import cecs429.index.DiskKgramIndex;
import cecs429.index.Posting;
import cecs429.index.SpellingCorrector;
import cecs429.indexer.DiskIndexWriter;
import cecs429.indexer.Indexer;
import cecs429.queries.BooleanQueryParser;
import cecs429.text.Constants;
import cecs429.text.IntermediateTokenProcessor;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import org.mapdb.BTreeMap;
/**
 *
 * @author Kermite
 */


/**
 * 
 * Path List
 * 
 * "C:\\Users\\Kermite\\Documents\\NetBeansProjects\\HW5.0\\src\\corpus"
 * "C:\\Users\\Daniel\\Desktop\\CECS 429\\CECS429Milestone1\\src\\corpus"
 * 
 * Parks
 * "C:\\Users\\Daniel\\Desktop\\CECS 429\\CECS429Milestone1\\src\\Parks10"
 * "C:\\Users\\Kermite\\Documents\\NetBeansProjects\\HW5.0\\src\\Parks10"
 * @author Daniel
 *
 */

public class main {
    public static void main(String[] args) throws IOException, Exception 
    {
        System.out.println("Building index...");
        
        
        Indexer indexer = new Indexer(Paths.get(Constants.currentPath),"txt");
        //indexer.getIndex().print();
        System.out.println("\n...index built\n\n");
        DiskIndexWriter diskWriter = new DiskIndexWriter(Constants.currentPath);

        
       diskWriter.writeIndex(indexer.getIndex(),Paths.get(Constants.currentPath).toAbsolutePath());
       diskWriter.writeKgramIndex(indexer.getKgramIndex(),Paths.get(Constants.currentPath).toAbsolutePath());

       DiskInvertedIndex di=new DiskInvertedIndex(Constants.currentPath);
       List<String> vocab= di.getVocabulary();
       List<String> indexVocab = indexer.getVocabulary();
       System.out.println("vocab.size(): " + vocab.size());

       System.out.println("indexer vocab size: " + indexVocab.size());
//       List<Posting> postings = di.getPostings("it");
//       for(int i = 0; i < postings.size(); i++)
//       {
//           System.out.println(vocab.get(i)+ " -> "+ postings.get(i).toString());
//           
//       }
        DiskKgramIndex dki=new DiskKgramIndex(Constants.currentPath);
//        System.out.print("dki   "+dki.getPostings("bro"));
//        SpellingCorrector sp= new SpellingCorrector(dki,di);
//        System.out.print(sp.calculateJacard("gosling", sp.makegrams("$gost$")));
//        sp.checkFor("bst");
        indexer.setDiskIndex(di);
        indexer.setDiskKgram(dki);
        List<String> results = indexer.rankedQuery("*t * t*e");
        
        for(String s : results)
        {
        	System.out.println(s);
        }

        di.closeandDeleteDB(Constants.currentPath);
        diskWriter.DeleteBinFiles(Constants.currentPath);
        dki.closeandDeleteDB(Constants.currentPath);
        diskWriter.DeleteKgramBinFiles(Constants.currentPath);
    }

}
