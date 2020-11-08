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
public class main {
    public static void main(String[] args) throws IOException, Exception {
        System.out.println("Building index...");
        String path="/Users/kabir/NetBeansProjects/CECS429Milestone1/src/Parks10";
        //Indexer indexer = new Indexer(Paths.get(path).toAbsolutePath(),"json");
        
        // indexer.getIndex().print();
        //System.out.println("\n...index built\n\n");
        //DiskIndexWriter diskWriter = new DiskIndexWriter(path+"/index");
        //diskWriter.writeIndex(indexer.getIndex()
        //, Paths.get(path+"/index")
        //.toAbsolutePath());
//        DiskInvertedIndex di=new DiskInvertedIndex(path);
        //diskWriter.writeKgramIndex(indexer.getKgramIndex(),Paths.get(path+"/index").toAbsolutePath());
        //System.out.print(indexer.getKgramIndex().getPostings("bro"));
         DiskKgramIndex dki=new DiskKgramIndex(path+"/index");
//        System.out.print("dki   "+dki.getPostings("bro"));
        SpellingCorrector sp=new SpellingCorrector(dki);
        //System.out.print(sp.calculateJacard("gosling", sp.makegrams("$gost$")));
        sp.checkFor("bst");
        // List<String> vocab= indexer.getVocabulary();
//        BooleanQueryParser qp=new BooleanQueryParser();
        //JsonFileDocument j=new JsonFileDocument(path,);
//        for(Posting p:qp.parseQuery("").getPostings(di, new IntermediateTokenProcessor()))
//        {
//            System.out.println(p.getDocumentId());
//        }
        //System.out.print(vocab.size());
//        
//        for(String s:vocab)
//        {
//            List <Posting> l1=indexer.getIndex().getPostings(s);
//            List <Posting> l2= di.getPostings(s);
//            System.out.println("term :" +s);
////            System.out.println("from l1:"+String.valueOf(l1));
////            System.out.println("from l2:"+String.valueOf(l2));
//            for(Posting p:l1)
//            {
//                System.out.println("From l1:");
//                System.out.print("docid: "+p.getDocumentId()+"   ");
//                System.out.println(String.valueOf(p.getPositions()));
//                
//                
//            }
//            for(Posting p:l2)
//            {
//                System.out.println("From l2:");
//                System.out.print("docid: "+p.getDocumentId()+"   ");
//                System.out.println(String.valueOf(p.getPositions()));
//                
//            }
//            
//        }

        //int i=0;
//        BTreeMap<String,Long> results=diskWriter.getBplustree();
//        for(String s:results.getKeys())
//        {
//            System.out.println(s+" -> "+ results.get(s));
//            i++;
//        }
//        results.forEach((term,postingsBegin) -> {
//            System.out.println(term + " -> " + postingsBegin);
//            i++;
//        });
//        System.out.println("no of terms in b+ tree: "+ i);
       
       //diskWriter.getDb().close();
        
    }

}
