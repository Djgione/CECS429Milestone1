/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author kabir
 */
public class KGramIndex {
    HashMap<String,List<String>> kgramindex;
    public KGramIndex()
    {
       kgramindex=new HashMap<>();
    }
    public void addGram(String str)
    {
        str="$"+str+"$";
        add(str,1);
        add(str,2);
        add(str,3);
        
    }
    private void add(String str,int len)
    {
        for(int i=0;i+len<str.length();i=i+len)
        {
            if(str.substring(i,i+len).equals("$"))continue;
            if(!kgramindex.containsKey(str.substring(i,i+len)))kgramindex.put(str.substring(i,i+len), new ArrayList<String>());
            kgramindex.get(str.substring(i,i+len)).add(str.substring(1,str.length()-1));

        }
    }
    public void print()
    {
        for(String s:kgramindex.keySet())
        {
            System.out.print(s+"->");
            for(String str: kgramindex.get(s))
            {
                System.out.print(str+"    ");
            }
            System.out.println();
        }
    }
    
    
    public List<String> getPostings(String term)
    {
    	return kgramindex.get(term);
    }
    
    
}
