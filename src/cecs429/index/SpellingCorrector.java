/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author kabir
 */
public class SpellingCorrector {
    DiskKgramIndex index;
    static DiskInvertedIndex di;
    public SpellingCorrector(DiskKgramIndex index,DiskInvertedIndex di)
    {
        this.index=index;
        this.di=di;
    }
    public List<String> checkFor(String term) throws IOException
    {
        
        List<String> grams=makegrams("$"+term+"$");
        
        Set<String> union=new HashSet();
        for(String s : grams)
        {
            for(String posting:index.getPostings(s))
            {
                union.add(posting);
            }
        }
        List<String> candidates=new ArrayList();
        for(String s: union)
        {
            if(calculateJacard(s,grams)>0.1)candidates.add(s);
        }
        List<String> suggestions=new ArrayList();
        PriorityQueue<Pair> pq=new PriorityQueue(new Comp());
        for(String s:candidates)
        {
            if(pq.size()<11)
            {
                pq.add(new Pair(s,editdistance(term,s)));
            }
            else
            {
                Pair p=new Pair(s,editdistance(term,s));
                if(pq.peek().getDistance()>p.getDistance())
                {
                    pq.poll();
                    pq.add(p);
                }
                else if(pq.peek().getDistance()==p.getDistance())
                {
                    if(di.getPostings(pq.peek().getTerm()).size()<di.getPostings(p.getTerm()).size())
                    {
                        pq.poll();
                        pq.add(p);
                    }
                }
            }
        }
        while(!pq.isEmpty())
        {
            String s=pq.poll().getTerm();
            suggestions.add(s.substring(1,s.length()-1));
        }
        Collections.reverse(suggestions);
        System.out.print(suggestions);
        return suggestions;
    }
    
    public double calculateJacard(String s,List<String> original)
    {
       Set<String> candidategrams=new HashSet(makegrams("$"+s+"$"));
       //System.out.println(String.valueOf(candidategrams));
       Set<String> originalgrams=new HashSet(original);
       //System.out.println(String.valueOf(originalgrams));
       double temp=candidategrams.size()+originalgrams.size();
       originalgrams.retainAll(candidategrams);
       double jacard=originalgrams.size()/(temp-originalgrams.size());
       return jacard;
    }
    public List<String> makegrams(String term)
    {
        
        List<String> answer=new ArrayList();
        
            for(int j=0;j<term.length()-(3-1);j++)
            {
    		if(term.substring(j,j+3).equals("$"))
    			continue;
                else
                    answer.add(term.substring(j,j+3));	
    	
        
        }
    return answer;
  
    }

    private int editdistance(String term, String s) {
        int[][] dp=new int[term.length()+1][s.length()+1];
        for(int i=0;i<term.length()+1;i++)
        {
            for(int j=0;j<s.length()+1;j++)
            {
                if(i==0)dp[i][j]=j;
                else if(j==0)
                {
                    dp[i][j]=i;
                }
                else if (term.charAt(i - 1) == s.charAt(j - 1)) 
                {
                    dp[i][j] = dp[i - 1][j - 1];
                } 
                else
                    
                {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i][j - 1],dp[i - 1][j]),dp[i - 1][j - 1]); 
                }
            }
        }
        return dp[term.length()][s.length()];
    }
    

    private static class Comp implements Comparator<Pair> {

        @Override
        public int compare(Pair o1, Pair o2) {
            if(o1.getDistance()<o2.getDistance())return 1;
            else if(o1.getDistance()>o2.getDistance())return -1;
            else 
            {
                if(di.getPostings(o1.getTerm()).size()>di.getPostings(o2.getTerm()).size())
                {
                    return 1;
                }
                else return -1;
            }
            
        }
    }
    public class Pair
    {
        private String term;
        private int distance;
        public Pair(String term,int distance)
        {
            this.term=term;
            this.distance=distance;
        }
        public String getTerm()
        {
            return term;
        }
        public int getDistance()
        {
            return distance;
        }
    }
}
