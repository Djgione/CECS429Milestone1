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
        
        String path="C:\\Users\\Daniel\\Desktop\\CECS 429\\CECS429Milestone1\\src\\corpus";
        
        Indexer indexer = new Indexer(Paths.get(path).toAbsolutePath(),"txt");
        //indexer.getIndex().print();
        System.out.println("\n...index built\n\n");
        DiskIndexWriter diskWriter = new DiskIndexWriter(path);
        
        diskWriter.writeIndex(indexer.getIndex(),Paths.get(path).toAbsolutePath());

        DiskInvertedIndex di=new DiskInvertedIndex(path);
        indexer.setDiskIndex(di);
//       List<String> vocab= di.getVocabulary();
//       List<String> indexVocab = indexer.getVocabulary();
//       System.out.println("vocab.size(): " + vocab.size());
//
//       System.out.println("indexer vocab size: " + indexVocab.size());
//      List<Posting> postings = di.getPostings();
//       for(int i = 0; i < vocab.size(); i++)
//       {
//           System.out.println(vocab.get(i)+ " -> "+ postings.get(i).toString());
//           
//       }
        //DiskKgramIndex dki=new DiskKgramIndex(path+"/index");
//      System.out.print("dki   "+dki.getPostings("bro"));
      //SpellingCorrector sp= new SpellingCorrector(dki,di);
        
//        List<String> vocab = di.getVocabulary();
//        List<Posting> postings = di.getPostings("it");
//        for(int i = 0; i < postings.size(); i++)
//        {
//        	System.out.println(vocab.get(i) + "=> " + postings.get(i).toString());
//        }
//        
        List<String> results = indexer.rankedQuery("it is the");
        
        for(String s : results)
        {
        	System.out.println(s);
        }
        
        //System.out.print(sp.calculateJacard("gosling", sp.makegrams("$gost$")));
        //sp.checkFor("bst");
        di.closeandDeleteDB(path);
        diskWriter.DeleteBinFile(path);

    }

}
