package cecs429.classification;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import cecs429.index.Index;
import cecs429.indexer.Indexer;
import cecs429.text.Constants;

public class RocchioValuesModel {
	
	Index disputedIndex;
	List<Index> indexList;
	List<Map<String,Double>> centroidValues;
	Set<String> vocabSet;
	List<Map<String,Double>> mappedDisputedDocs;
	List<List<Double>> distanceFromClassifications;
	List<Integer>disputedDocsClassifications;
	


	public RocchioValuesModel()
	{
		indexList = new ArrayList<>();
		Indexer jayIndexer = new Indexer(Paths.get(Constants.jayPath), "txt", 0);
		Indexer hamiltonIndexer = new Indexer(Paths.get(Constants.hamiltonPath),"txt", 0);
		Indexer madisonIndexer = new Indexer(Paths.get(Constants.madisonPath),"txt", 0);
		Indexer disputedIndexer = new Indexer(Paths.get(Constants.disputedPath),"txt", 0);
		
		disputedIndex = disputedIndexer.index();
		indexList.add(hamiltonIndexer.index());
		indexList.add(jayIndexer.index());
		indexList.add(madisonIndexer.index());
		vocabInit();
		mapInit();
	}
	
	private void vocabInit()
	{
		vocabSet = new TreeSet<String>();
		indexList.forEach(value -> vocabSet.addAll(value.getVocabulary()));
		vocabSet.addAll(disputedIndex.getVocabulary());
		
	}
	
	public Index getDisputedIndex()
	{
		return disputedIndex;
	}
	
	public List<Map<String, Double>> getMappedDisputedDocs() {
		return mappedDisputedDocs;
	}

	public void setMappedDisputedDocs(List< Map<String, Double>> mappedDisputedDocs) {
		this.mappedDisputedDocs = mappedDisputedDocs;
	}

	private void mapInit()
	{
		centroidValues = new ArrayList<>();
		mappedDisputedDocs = new ArrayList<>();
		
		
		for(int i = 0; i < indexList.size(); i++)
		{
			centroidValues.add(new TreeMap<String,Double>());
		}
		
		
		
		for(Map<String,Double> map : centroidValues)
		{
			for(String s : vocabSet)
			{
				map.put(s, 0.0);
			}
		}
		
		for(int i = 0; i < disputedIndex.getDocumentValuesModel().getDocWeights().size(); i++) 
		{
			TreeMap<String,Double> temp = new TreeMap<>();
			temp.putAll(centroidValues.get(0));
			mappedDisputedDocs.add(temp);
			
		}
	}
	
	public List<Index> getIndexList()
	{
		return indexList;
	}
	
	public void setCentroidValues(List<Map<String,Double>> centroidValues)
	{
		this.centroidValues = centroidValues;
	}

	public List<Map<String, Double>> getCentroidValues() {
		return centroidValues;
	}
	
	public Set<String> getVocabSet()
	{
		return vocabSet;
	}
	

	
	public List<List<Double>> getDistanceFromClassifications() {
		return distanceFromClassifications;
	}
	

	public void setDistanceFromClassifications(List<List<Double>> distanceFromClassifications) {
		this.distanceFromClassifications = distanceFromClassifications;	
		setDisputedDocsClassifications();
	}

	public List<Integer> getDisputedDocsClassifications() {
		return disputedDocsClassifications;
	}

		
	private void setDisputedDocsClassifications() {
		
		List<Integer> list = new ArrayList<>();
		
		for(int i = 0; i < distanceFromClassifications.size();i++)
		{
			
			double tempMin = distanceFromClassifications.get(i).get(0);
			int index = 0;
			for(int k = 1; k < distanceFromClassifications.get(i).size(); k++)
			{
				if(distanceFromClassifications.get(i).get(k) < tempMin)
				{
					tempMin = distanceFromClassifications.get(i).get(k);
					index = k;
				}
				
			}
			list.add(index);
		}
		
		disputedDocsClassifications = list;
	}
	
	public String getDisputedDocsClassificationsToString()
	{
		StringBuilder build = new StringBuilder();
		for(int i = 0; i < disputedDocsClassifications.size();i++)
		{
			build.append("Doc " + (i+49) + ": ");
			switch(disputedDocsClassifications.get(i)) {
			case 0:
				build.append("Hamilton");
			case 1: 
				build.append("Jay");
			case 2: 
				build.append("Madison");
			default:
				;
			}
			build.append("\n");
		}
		
		return build.toString();
	}
	
}
