package cecs429.classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import cecs429.documents.Document;
import cecs429.index.Index;
import cecs429.text.EnglishTokenStream;
import cecs429.text.IntermediateTokenProcessor;
public class Bayes {
	Index Hamilton,Jay, Madison;
	ArrayList<String> vocab;
	
	public Bayes(Index Hamilton,Index Jay,Index Madison)
	{
		this.Hamilton=Hamilton;
		this.Jay=Jay;
		this.Madison=Madison;
		vocab=T(10);
	}
	//use only the terms in T*
	//
	public double classify(Document d,Index index)
	{
		//System.out.println(vocab);
		double cmap=0.0;
		double indexprob=((double)index.getDocCount()/(double)71);
		EnglishTokenStream ts=new EnglishTokenStream(d.getContent());
		int corpfreq=countCorpFreq(index);
		System.out.println("indexprob"+indexprob);
		//System.out.println("hamiltonprob "+hamiltonprob);
		double temp=0.0;
		for(String str : ts.getTokens())
		{
			
			IntermediateTokenProcessor processor=new IntermediateTokenProcessor();
			
				
					for(String s : processor.processToken(str))
					{
						if(vocab.contains(s))
						{
							double ftc=(double)index.getTf(s);
							//System.out.print("ftc"+ftc);
							temp= temp + Math.log((ftc+1.0)/((double)corpfreq+(double)vocab.size()));
						}
					}
					
				
		}
		//System.out.println("temp="+temp);
		//cmaphamilton=(log2(hamiltonprob)/log2(2))+temp;
		cmap = Math.log(indexprob) + temp;
		
		return cmap;
	}
	public double log2(double N) 
    { 
  
        
        double result = (double)(Math.log(N) / Math.log(2)); 
  
        return result; 
    } 
	private int countCorpFreq(Index index)
	{
		int result=0;
		for(String str : vocab)
		{
			result += index.getTf(str);
		}
		return result;
	}
	
