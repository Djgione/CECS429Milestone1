/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import cecs429.documents.DirectoryCorpus;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 *
 * @author kabir
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter file path to index");
        String str = scanner.nextLine();
        Indexer indexer;
        while (true) {
            if (str.equals("q")) {
                System.exit(0);
            } else if (str.equals("Build index")) {
                System.out.println("enter directory path");
                String path = scanner.nextLine();
                indexer=new Indexer(Paths.get("").toAbsolutePath());

            } else if (str.equals("stem")) {
                //TODO implement stemmer code here;
            } else if (str.equals("vocab")) {
                //TODO print first 1000 words of vocab in sorted manner one term per line
            } else {

            }

        }

    }
}
