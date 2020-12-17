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
	public static String currentPath = "C:\\Users\\Kermite\\Downloads\\src_2\\src\\corpus";
	public static String madisonPath =currentPath+"\\MADISON";
	public static String hamiltonPath=currentPath+"\\HAMILTON";
	public static String jayPath =currentPath+"\\JAY";
	public static String disputedPath =currentPath+"\\DISPUTED";
	public static String allPath = currentPath+"\\ALLPAPERS";

}
