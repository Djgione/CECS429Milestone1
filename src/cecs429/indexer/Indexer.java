/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.indexer;

import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.index.BiWordIndex;
import cecs429.index.DiskKgramIndex;
import cecs429.index.Index;
import cecs429.index.KGramIndex;
import cecs429.index.PositionalInvertedIndex;
import cecs429.index.Posting;
import cecs429.queries.BooleanQueryParser;
import cecs429.queries.DefaultRankedQuery;
import cecs429.queries.IRankedQuery;
import cecs429.queries.Okapi_BM25_RankedQuery;
import cecs429.queries.Query;
import cecs429.queries.Tf_IDF_RankedQuery;
import cecs429.queries.WackyRankedQuery;
import cecs429.queries.WildcardLiteral;
import cecs429.text.EnglishTokenStream;
import cecs429.text.IntermediateTokenProcessor;
import cecs429.text.TokenProcessor;
import cecs429.text.TokenStream;
import cecs429.weights.Accumulator;
import cecs429.weights.DefaultDocumentWeightCalculator;
import cecs429.weights.DocumentValuesModel;
import cecs429.weights.IDocumentWeightCalculator;
import cecs429.weights.*;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cecs429.text.BasicTokenProcessor;
import cecs429.text.Constants;

/**
 *
 * @author kabir
 */
public class Indexer {

	private DocumentCorpus corpus;
	private Index index;
	private Index diskIndex;
	TokenProcessor processor=new IntermediateTokenProcessor();
	private BiWordIndex biwordindex;
	BooleanQueryParser parser;
	KGramIndex kgramindex;
	IDocumentWeightCalculator calculator;
	private IRankedQuery rankedQuery;
	private Path path;
	private DiskKgramIndex diskKgram;
	private String extension;
	



	public Indexer(Path path,String extension)
	{
		this.path = path;
		this.extension = extension;
		parser= new BooleanQueryParser();
		biwordindex=new BiWordIndex();
		kgramindex=new KGramIndex();
		
		if(Constants.rankConfig == 0) {
			calculator = new DefaultDocumentWeightCalculator();
			rankedQuery = new DefaultRankedQuery();
		}
		else if(Constants.rankConfig == 1)
		{
			calculator = new TfIdfDocumentWeightCalculator();
			rankedQuery = new Tf_IDF_RankedQuery();
		}
		else if(Constants.rankConfig ==2)
		{
			calculator = new Okapi_BM25_DocumentWeightCalculator();
			rankedQuery = new Okapi_BM25_RankedQuery();
		}
		else if(Constants.rankConfig == 3)
		{
			calculator = new WackyDocumentWeightCalculator();
			rankedQuery = new WackyRankedQuery();
		}
		else
		{
			calculator = new DefaultDocumentWeightCalculator();
			rankedQuery = new DefaultRankedQuery();
		}
			
		
		
		
		
		if(extension.equals("json"))
		{
			corpus=DirectoryCorpus.loadJsonDirectory(path, ".json");
		}
		else if(extension.equals("txt"))
		{
			corpus=DirectoryCorpus.loadTextDirectory(path, ".txt");
		}
	
		
	}
	
	public Indexer(Path path, String extension, int overrideConstant)
	{
		this(path,extension);
		
		if(overrideConstant == 0) {
			calculator = new DefaultDocumentWeightCalculator();
			rankedQuery = new DefaultRankedQuery();
		}
		else if(overrideConstant  == 1)
		{
			calculator = new TfIdfDocumentWeightCalculator();
			rankedQuery = new Tf_IDF_RankedQuery();
		}
		else if(overrideConstant  ==2)
		{
			calculator = new Okapi_BM25_DocumentWeightCalculator();
			rankedQuery = new Okapi_BM25_RankedQuery();
		}
		else if(overrideConstant  == 3)
		{
			calculator = new WackyDocumentWeightCalculator();
			rankedQuery = new WackyRankedQuery();
		}
		else
		{
			calculator = new DefaultDocumentWeightCalculator();
			rankedQuery = new DefaultRankedQuery();
		}
			
		
	}
	public IDocumentWeightCalculator getCalculator() {return calculator;}
	public String getCorpusPath() {return path.getFileName().toString();}
	
