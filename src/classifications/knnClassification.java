package classifications;


import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cecs429.documents.Document;
import cecs429.indexer.Indexer;


public class knnClassification {

	private List<DocumentVector> dataDocs = new ArrayList<DocumentVector>();
	
	
	public void knn(List<Indexer> nonDisputed,
				    Indexer       allCorpuses, 
				    Indexer 	  disputedCorp, 
				    int 	      k)
	{
		boolean successfullyClassified = false;
		do
		{
			successfullyClassified = ClassifyKnn(nonDisputed,allCorpuses,disputedCorp,k);
			if(!successfullyClassified)
				k--;
		}while(!successfullyClassified);
	}
	
	public boolean ClassifyKnn(List<Indexer> nonDisputed,
							   Indexer 		 allCorpuses, 
							   Indexer 		 disputedCorp, 
							   int 	         k)
	{
        Map<Document, List<EuclideanEntry>> nearestNeighbors = findNearestNeighbors(allCorpuses, disputedCorp, k);
        printKnnResults(nearestNeighbors);
        Map<Document, List<Category>> map = new HashMap<Document, List<Category>>();
        for(Entry<Document, List<EuclideanEntry>> e : nearestNeighbors.entrySet())
        {
        	 List<Category> winners = new ArrayList<Category>();
        	 
        
	        for(int i = 0; i < nonDisputed.size(); i++)
	        {
	        	int score = countCategoryDocs(nonDisputed.get(i),e.getValue());
	        	winners.add(new Category(nonDisputed.get(i).getCorpusPath(),score,e.getKey()));
	        	
	        }
	        
	        if(!isTied(findMaxScore(winners),winners))
	        {
	        	printClassification(winners, e.getKey());
	        }
	        else
	        {
	        	System.out.println("returned false here");
	        	return false;
	        }
        }
        
        return true;
	}
	
	public void printClassification(List<Category> winners, Document nearestNeighbor)
	{
        	Category finalDecision = findMaxScore(winners);
	        
	        System.out.println("Based on KNN classification, \"" + nearestNeighbor.getTitle() + "\" belongs in " + finalDecision.getPath());
       
	}
	
	boolean isTied(Category maxWinner, List<Category> winners)
	{
		boolean isTied = false;
		
		double winningScore = maxWinner.getScore();
		
		int i = 0;
		
		while(!isTied && i < winners.size())
		{
			if(winners.get(i).getScore() == winningScore
			&& !winners.get(i).equals(maxWinner))
				isTied = true;
			
			i++;
		}
		
		return isTied;
	}
	public Category findMaxScore(List<Category> winners)
	{
		Category finalClass = winners.get(0);
		double max = 0;
		for(Category c : winners)
		{
			if(c.getScore() > max)
			{
				finalClass = c;
				max = c.getScore();
			}
				
		}
		
		return finalClass;
	}
	
	public int countCategoryDocs(Indexer category, List<EuclideanEntry> winners )
	{
		int total = 0;
		
		for(EuclideanEntry ee : winners)
		{
			for(Document d : category.getCorpus().getDocuments())
			{
				if(d.getTitle().equals(ee.getDoc().getTitle()))
				{
					total++;
				}
			}
		}
		
		return total;
	}
	
    public Map<Document, List<EuclideanEntry>> findNearestNeighbors(Indexer 		 allCorpuses, 
    											   Indexer 		 disputedCorp, 
    											   int 			 k) 
    {
        Map<Document, List<EuclideanEntry>> kResults = new HashMap<Document, List<EuclideanEntry>>();
        
        Iterable<Document> disputedDocs = disputedCorp.getCorpus().getDocuments();
        Map<Integer,List<Double>> disputedDataSet = buildDataSet(allCorpuses,disputedDocs);
        Map<Integer,List<Double>> fullTrainingSet = allCorpuses.getCalculator().termWeightSets();

        Map<Document,Double> eucDistances = new HashMap<Document,Double>();
        for(Entry<Integer,List<Double>> e : disputedDataSet.entrySet())
        {       		
        	List<Double> docVectorY = buildTspaceVector(allCorpuses, e);

        	dataDocs.add(new DocumentVector(allCorpuses.getCorpus().getDocument(e.getKey()),docVectorY));
        	
            for(Entry<Integer,List<Double>> te : fullTrainingSet.entrySet())
            {
            	if(!allCorpuses.getCorpus().getDocument(te.getKey()).getTitle()
        			.equals(allCorpuses.getCorpus().getDocument(e.getKey()).getTitle())
        		 && !oneOfDisputed(allCorpuses.getCorpus().getDocument(te.getKey()),disputedDocs))
            	{
            		List<Double> docVectorX = buildTspaceVector(allCorpuses, te);
                	
    	            eucDistances.put(allCorpuses.getCorpus().getDocument(te.getKey()), EuclideanDistance(docVectorX,docVectorY));
            	}
            }

            List<EuclideanEntry> temp = sortByValue(eucDistances);
            List<EuclideanEntry> targetWinners = new ArrayList<EuclideanEntry>();

            int i = 0; 
            for(EuclideanEntry targetEntry : temp)
            {
            	if(i < k)
            		targetWinners.add(targetEntry);
            	
            	i++;
            }
            
            kResults.put(allCorpuses.getCorpus().getDocument(e.getKey()), targetWinners);            
        }

        return kResults;
    }
    
    
    
