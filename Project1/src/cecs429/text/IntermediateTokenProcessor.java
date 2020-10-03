package cecs429.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
<<<<<<< HEAD
=======
import java.util.regex.*;
>>>>>>> master

import org.tartarus.snowball.SnowballStemmer;

public class IntermediateTokenProcessor implements TokenProcessor {

	//private final Pattern mQuotes = Pattern.compile("["']");
	//private final Pattern mHyphen = Pattern.compile("[-]");
	@Override
	public List<String> processToken(String token){
		// TODO Auto-generated method stub
		List<String> temp = new ArrayList<>();
		
		// Removes all non-alphanumeric from front of string
		while(token.length()>0 && !Character.isLetterOrDigit(token.charAt(0)))

		{
			token = token.substring(1);
		}
		
		// Removes all non-alphanumeric from back of string
		while(!Character.isLetterOrDigit(token.charAt(token.length()-1)))
		{
			token = token.substring(0,token.length()-1);
		}

		token = token.replaceAll("[^a-zA-Z0-9_]","");
		
	
		// Set to lower case
		token = token.toLowerCase();
		
		// Replace all hyphens with no space and make extra string out of it
		
		//Wont take as list and let u add on additional
		List<String> splitWords = new ArrayList<>(Arrays.asList(token.split("-")));
		if(splitWords.size()>1) {
			token = token.replaceAll("-", "");
			splitWords.add(token);
		}
		try 
		{
			Class<?> stemClass = Class.forName("org.tartarus.snowball.ext." + "english" + "Stemmer");
			SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();

			for(int i = 0; i < splitWords.size(); i++)
			{
				stemmer.setCurrent(splitWords.get(i));
				stemmer.stem();
				temp.add(stemmer.getCurrent());
			}
			
		}
		catch(Exception e)
		{
			
		}
		return temp;
	}

	
}