	private List<Long> findByteSize(Path path, String extension)
	{
		List<Long> results = new ArrayList<>();
		File directory = path.toFile();
		File[] listing = directory.listFiles();
		if(listing == null)
			return results;
		String ext = "";
		for(File f : listing)
		{
			if(f.getPath().contains(extension))
			{
				results.add(f.length());
			}
		}
		
		return results;
	}


	public Index index()
	{
		Index pInvertedIndex=new PositionalInvertedIndex(corpus.getCorpusSize());
		HashSet<String> noDupes = new HashSet<>();
		Map<Integer,Map<String,Integer>> mapForCalculation = new HashMap<>();
		List<Integer> docLengths = new ArrayList<>();
		List<Long> docBytes = findByteSize(path,extension);
		

		int docNumber = 0;
		for(Document doc:corpus.getDocuments())
		{
			Map<String, Integer> termFrequency = new HashMap<>();
			int docLength = 0;
			int pos=0;
			
			TokenStream stream=new EnglishTokenStream(doc.getContent());
			String s1="";
			String s2="";
			int i=0;
			
			BasicTokenProcessor proc = new BasicTokenProcessor();
			for(String str : stream.getTokens())
			{
				
				docLength++;
				
				noDupes.addAll(processor.processToken(str));

				for(String s: processor.processToken(str))
				{
					if(i==0)
					{
						s1=s;
						i++;
					}
					else
					{
						s2=s;
						biwordindex.addTerm(s1+" "+s2, doc.getId());
						s1=s2;
					}
					if(!s.isEmpty() && !s.isBlank())
						pInvertedIndex.addTerm(s,doc.getId(),pos++);
					

					// If the termFrequency map does not contain key, put it in there. Else, add one to the counter of the term
					if(!termFrequency.containsKey(s) && !s.isEmpty())
					{
						termFrequency.put(s, 1);
					}
					else if(!s.isEmpty())
					{
						termFrequency.computeIfPresent(s, (key,val) -> val+=1);
					}

				}
			}

			mapForCalculation.put(docNumber, termFrequency);
			docLengths.add(docLength);
			docNumber++;
			
		}
		
		
		for(String s: noDupes)
		{
			kgramindex.addTerm(s, 0, 0);
			
		}
		
	

		
		DocumentValuesModel model = new DocumentValuesModel();
		model.setDocLengths(docLengths);
		model.setByteSizes(docBytes);
		model.setMap(mapForCalculation);
		
		model.setDocAverageTFDs(
				calculateAverageTFDs(mapForCalculation, docLengths));
		model.setDocWeights(calculator.calculate(model));

		
		pInvertedIndex.setDocumentValuesModel(model);
		pInvertedIndex.setIndex(kgramindex);
		biwordindex.setIndex(kgramindex);
		index = pInvertedIndex;
		return pInvertedIndex; 
	}
	public DocumentCorpus getCorpus()
	{
		return corpus;
	}
	
	
	public Index getIndex() {return index;}
   // public KGramIndex getKgramIndex(){return kgramindex;}

	public List<String> getVocab1000()
	{
		List<String> thousandTerms = new ArrayList<String>();
		List<String> allTerms = getVocabulary();

		for(int i = 0; i < 999; i++)
		{
			thousandTerms.add(allTerms.get(i));
		}

		return thousandTerms;
	}
	public List<String> getVocabulary()
	{
		return index.getVocabulary();
	}
	
