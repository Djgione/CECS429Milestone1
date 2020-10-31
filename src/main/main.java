/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import cecs429.documents.DirectoryCorpus;
import cecs429.index.DiskInvertedIndex;
import cecs429.index.Posting;
import cecs429.indexer.DiskIndexWriter;
import cecs429.indexer.Indexer;
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
        Indexer indexer = new Indexer(Paths.get(path).toAbsolutePath(),"json");
      // indexer.getIndex().print();
        //System.out.println("\n...index built\n\n");
     //   DiskIndexWriter diskWriter = new DiskIndexWriter(path);
        DiskInvertedIndex di=new DiskInvertedIndex(path);
        List<String> vocab= indexer.getVocabulary();
        //System.out.print(vocab.size());
//        diskWriter.writeIndex(indexer.getIndex()
//        , Paths.get(path)
//        .toAbsolutePath());
        for(String s:vocab)
        {
            List <Integer> l1=indexer.getIndex().getDocIds(s);
            List <Integer> l2= di.getDocIds(s);
            System.out.println("term :" +s);
            System.out.println("from l1:"+String.valueOf(l1));
            System.out.println("from l2:"+String.valueOf(l2));
//            for(Integer p:l1)
//            {
//                System.out.println("From l1:");
//                System.out.println("docid: "+p+"   ");
//                
//                
//            }
//            for(Integer p:l2)
//            {
//                System.out.println("From l2:");
//                System.out.println("docid: "+p+"   ");
//                
//                
//            }
            
        }
        System.out.println("STARTING BYTE POSITIONS OF POSTINGS FOR EACH TERM");
        System.out.println("-------------------------------------------------");
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
