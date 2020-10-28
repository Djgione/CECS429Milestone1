/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import cecs429.documents.DirectoryCorpus;
import cecs429.indexer.DiskIndexWriter;
import cecs429.indexer.Indexer;
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
    public static void main(String[] args) {
        System.out.println("Building index...");
        Indexer indexer = new Indexer(Paths.get("C:\\Users\\Daniel\\Desktop\\CECS 429\\CECS429Milestone1\\src\\corpus").toAbsolutePath(),"txt");
        System.out.println("\n...index built\n\n");
        DiskIndexWriter diskWriter = new DiskIndexWriter();
        BTreeMap<String,Long> results = diskWriter.writeIndex(indexer.getIndex()
        , Paths.get("C:\\Users\\Daniel\\Desktop\\CECS 429\\CECS429Milestone1\\src\\Parks10")
        .toAbsolutePath());
        List<String> vocab= indexer.getVocabulary();
        System.out.println("STARTING BYTE POSITIONS OF POSTINGS FOR EACH TERM");
        System.out.println("-------------------------------------------------");
   
        results.forEach((term,postingsBegin) -> {
            System.out.println(term + " -> " + postingsBegin);
        });
        
        diskWriter.getDb().close();
    }

}
