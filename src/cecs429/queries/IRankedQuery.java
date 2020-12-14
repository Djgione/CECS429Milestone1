package cecs429.queries;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.PriorityQueue;
import cecs429.index.Posting;
import cecs429.index.Index;
import cecs429.weights.Accumulator;

public abstract class IRankedQuery {

	protected RandomAccessFile weights;
	protected int TOTAL_LENGTH_DOC_VARIABLES = (Double.BYTES*2) + Integer.BYTES + Long.BYTES;
	protected int docAmount;
	
	public IRankedQuery()
	{
		
	}
	public IRankedQuery(String path) throws FileNotFoundException
	{
		weights = new RandomAccessFile(path, "r");
		getDocAmounts();
	}
	
	public void setPath(String path) throws FileNotFoundException
	{
		weights = new RandomAccessFile(path, "r");
		getDocAmounts();
	}
	/**
	 * Returns a list of accumulators and their doc id's
	 * @param terms
	 * @return
	 */
	public abstract List<Accumulator> query(List<String> terms, Index index);
	
	private void getDocAmounts()
	{
		int i = 0;
		try 
		{
			while(weights.getFilePointer() != (weights.length() - Integer.BYTES))
			{
				weights.skipBytes(TOTAL_LENGTH_DOC_VARIABLES);
				i++;
			}
		} catch(Exception e)
		{
			e.printStackTrace();
			docAmount = -1;
		}
		
		docAmount = i;
	}
	
	private int skipLength(int docId)
	{
		int skipLength = 0;
		for(int i = 0; i < docId; i++)
		{
			skipLength+= TOTAL_LENGTH_DOC_VARIABLES;
		}
		
		return skipLength;
	}
	
	protected double readWeight(int docId)
	{
		int skipLength = skipLength(docId);
		
		try 
		{
			weights.seek(skipLength);
			return weights.readDouble();
		} catch(IOException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	protected double readAveTFD(int docId)
	{
		int skipLength = skipLength(docId);
		
		try	
		{
			weights.seek(skipLength + Double.BYTES);
			return weights.readDouble();
			
		} catch(IOException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	
	
	protected int readLength(int docId)
	{
		int skipLength = skipLength(docId);
		
		try 
		{
			weights.seek(skipLength + (Double.BYTES * 2));
			return weights.readInt();
			
		} catch(IOException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	protected long readByteSize(int docId)
	{
		int skipLength = skipLength(docId);
		
		try 
		{	
			weights.seek(skipLength + (Double.BYTES * 2) + Integer.BYTES);
			return weights.readLong();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} 

	}
	 
	protected int readTotalLength()
	{
		try 
		{
			long length = weights.length();
			length -= Integer.BYTES;
			
			weights.seek(length);
			return weights.readInt();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}
	
}
