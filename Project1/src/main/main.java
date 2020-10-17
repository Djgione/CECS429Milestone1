/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import cecs429.documents.DirectoryCorpus;
import cecs429.indexer.Indexer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
/**
 *
 * @author Kermite
 */
public class main {
    public static void main(String[] args) {
        System.out.println("Building index...");
        Indexer index = new Indexer(Paths.get("/corpus"),"txt");
        System.out.println("\n\nindex built\n");
    }

}
