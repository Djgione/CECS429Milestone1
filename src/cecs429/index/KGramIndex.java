/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author kabir
 */
public class KGramIndex {
    HashMap<String,List<String>> kGramIndex;
    public KGramIndex()
    {
       kGramIndex=new HashMap<>();
    }
    
    /**
     * Takes a string and the length of desired kgram, breaks the string down and adds it to the hashmap
     * The string stored will begin and end with a $ for further processing by wildcardLiteral
     * @param str
     * @param len
     */
    private void add(String str,int len)
    {
    	for(int i=0;i<str.length()-(len-1);i++)
    	{
    		if(str.substring(i,i+len).equals("$"))
    			continue;
    		if(!kGramIndex.containsKey(str.substring(i,i+len)))
    			kGramIndex.put(str.substring(i,i+len), new ArrayList<String>());
    		kGramIndex.get(str.substring(i,i+len)).add(str);

    	}
    }
    public void print()
    {
        for(String s:kGramIndex.keySet())
        {
            System.out.print(s+"->");
            for(String str: kGramIndex.get(s))
            {
                System.out.print(str+"    ");
            }
            System.out.println();
        }
        System.out.print("OVER");
    }


    public List<String> getPostings(String term)
    {
    	
    	if(kGramIndex.get(term)==null)
    		return new ArrayList<String>();
    	HashSet<String> terms = new HashSet<>(kGramIndex.get(term));
    	return new ArrayList<String>(terms);
    }

    public void addTerm(String str, int id, int i) {
		str="$"+str+"$";
        add(str,1);
        add(str,2);
        add(str,3);
		
	}
    public List<String> getVocab()
    {
       return List.copyOf(kGramIndex.keySet());
       
    }
       
   
    
}