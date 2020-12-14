/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import cecs429.index.DiskInvertedIndex;
import cecs429.indexer.DiskIndexWriter;
import cecs429.indexer.Indexer;
import classifications.EuclideanEntry;
import classifications.knnClassification;
import cecs429.documents.Document;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Kermite
 */
import java.util.Map.Entry;


/**
 * 
 * Path List
 * 
 * "C:\\Users\\Kermite\\Documents\\NetBeansProjects\\HW5.0\\src\\corpus"
 * "C:\\Users\\Daniel\\Desktop\\CECS 429\\CECS429Milestone1\\src\\corpus"
 * 
 * Parks
 * "C:\\Users\\Daniel\\Desktop\\CECS 429\\CECS429Milestone1\\src\\Parks10"
 * "C:\\Users\\Kermite\\CECS429Milestone1\\src\\Parks10"
 * @author Daniel
 *
 */

public class main {
    public static void main(String[] args) throws IOException, Exception 
    {
        System.out.println("Building index...");
        
        String path = "C:\\Users\\Kermite\\eclipse-workspace\\CECS429Milestone2\\src\\corpus";
        String jayPath= path + "\\JAY";
        String madisonPath=path +"\\MADISON";
        String hamiltonPath=path + "\\HAMILTON";
        String disputedPath=path + "\\DISPUTED";
        String allPath = path + "\\ALLPAPERS";
        Indexer indexer1 = new Indexer(Paths.get(jayPath).toAbsolutePath(),"txt");
        Indexer indexer2 = new Indexer(Paths.get(madisonPath).toAbsolutePath(),"txt");
        Indexer indexer3 = new Indexer(Paths.get(hamiltonPath).toAbsolutePath(),"txt");
        Indexer indexer4 = new Indexer(Paths.get(disputedPath).toAbsolutePath(),"txt");
        Indexer bigIndexer = new Indexer(Paths.get(allPath).toAbsolutePath(), "txt");
        System.out.println("Vocab Size: " + bigIndexer.getVocabulary().size());

        for(int i = 0; i < 30; i++)
        {
     	   System.out.println(bigIndexer.getVocabulary().get(i));
        }
        System.out.println("\n...index built\n\n");
        DiskIndexWriter diskWriter = new DiskIndexWriter(path);

        
       diskWriter.writeIndex(bigIndexer.getIndex(),Paths.get(path).toAbsolutePath());
       diskWriter.closeDB();
       DiskInvertedIndex di = new DiskInvertedIndex(path);
       bigIndexer.setDiskIndex(di);
       List<Indexer> threeCorpuses = new ArrayList<>();
       threeCorpuses.add(indexer1);
       threeCorpuses.add(indexer2);
       threeCorpuses.add(indexer3);
       knnClassification knn = new knnClassification();
       System.out.println("before knn");
       
       knn.knn(threeCorpuses,bigIndexer, indexer4, 5);
        System.out.println("made it out of knn");
        
    }

}
