package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import cecs429.index.Index;

public class RocchioClassification {

	//DocumentValuesModel.getDocWeights.size == num of docs
	///DocumentValuesModel.getDocWeights gets the v->(d)
	
	
	public RocchioClassification()
	{
		
	}
	
	
	public List<Map<String,Double>> findCentroid(RocchioValuesModel model)
	{
		
		List<Map<String,Double>> maps = new ArrayList<>();
		
		for(int i = 0; i < model.indexList.size(); i++) 
		{
			Index index = model.getIndexList().get(i);
			Map<String, Double> answer = model.getCentroidValues().get(i);
			List<Double> docWeights = index.getDocumentValuesModel().getDocWeights();
			Map<Integer,Map<String,Integer>> wdtMap = index.getDocumentValuesModel().getMap();
			double divisor = Math.abs(docWeights.size());
			
			
			
			// E v->(d)
			for(int k = 0; k < docWeights.size(); k++)
			{
				
				Map<String, Double> temp = wdtMap.get(k).entrySet()
						.stream()
						.collect(Collectors.toMap(Map.Entry::getKey,e -> e.getValue() + 0.0));
				
				double tempWeight = docWeights.get(k);
				
				temp.replaceAll( (key,v) -> v = (1 + Math.log(v))/tempWeight );
				
				//model.getCentroidValues().get(i); gets the empty map
				temp.forEach((key,v) ->
					answer.merge(key, v, (v1, v2) ->
							v1 + v2 )
						);
				
			}
			
			
			// 1/|Dc|
			answer.replaceAll( (key, v) -> v /= divisor);
			maps.add(answer);
		}
		
		return maps;
	}
	
	
	
	/**
	 * Calculates distance from 
	 * @param model
	 * @return Each disputed document is outer list element, each inner list contains its distance from the classification classes
	 */
	public List<List<Double>> calculateDistance(RocchioValuesModel model)
	{
		
		List<List<Double>> distances = new ArrayList<>();
		List<Map<String,Double>> docVectors = model.getMappedDisputedDocs();
		for(int i = 0; i < docVectors.size(); i++)
		{
			Map<String,Double> tempDocVector = docVectors.get(i);
			List<Double> tempInnerList = new ArrayList<>();
			for(int k = 0; k < model.getCentroidValues().size(); k++)
			{
				double totalDistance = 0.0;
				Map<String,Double> centroid = model.getCentroidValues().get(k);
				
				for(String s : model.getVocabSet())
				{
					totalDistance += Math.pow(centroid.get(s) - tempDocVector.get(s),2);	
				}
				
				totalDistance = Math.sqrt(totalDistance);
				tempInnerList.add(totalDistance);
			}
			distances.add(tempInnerList);
		}	
	
		return distances;
	}
	
	
	/**
	 * Creates the list of maps for each disputed doc vector
	 * @param model
	 * @return
	 */
	public List<Map<String,Double>> createDisputedDocVectors(RocchioValuesModel model)
	{
		
		List<Map<String,Double>> results = model.getMappedDisputedDocs();
		Index index = model.getDisputedIndex();
		Map<Integer, Map<String,Integer>> wdtMap = index.getDocumentValuesModel().getMap();
		List<Double> docWeights = index.getDocumentValuesModel().getDocWeights();
		
		
		for(int i = 0; i < docWeights.size(); i++)
		{
			Map<String,Double> fromResults = results.get(i);
			Map<String, Double> temp = wdtMap.get(i).entrySet()
					.stream()
					.collect(Collectors.toMap(Map.Entry::getKey,e -> e.getValue() + 0.0));
			
			double tempWeight = docWeights.get(i);
			
			temp.replaceAll( (key,v) -> v = (1 + Math.log(v))/tempWeight );
			
			//model.getCentroidValues().get(i); gets the empty map
			temp.forEach((key,v) ->
				fromResults.merge(key, v, (v1, v2) ->
						v1 + v2 )
					);
			
			
			
			results.set(i,fromResults);
			
		}

		return results;
		
	}
}
	
	
	

	
