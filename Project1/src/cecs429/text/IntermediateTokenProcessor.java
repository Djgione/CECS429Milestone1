package cecs429.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class IntermediateTokenProcessor implements TokenProcessor {

	private final Pattern mQuotes = Pattern.compile("[\'\"]");
	private final Pattern mHyphen = Pattern.compile("[-]");
	@Override
	public List<String> processToken(String token) {
		// TODO Auto-generated method stub
		List<String> temp = new ArrayList<>();
		
		// Removes all nonalphanumeric from front of string
		while(!Character.isLetterOrDigit(token.charAt(0)))
		{
			token = token.substring(1);
		}
		// Removes all nonalphanumeric from back of string
		while(!Character.isLetterOrDigit(token.charAt(token.length()-1)))
		{
			token = token.substring(0,token.length()-1);
		}
		// Replace all quotations with no space
		token=token.replaceAll("'", "");
                token=token.replaceAll("\"","");
                ArrayList<String> hyphenprocessed=new ArrayList();
                hyphenprocessed.add(token.replaceAll("-",""));
                List<String> list=Arrays.asList(token.split("-"));
                if(list.size()>1)hyphenprocessed.addAll(list);
               

                
//                List<> arr=token.split("-");
//                if(arr.length>1)temp.addAll();
               /// mQuotes.matcher(token).replaceAll("");
		// Set to lower case
		for(String s:hyphenprocessed)
                {
                    s=s.toLowerCase();
                }
                return hyphenprocessed;
		// Replace all hyphens with no space and make extra string out of it
		//String noHyphen = mHyphen.matcher(token).replaceAll("");
		
		
		return null;
	}

	
}
