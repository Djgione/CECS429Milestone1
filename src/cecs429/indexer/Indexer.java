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
import cecs429.index.Index;
import cecs429.index.KGramIndex;
import cecs429.index.PositionalInvertedIndex;
import cecs429.index.Posting;
import cecs429.queries.BiWordQuery;
import cecs429.queries.BooleanQueryParser;
import cecs429.queries.DefaultRankedQuery;
import cecs429.queries.IRankedQuery;
import cecs429.queries.Query;
import cecs429.queries.WildcardLiteral;
import cecs429.text.BasicTokenProcessor;
import cecs429.text.EnglishTokenStream;
import cecs429.text.IntermediateTokenProcessor;
import cecs429.text.TokenProcessor;
import cecs429.text.TokenStream;
import cecs429.weights.Accumulator;
import cecs429.weights.DefaultDocumentWeightCalculator;
import cecs429.weights.DocumentValuesModel;
import cecs429.weights.IDocumentWeightCalculator;
import cecs429.weights.Tf_Idf_DocumentWeightCalculator;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
	
	



	public Indexer(Path path,String extension)
	{
		parser= new BooleanQueryParser();
		biwordindex=new BiWordIndex();
		kgramindex=new KGramIndex();
		calculator = new DefaultDocumentWeightCalculator();
		rankedQuery = new DefaultRankedQuery();
		
		if(extension.equals("json"))
		{
			corpus=DirectoryCorpus.loadJsonDirectory(path, ".json");
		}
		else if(extension.equals("txt"))
		{
			corpus=DirectoryCorpus.loadTextDirectory(path, ".txt");
		}
		index=index(corpus, path, extension);
		
	}
	
	
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
//		
//		for(Long l : results)
//			System.out.println(l);
		return results;
	}


	private Index index(DocumentCorpus corpus, Path path, String extension)
	{
		Index pInvertedIndex=new PositionalInvertedIndex(corpus.getCorpusSize());
		HashSet<String> noDupes = new HashSet<>();
		Map<Integer,Map<String,Integer>> mapForCalculation = new HashMap<>();
		List<Integer> docLengths = new ArrayList<>();
		List<Long> docBytes = findByteSize(path,extension);
		

		int docNumber = 1;
		for(Document doc:corpus.getDocuments())
		{

			

			Map<String, Integer> termFrequency = new HashMap<>();
			
			int docLength = 0;
			int pos=0;
			
			TokenStream stream=new EnglishTokenStream(doc.getContent());
			String s1="";
			String s2="";
			int i=0;
			
			for(String str : stream.getTokens())
			{
				
				docLength++;
				noDupes.add(str.toLowerCase());
				//System.out.print(str);
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
					//System.out.println(s + "docid=" + doc.getId());
					pInvertedIndex.addTerm(s,doc.getId(),pos++);
					//kgramindex.addTerm(s,0,0);

					// If the termFrequency map does not contain key, put it in there. Else, add one to the counter of the term
					if(!termFrequency.containsKey(s))
					{
						termFrequency.put(s, 1);
					}
					else
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
		
		System.out.println(mapForCalculation.size());
		// Sets the document weights into the inverted Index using the calculator from the constructor
		//calculator.setDocumentTermFrequencies(mapForCalculation);
		
		DocumentValuesModel model = new DocumentValuesModel();
		model.setDocLengths(docLengths);
		model.setByteSizes(docBytes);
		model.setMap(mapForCalculation);
		
		model.setDocAverageTFDs(
				calculateAverageTFDs(mapForCalculation, docLengths));
		//List<Double> docWeights = calculator.calculate(model);
		model.setDocWeights(calculator.calculate(model));
		
		
		
		
		pInvertedIndex.setDocumentValuesModel(model);
		pInvertedIndex.setIndex(kgramindex);
		biwordindex.setIndex(kgramindex);
		//pInvertedIndex.print();
		return pInvertedIndex; 
	}
	public DocumentCorpus getCorpus()
	{
		return corpus;
	}
	//Omar added this
	public Index getIndex() {return index;}

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
		List<Accumulator> queryResults;
		List<String> methodResults = new ArrayList<>();
		TokenProcessor proc = new IntermediateTokenProcessor();
		String[] queryTerms = query.split(" ");
		
		List<String> formattedTerms = new ArrayList<>();
		
		for(String s : queryTerms)
		{
			formattedTerms.add(proc.processToken(s).get(0));
		}
		
		queryResults = rankedQuery.query(formattedTerms, diskIndex);
		
		for(Accumulator acc : queryResults)
		{
			StringBuilder s = new StringBuilder();
			s.append("Title: ");
			s.append(corpus.getDocument(acc.getDocId()).getTitle());
			s.append(" | Accumulator Value : ");
			s.append(acc.getaValue());
			
			methodResults.add(s.toString());
		}
		
		
		return methodResults;
	}

	public List<Posting> query(String query)
	{                          
		List<Posting> p;
		Query q = parser.parseQuery(query);
		if(q.isBiWord())
			p=q.getPosting(biwordindex);
		else if(q.getnegative() && q.getClass() != WildcardLiteral.class) 
			return notmerge(index.getPostings(), q.getPostings(index, new IntermediateTokenProcessor()));
		else p= q.getPostings(index,new IntermediateTokenProcessor());


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
}
