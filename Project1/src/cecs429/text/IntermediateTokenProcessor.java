package cecs429.text;

import java.util.ArrayList;
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
		mQuotes.matcher(token).replaceAll("");
		// Set to lower case
		token = token.toLowerCase();
		// Replace all hyphens with no space and make extra string out of it
		String noHyphen = mHyphen.matcher(token).replaceAll("");
		
		
		return null;
	}

	
}
