/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.main;
import cecs429.classification.Bayes;
import cecs429.classification.RocchioClassification;
import cecs429.classification.RocchioValuesModel;
import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.JsonFileDocument;
import cecs429.index.DiskInvertedIndex;
import cecs429.index.DiskKgramIndex;
import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.index.SpellingCorrector;
import cecs429.indexer.DiskIndexWriter;
import cecs429.indexer.Indexer;
import cecs429.queries.BooleanQueryParser;
import cecs429.text.Constants;
import cecs429.text.IntermediateTokenProcessor;
import cecs429.index.DiskInvertedIndex;
import cecs429.indexer.DiskIndexWriter;
import cecs429.indexer.Indexer;
import cecs429.classification.*;
import cecs429.documents.Document;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import javax.sound.midi.Sequence;
import org.mapdb.BTreeMap;
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
		RocchioClassification classifier = new RocchioClassification();
		RocchioValuesModel model = new RocchioValuesModel();
		
		model.setCentroidValues(classifier.findCentroid(model));
		model.setMappedDisputedDocs(classifier.createDisputedDocVectors(model));
		model.setDistanceFromClassifications(classifier.calculateDistance(model));

		
		System.out.println("\n\npaper52 Rocchio:");
		System.out.println("Components:");

		List<String> keys = new ArrayList<>(model.getMappedDisputedDocs().get(3).keySet());
		System.out.print("<");
		for(int i = 0; i < 30; i++)
		{
			System.out.print(model.getMappedDisputedDocs().get(3).get(keys.get(i)) + ", ");
			if(i==20)
				System.out.println();
		}
		System.out.println(">");
		System.out.println("Order:\nHamilton\nJay\nMadison");
		System.out.println("Distances:");

		for(int i = 0; i < 3; i++)
		{
			System.out.println(model.getDistanceFromClassifications().get(3).get(i));
		}

		System.out.println("\n" +model.getDisputedDocsClassificationsToString());
        
        


                //String path="/Users/kabir/NetBeansProjects/CECS429Milestone2/src/";   
        
        
		
		Indexer indexer4 = new Indexer(Paths.get(Constants.disputedPath).toAbsolutePath(),"txt",0);
		Indexer bigIndexer = new Indexer(Paths.get(Constants.allPath).toAbsolutePath(), "txt",0);
		Indexer hamiltonIndexer = new Indexer(Paths.get(Constants.hamiltonPath).toAbsolutePath(),"txt",0);
		Indexer jayIndex=new Indexer(Paths.get(Constants.jayPath).toAbsolutePath(),"txt",0);
		Indexer madisonIndex=new Indexer(Paths.get(Constants.madisonPath).toAbsolutePath(),"txt",0);
		indexer4.index();
		bigIndexer.index();


		Index hamiltonI=hamiltonIndexer.index();
		Index jayI=jayIndex.index();
		Index madisonI=madisonIndex.index();
		Sequence s;
		System.out.println(madisonI.getVocabulary().size()+hamiltonI.getVocabulary().size());

		//1.get total no of documnets from corpus and total no of documents in each catagory 
		//find N00- documents that dont have the term and are are not in the catagory.
	//		  2.N11- documents  documents that  have the term and in the catagory.
	//        3.N10- documents that have the term and are not in the catagory
	//        4.N01- documents that do not have the term and are in the catagory.

	        Bayes b=new Bayes(hamiltonI,jayI,madisonI);
		Indexer indexer=new Indexer(Paths.get(Constants.disputedPath).toAbsolutePath(),"txt",0);
	       
	        for(Document d:indexer.getCorpus().getDocuments())
	        {
	        	System.out.println("for document"+d.getTitle());
	        	double h=(double)b.classify(d,hamiltonI);
	        	double j=(double)b.classify(d,jayI);
	        	double m=(double)b.classify(d,madisonI);
	        	System.out.println("hamilton:"+"  "+ h);
	        	System.out.println("jay"+"  "+ j);
	        	System.out.println("madison:"+"  "+ m);
	       	
	        	double temp=max(h,j,m);
	        	if(h==temp)System.out.print("predicted author - Hamilton");
	        	else if(j==temp)System.out.print("predicted author - Jay");
	        	else if(m==temp)System.out.print("predicted author - Madison");
	       	
	        	System.out.println("");
	        	System.out.println("");
	        	System.out.println("");
	        	System.out.println("");
	       	
	        }


	       List<Indexer> threeCorpuses = new ArrayList<>();
	       threeCorpuses.add(jayIndex);
	       threeCorpuses.add(madisonIndex);
	       threeCorpuses.add(hamiltonIndexer);
	       knnClassification knn = new knnClassification();

	       knn.knn(threeCorpuses,bigIndexer, indexer4, 5);
        
    }
	
    public static double max(double a,double b,double c)
    {
    	return Math.max(a,Math.max(b,c));
    }


}
