/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.index;

import cecs429.text.IntermediateTokenProcessor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author kabir
 */
public class CheckForSpellings {
	String[] tokens;
	IntermediateTokenProcessor itp;
	DiskInvertedIndex di;
	DiskKgramIndex dki;
	public CheckForSpellings (DiskInvertedIndex di,DiskKgramIndex dki)
	{

		itp =new IntermediateTokenProcessor();
		this.di=di;
		this.dki=dki;

	}
	public String suggest(String s) throws IOException
	{
		tokens=s.split(" ");

		for(int i=0;i<tokens.length;i++)
		{


			if(dki.getPostings(tokens[i]).size()<15)
			{


				SpellingCorrector sp=new SpellingCorrector(dki,di);
				if(sp.checkFor(tokens[i]).size()>0)
				{
					tokens[i]=sp.checkFor(tokens[i]).get(0);
				}
			}
		}
		String answer="";
		for(String str:tokens)
		{
			answer=answer+str;

			if(!str.equals(tokens[tokens.length - 1]))
				answer += " ";
		}
		return answer;
	}

}