    boolean oneOfDisputed(Document te, Iterable<Document> disputedDocs)
    {
    	for(Document d : disputedDocs)
    	{
    		if(d.getTitle().equals(te.getTitle()))
    			return true;
    	}
    	return false;
    }
    
    List<Double> buildTspaceVector(Indexer corpus,  Entry<Integer,List<Double>> docSet)
    {
    	List<Double> x = new ArrayList<Double>();
    	
	    Map<Integer, Map<String, Integer>> documentTermFrequencies = corpus.getIndex().getDocumentValuesModel().getMap();
	    
	    List<Double> docWeights = corpus.getIndex().getDocumentValuesModel().getDocWeights();
	    
	    Double docWeight = docWeights.get(docSet.getKey());
	    
	    Map<String,Integer> dtfDocVals = documentTermFrequencies.get(docSet.getKey());

	    for(String s : corpus.getVocabulary())
    	{
    		if(!dtfDocVals.containsKey(s))
    		{
    			
    			x.add(0.0);
    		}
    		else
    		{   
    			x.add((1 + Math.log(dtfDocVals.get(s)))/docWeight);   			
    		}
    	}
	    
    	
    	return x;
    }
    
    
    Map<Integer,List<Double>> buildDataSet(Indexer fullCorpus, Iterable<Document> disputedCorpDocs)
    {
    	Map<Integer,List<Double>> dataSet = new HashMap<Integer,List<Double>>();
    	Map<Integer,List<Double>> trainingSet = fullCorpus.getCalculator().termWeightSets();
    	

    	for(Document d : disputedCorpDocs)
    	{       	
    		List<Integer> keys = new ArrayList<Integer>(trainingSet.keySet());
    		
    		List<List<Double>> values = new ArrayList<List<Double>>(trainingSet.values());
    		for(int i = 0; i < keys.size(); i++)
    		{
    			if(fullCorpus.getCorpus().getDocument(i).getTitle().equals(d.getTitle()))
    			{
    				dataSet.put(i, values.get(i));
    			}
    		}
    	}

    	return dataSet;
    }
    
    
    public Double EuclideanDistance(List<Double> xWdt, List<Double> yWdt)
    {
    	Double sum = 0.0;
    	for(int i = 0; i < xWdt.size(); i++)
    	{
    		sum += Math.pow(xWdt.get(i) - yWdt.get(i), 2);
    	}
    	
    	sum = Math.sqrt(sum);
    	
    	return sum;
    }

    public void printKnnResults(Map<Document, List<EuclideanEntry>> disputedResults)
    {
    	DecimalFormat df = new DecimalFormat("#.######");
    	df.setRoundingMode(RoundingMode.CEILING);
    	
    	for(DocumentVector dv : dataDocs)
    	{
    		System.out.println("\n\nFirst 30 components of vector for \"" + dv.getDataDoc().getTitle() + "\"\n");
    		
    		for(int i = 0; i < 30; i++)
    			System.out.print(df.format(dv.getComponents().get(i)) + " ");
    	}
        for(Entry<Document, List<EuclideanEntry>>entry : disputedResults.entrySet())
        {
            System.out.println("\n\"" + entry.getKey().getTitle() + "\"" + " is nearest to:");

            int count = 1;
            
            for(EuclideanEntry winner : entry.getValue())
            {
                System.out.println(count + ": \"" + winner.getDoc().getTitle() + "\"" + "(" + df.format(winner.getEuclideanDistance()) + ")");
                
                count++;
            }
        }
    }

    // function to sort hashmap by values 
    private  List<EuclideanEntry>  sortByValue(Map<Document, Double> eucD) {
        // Create a list from elements of HashMap
        List<Map.Entry<Document, Double>> list = new LinkedList<Map.Entry<Document, Double>>(eucD.entrySet());
        
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<Document,Double> >() { 
            public int compare(Map.Entry<Document,Double> o1,  
                               Map.Entry<Document,Double> o2) 
            { 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
        
        // put data from sorted list to hashmap  
        List<EuclideanEntry> distances = new ArrayList<EuclideanEntry>();
        for (Entry<Document,Double> aa : list) { 
        	distances.add(new EuclideanEntry(aa.getKey(),aa.getValue()));
        } 
        
        return distances; 
    
    } 
}
