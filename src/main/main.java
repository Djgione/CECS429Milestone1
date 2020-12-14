/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import cecs429.classification.RocchioClassification;
import cecs429.classification.RocchioValuesModel;
import cecs429.documents.DirectoryCorpus;
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
import java.io.IOException;
import java.nio.file.Path;
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


		Indexer indexer = new Indexer(Paths.get(Constants.currentPath),"txt");
		//indexer.getIndex().print();
		System.out.println("\n...index built\n\n");
//		DiskIndexWriter diskWriter = new DiskIndexWriter(Constants.currentPath);

//
//		diskWriter.writeIndex(indexer.getIndex(),Paths.get(Constants.currentPath).toAbsolutePath());
//		diskWriter.writeKgramIndex(indexer.getKgramIndex(),Paths.get(Constants.currentPath).toAbsolutePath());
//
//		DiskInvertedIndex di=new DiskInvertedIndex(Constants.currentPath);
//		List<String> vocab= di.getVocabulary();
//		List<String> indexVocab = indexer.getVocabulary();
//		System.out.println("vocab.size(): " + vocab.size());

//		System.out.println("indexer vocab size: " + indexVocab.size());

//		DiskKgramIndex dki=new DiskKgramIndex(Constants.currentPath);
//
//		indexer.setDiskIndex(di);
//		indexer.setDiskKgram(dki);
//		List<String> results = indexer.rankedQuery("*t * t*e");

//		for(String s : results)
//		{
//			System.out.println(s);
//		}

//		di.closeandDeleteDB(Constants.currentPath);
//		diskWriter.DeleteBinFiles(Constants.currentPath);
//		dki.closeandDeleteDB(Constants.currentPath);
//		diskWriter.DeleteKgramBinFiles(Constants.currentPath);
//		
		RocchioClassification classifier = new RocchioClassification();
		RocchioValuesModel model = new RocchioValuesModel();
		//List<Map<String,Double>> centroids = new ArrayList<>();
		model.setCentroidValues(classifier.findCentroid(model));
		model.setMappedDisputedDocs(classifier.createDisputedDocVectors(model));
		model.setDistanceFromClassifications(classifier.calculateDistance(model));

		System.out.println("Order:\nHamilton\nJay\nMadison");
		
//		for(int i = 0; i < 3; i++)
//		{
//			List<String> keys = new ArrayList<>(model.getCentroidValues().get(i).keySet());
//			for(int k = 0; k < 10; k++)
//			{
//				System.out.print(keys.get(k) + ": ");
//				System.out.print(model.getCentroidValues().get(i).get(keys.get(k)) + " | ");
//			}
//
//		}
		System.out.println("\n\npaper52 Rocchio:");
		System.out.println("Components:");
		
		List<String> keys = new ArrayList<>(model.getMappedDisputedDocs().get(3).keySet());
		System.out.print("<");
		for(int i = 0; i < 30; i++)
		{
			System.out.print(model.getMappedDisputedDocs().get(3).get(keys.get(i)) + ", ");
		}
		System.out.println(">");
		System.out.println("Distances:");
		
		for(int i = 0; i < 3; i++)
		{
			System.out.println(model.getDistanceFromClassifications().get(3).get(i));
		}

		System.out.println("\n" +model.getDisputedDocsClassificationsToString());
		
//		List<List<Double>> classifications = classifier.classifyDocs(disputedIndex, centroids);
//		for(List<Double> i : classifications)
//		{
//			i.forEach(value -> System.out.print(value + " | "));
//			System.out.println();
//		}
//		
	}
	

}