	//Must break down query into simple tokens and send as list to the IRankedQuery.query call
	//Returns the document title and the accumulator value
	public List<String> rankedQuery(String query)
	{
		
		try 
		{
			rankedQuery.setPath(path.toString()+"/docWeights.bin");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		List<Accumulator> queryResults;
		List<String> methodResults = new ArrayList<>();
		TokenProcessor proc = new IntermediateTokenProcessor();
		String[] queryTerms = query.split(" ");
		
		List<String> formattedTerms = new ArrayList<>();
		
		for(String s : queryTerms)
		{
			if(s.length() == 1 && s.indexOf('*')>= 0)
				formattedTerms.add(s);
			else
				formattedTerms.addAll(proc.processToken(s));
	
		}
		
		try {
			List<String> temps = new ArrayList<>();
			for(int i = 0; i <  formattedTerms.size(); i++)
			{
				
				if(formattedTerms.get(i).length() > 1 && formattedTerms.get(i).indexOf('*') >= 0)
				{
					WildcardLiteral lit = new WildcardLiteral(formattedTerms.get(i));
					formattedTerms.remove(formattedTerms.get(i));
					i--;
					temps.addAll(lit.getKGrams(diskKgram));
				}
				else if(formattedTerms.get(i).length() == 1 && formattedTerms.get(i).indexOf('*') == 0)
				{
					formattedTerms.addAll(diskIndex.getVocabulary());
					formattedTerms.remove(formattedTerms.get(i));
				}
			}
			
			formattedTerms.addAll(temps);
			
			queryResults = rankedQuery.query(formattedTerms, diskIndex);
			
			if(queryResults.size() != 0) {
				for(Accumulator acc : queryResults)
				{
					if(acc == null)
						continue;
					StringBuilder s = new StringBuilder();
					s.append("Title: ");
					
					s.append(corpus.getDocument(acc.getDocId()).getTitle());
					s.append(" | Accumulator Value : ");
					s.append(acc.getaValue());
					
					methodResults.add(s.toString());
				}
				
			}
			return methodResults;
		}catch(Exception e)
		{
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	public List<Posting> query(String query)
	{                          
		List<Posting> p;
		Query q = parser.parseQuery(query);

		if(q.getnegative() && q.getClass() != WildcardLiteral.class) 
			return notmerge(diskIndex.getPostings(), q.getPostings(diskIndex, new IntermediateTokenProcessor()));
		else p= q.getPostings(diskIndex,new IntermediateTokenProcessor());


		return p;               
	}

	private List<Posting> notmerge(List<Posting> list1, List<Posting> list2) {
		List<Posting> result=new ArrayList<>();
		int i=0;
		int j=0;
		while(i<list1.size() && j<list2.size())
		{
			if(list1.get(i).getDocumentId()==list2.get(j).getDocumentId())
			{
				i++;
				j++;
			}
			else if(list1.get(i).getDocumentId()<list2.get(j).getDocumentId())
			{
				result.add(list1.get(i));
				i++;
			}
			else if(list1.get(i).getDocumentId()>list2.get(j).getDocumentId())
			{
				j++;
			}
		}
		while(i<list1.size())
		{
			result.add(list1.get(i++));
		}
		return result;
	}

	public void delIndex() 
	{
		index = null;
	}
	
	public void delKgram()
	{
		kgramindex=null;
	}

	
	private List<Double> calculateAverageTFDs(Map<Integer,Map<String,Integer>> map, List<Integer> lengths)
	{
		List<Double> averageTFDResults = new ArrayList<>();
		int i = 0;
		for(Map<String,Integer> interiorMaps : map.values())
		{
			Double e = 0.0;
			for(Integer value : interiorMaps.values())
			{
				e+=value;
			}
			
			e/= lengths.get(i);
			averageTFDResults.add(e);
			i++;
		}
		
		return averageTFDResults;
	}
	
	public void setDiskKgram(DiskKgramIndex index)
	{
		diskKgram = index;
	}
	
	public void setDiskIndex(Index index)
	{
		diskIndex = index;
	}
	public KGramIndex getKgramIndex() {
        	return kgramindex;
	}
}
