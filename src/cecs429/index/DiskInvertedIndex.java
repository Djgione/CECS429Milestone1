/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.index;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

/**
 *
 * @author kabir
 */
public class DiskInvertedIndex implements Index{

    private DB db; 
    private BTreeMap <String,Long> map;
    private TreeMap<Integer, Double> documentWeights;
    RandomAccessFile file;
    RandomAccessFile weightsFile;
    
    public DiskInvertedIndex(String path) throws FileNotFoundException, IOException
    {
        db = DBMaker.fileDB(path+"/theDB").make();
        map = db.treeMap("map").keySerializer(Serializer.STRING)
                               .valueSerializer(Serializer.LONG)
                               .createOrOpen();
        file=new RandomAccessFile(path+"/index/postings.bin","r");       
        weightsFile = new RandomAccessFile(path+"/index/docWeights.bin","r");
    }

    /**
     *
     * @param term
     * @return
     * @throws Exception
     */
    @Override
    public List<Posting> getPostings(String term){
        List<Posting> answer=new ArrayList();
        
        try {
            file.seek(map.get(term));
            int dft=file.readInt();
            int docId=0;
            for(int i=0;i<dft;i++)
            {
                docId = file.readInt()+docId;
                int tftd = file.readInt();
                List<Integer> positions=new ArrayList();
                int gap=0;
                for(int j=0;j<tftd ;j++)
                {
                    gap=gap+file.readInt();
                    positions.add(gap);
                }
                answer.add(new Posting(docId,positions)) ;
        }} catch (IOException ex) {
            Logger.getLogger(DiskInvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
        }           
        return answer;
    }

    @Override
    public List<Posting> getPostings() {
        List<Posting> answer=new ArrayList();
        try 
        {
        	for(int count = 0; count < getVocabulary().size(); count++)
        	{
        		int dft=file.readInt();
                int docId=0;
                for(int i=0;i<dft;i++)
                {
                    docId = file.readInt()+docId;
                    int tftd = file.readInt();
                    List<Integer> positions=new ArrayList();
                    int gap=0;
                    for(int j=0;j<tftd ;j++)
                    {
                        gap=gap+file.readInt();
                        positions.add(gap);
                    }
                    answer.add(new Posting(docId,positions)) ;
                }
            
        	 }
    	} 
        catch (IOException ex) {
            Logger.getLogger(DiskInvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return answer;
    }

    @Override
    public List<String> getVocabulary() {
    	List<String> vocabulary = new ArrayList<>();
        map.forEach((term,derp) -> {
        	vocabulary.add(term);
        });
        
        return vocabulary;
    }
    
    public void closeDB()
    {
    	db.close();
    }

    @Override
    public void addTerm(String s, int id, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void print() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setIndex(KGramIndex index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public KGramIndex getIndex() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Integer> getDocIds(String term) {
        long address=map.get(term);
        List<Integer> ids=new ArrayList();
        try {
            file.seek(address);
            int dft=file.readInt();
            int gap=0;
            for(int i=0;i<dft;i++)
            {
                if(i==0)ids.add(file.readInt());
                else ids.add(file.readInt()+ids.get(ids.size()-1));
                int tftd=file.readInt();
                for(int j=0;j<tftd;j++)
                {
                    file.readInt();
                }
                //file.seek(address);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(DiskInvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ids;
    }

	@Override
	public void setDocumentWeights(TreeMap<Integer, Double> map) {
		// TODO Auto-generated method stub
		documentWeights = map;
		
	}
	

	@Override
	public TreeMap<Integer, Double> getDocumentWeights() {
		TreeMap<Integer,Double> map = new TreeMap<>();
		
		int docNum = 1;
		try {
			while(weightsFile.getFilePointer() != weightsFile.length())
			{
				map.put(docNum, weightsFile.readDouble());
				docNum++;
			}
			
			
			for(int i = 1; i < map.size()+1; i++) {
				System.out.println("Document " + i + " Weight: " + map.get(i));
			}
			return map;
		}
		catch(EOFException ex)
		{
			Logger.getLogger(DiskInvertedIndex.class.getName()).log(Level.SEVERE,null,ex);
		}
		catch(IOException ex)
		{
			Logger.getLogger(DiskInvertedIndex.class.getName()).log(Level.SEVERE,null,ex);
		}
		catch(Exception ex)
		{
			
		}
		
		return null;
	}
    
}
