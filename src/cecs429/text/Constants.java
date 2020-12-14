package cecs429.text;

public class Constants {

	private Constants()
	{ }

	public static final int DOCS_RETURNED = 10;
	/*
	 * 0 = default
	 * 1 = tf -idf
	 * 2 = okapi bm25
	 * 3 = wacky
	 */
	public static int rankConfig = 1;
	public static String currentPath = "C:\\Users\\Daniel\\Desktop\\CECS 429\\CECS429Milestone1\\src\\corpus\\index";
	public static String madisonPath ="C:\\Users\\Daniel\\Desktop\\CECS 429\\FedPapers\\MADISON";
	public static String hamiltonPath="C:\\Users\\Daniel\\Desktop\\CECS 429\\FedPapers\\HAMILTON";
	public static String jayPath ="C:\\Users\\Daniel\\Desktop\\CECS 429\\FedPapers\\JAY";
	public static String disputedPath = "C:\\Users\\Daniel\\Desktop\\CECS 429\\FedPapers\\DISPUTED";
}
