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
/**
 *
 * @author Kermite
 */
public class main {
    public static void main(String[] args) {
        System.out.println("Building index...");
        Indexer indexer = new Indexer(Paths.get("C:\\Users\\Kermite\\Documents\\NetBeansProjects\\HW5.0\\src\\corpus").toAbsolutePath(),"txt");
        System.out.println("\n...index built\n\n");
        DiskIndexWriter diskWriter = new DiskIndexWriter();
        List<Long> results = diskWriter.writeIndex(indexer.getIndex()
        , Paths.get("C:\\Users\\Kermite\\Documents\\NetBeansProjects\\HW5.0\\src\\corpus")
        .toAbsolutePath());
        List<String> vocab= indexer.getVocabulary();
        System.out.println("STARTING BYTE POSITIONS OF POSTINGS FOR TERMS");
        System.out.println("---------------------------------------------");
        System.out.println("Total terms: " + results.size());
        System.out.println("Total byte positions: " + vocab.size());
   
        for(int i = 0; i < vocab.size(); i++)
        {
            System.out.println("Byte position for Term " + vocab.get(i) + " : "
                                + results.get(i));
        }
    }

}
