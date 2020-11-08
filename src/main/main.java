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
    public static void main(String[] args) throws IOException, Exception 
    {
        System.out.println("Building index...");
        
        String path="C:\\Users\\Kermite\\CECS429Milestone1\\src\\corpus";
        
        Indexer indexer = new Indexer(Paths.get(path).toAbsolutePath(),"txt");
       indexer.getIndex().print();
        System.out.println("\n...index built\n\n");
        DiskIndexWriter diskWriter = new DiskIndexWriter(path);
       diskWriter.writeIndex(indexer.getIndex(),Paths.get(path).toAbsolutePath());

       DiskInvertedIndex di=new DiskInvertedIndex(path);
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
        
        di.closeandDeleteDB(path);
        diskWriter.DeleteBinFile(path);
    }

}