	public ArrayList<String> T(int size)
	{
		Set<Integer> totaldocs= new HashSet<Integer>();
		totaldocs.addAll(Hamilton.getAllDocs());
		totaldocs.addAll(Jay.getAllDocs());
		totaldocs.addAll(Madison.getAllDocs());
		PriorityQueue<Pair> pq=new PriorityQueue(new Comp());
		int totalVocab=getTotalVocab();
		System.out.println("Jay docs"+Jay.getAllDocs());
		System.out.println("Hamilton docs"+Hamilton.getAllDocs());
		
		for(String s:Hamilton.getVocabulary())
		{
			//documents that arent in the catagory and does not contain the term
			
			HashSet<Integer> set1=new HashSet(Jay.getAllDocs());
			
			set1.removeAll(Jay.getDocIds(s));
			HashSet<Integer> set2=new HashSet(Madison.getAllDocs());
			set2.removeAll(Madison.getDocIds(s));
			double n00=(double)set1.size()+set2.size();
			//documents that are in the catagory and do not contain the term
			set1=new HashSet(Hamilton.getAllDocs());
			set1.removeAll(Hamilton.getDocIds(s));
		//	System.out.println("n01="+set1);
			double n01=(double)set1.size();
			
			//documents that are not in the catagory and do contain the term
			set1=new HashSet(Jay.getAllDocs());
			set1.retainAll(Jay.getDocIds(s));
			set2=new HashSet(Madison.getAllDocs());
			set2.retainAll(Madison.getDocIds(s));
			double n10=(double)set1.size()+(double)set2.size();
			//documents that are in the catagory and do contain the term
			double n11=(double)Hamilton.getDocFreq(s);
			
			double n=n00+n01+n11+n10;
			//=72
			double  mutualinfo=0.0;
			//System.out.println(s+" n00"+n00+"n01 "+n01+" n11"+n11+" n10"+n10+"="+ n);
			
//			if(n11==0||n10==0||n01==0||n00==0)
//			{
//				mutualinfo=0.0;
//			}
//			else
//			{
			
			if((n10+n11)==0 || (n01+n11)==0 ||(n00+n10)==0 || (n00+n01)==0 || n11==0 || n00==0 || n01 ==0 || n10==0)
			{
				//System.out.println("abcd:"+a+" "+b+" "+c+" "+d);
			}
				
			
			else
			{
				double a=((n*n11))/((n10+n11)*(n01+n11));
				double b=((n*n10))/((n10+n11)*(n00+n10));
				double c=(n*n01)/((n00+n01)*(n11+n01));
				double d=((n*n00))/((n00+n01)*(n00+n10));
				mutualinfo = ((n11/n) * log2(a)) + ((n10/n) * log2(b))
						+((n01/n) * log2(c)) +((n00/n) * log2(d));
				
			}
				//System.out.println(a+" "+b+" "+c+" "+d);
				
			//}
			//System.out.println(s+" "+mutualinfo);
			if(pq.size()<size)
			{
				pq.add(new Pair(s,mutualinfo));
			}
			else if(pq.peek().value<mutualinfo)
			{
				pq.poll();
				pq.add(new Pair(s,mutualinfo));
			}
			
		}
		for(String s:Jay.getVocabulary())
		{
			HashSet<Integer> set1=new HashSet(Hamilton.getAllDocs());
			set1.removeAll(Hamilton.getDocIds(s));
			HashSet<Integer> set2=new HashSet(Madison.getAllDocs());
			set2.removeAll(Madison.getDocIds(s));
			double n00=(double)set1.size()+(double)set2.size();
			//documents that are in the catagory and do not contain the term
			set1=new HashSet(Jay.getAllDocs());
			set1.removeAll(Jay.getDocIds(s));
			double n01=(double)set1.size();
			
			//documents that are not in the catagory and do contain the term
			set1=new HashSet(Hamilton.getAllDocs());
			set1.retainAll(Hamilton.getDocIds(s));
			set2=new HashSet(Madison.getAllDocs());
			set2.retainAll(Madison.getDocIds(s));
			double n10=(double)set1.size()+(double)set2.size();
			//documents that are in the catagory and do contain the term
			double n11=(double)Jay.getDocFreq(s);
			double n=n00+n01+n11+n10;

			double mutualinfo=0.0;
			
			
			if((n10+n11)==0 || (n01+n11)==0 ||(n00+n10)==0 || (n00+n01)==0 || n11==0 || n00==0 || n01 ==0 || n10==0)
			{
				//System.out.println("abcd:"+a+" "+b+" "+c+" "+d);
			}
				
			
			else
			{
				double a=((n*n11))/((n10+n11)*(n01+n11));
				double b=((n*n10))/((n10+n11)*(n00+n10));
				double c=(n*n01)/((n00+n01)*(n11+n01));
				double d=((n*n00))/((n00+n01)*(n00+n10));
				mutualinfo = ((n11/n) * log2(a)) + ((n10/n) * log2(b))
						+((n01/n) * log2(c)) +((n00/n) * log2(d));
				
			}
			//System.out.println(s+" "+mutualinfo);
			if(pq.size()<size)
			{
				pq.add(new Pair(s,mutualinfo));
			}
			else if(pq.peek().value<mutualinfo)
			{
				pq.poll();
				pq.add(new Pair(s,mutualinfo));
			}
		}
		for(String s:Madison.getVocabulary())
		{
			HashSet<Integer> set1=new HashSet(Hamilton.getAllDocs());
			set1.removeAll(Hamilton.getDocIds(s));
			HashSet<Integer> set2=new HashSet(Jay.getAllDocs());
			set2.removeAll(Jay.getDocIds(s));
			double n00=(double)set1.size()+(double)set2.size();
			//documents that are in the catagory and do not contain the term
			set1=new HashSet(Madison.getAllDocs());
			set1.removeAll(Madison.getDocIds(s));
			double n01=(double)set1.size();
			
			//documents that are not in the catagory and do contain the term
			set1=new HashSet(Hamilton.getAllDocs());
			set1.retainAll(Hamilton.getDocIds(s));
			set2=new HashSet(Jay.getAllDocs());
			set2.retainAll(Jay.getDocIds(s));
			double n10=(double)set1.size()+(double)set2.size();
			//documents that are in the catagory and do contain the term
			double n11=(double)Madison.getDocFreq(s);
			//System.out.println(n00+" "+n11+" "+n10+" "+n01);
			double n=n00+n01+n11+n10;
			double mutualinfo=0.0;
			
			if((n10+n11)==0 || (n01+n11)==0 ||(n00+n10)==0 || (n00+n01)==0 || n11==0 || n00==0 || n01 ==0 || n10==0)
			{
				//System.out.println("abcd:"+a+" "+b+" "+c+" "+d);
			}
				
			
			else
			{
				double a=((n*n11))/((n10+n11)*(n01+n11));
				double b=((n*n10))/((n10+n11)*(n00+n10));
				double c=(n*n01)/((n00+n01)*(n11+n01));
				double d=((n*n00))/((n00+n01)*(n00+n10));
				mutualinfo = ((n11/n) * log2(a)) + ((n10/n) * log2(b))
						+((n01/n) * log2(c)) +((n00/n) * log2(d));
				
			}
			//System.out.println(s+" "+mutualinfo);
			if(pq.size()<size)
			{
				pq.add(new Pair(s,mutualinfo));
			}
			else if(pq.peek().value<mutualinfo)
			{
				pq.poll();
				pq.add(new Pair(s,mutualinfo));
			}
			
		}
		ArrayList<String> temp=new ArrayList();
		int i=1;
		while(!pq.isEmpty())
		{
			Pair p=pq.poll();
			temp.add(p.key);
			System.out.println(i++ +" "+p.key+" "+p.value);
		}
		System.out.println(temp.size());
		return temp;
		
	}
	
	private int getTotalVocab() {
		
		HashSet<String> temp=new HashSet(Hamilton.getVocabulary());
		temp.addAll(Jay.getVocabulary());
		temp.addAll(Madison.getVocabulary());
		return temp.size();
		
		
	}
	public class Comp implements Comparator<Pair>
	{
		public int compare(Pair p1,Pair p2)
		{
			if(p1.value<p2.value)return -1;
			else return 1;
		}
	}
	public class Pair
	{
		String key;
		double value;
		public Pair(String key,double mutualinfo)
		{
		this.key=key;
		this.value=mutualinfo;
		}
		
	}

}
