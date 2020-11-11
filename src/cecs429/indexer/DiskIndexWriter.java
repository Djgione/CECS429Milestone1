/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.indexer;
import cecs429.index.BiWordIndex;
import cecs429.index.Index;
import cecs429.index.KGramIndex;
import cecs429.index.PositionalInvertedIndex;
import cecs429.index.Posting;
import cecs429.weights.DocumentValuesModel;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

/**
 *
 * @author Kermite
 */
public class DiskIndexWriter {
    //need to create a db that creates a db file in order to..
    private DB db; 
    private BTreeMap<String,Long> map;
    private DB kgramdb;
    private BTreeMap<String,Long> KgramMap;
    public DiskIndexWriter(String path)
    {
        db = DBMaker.fileDB(path+"/theDB").make();
        map = db.treeMap("map")
                                    .keySerializer(Serializer.STRING)
                                    .valueSerializer(Serializer.LONG)
                                    .createOrOpen();
        
        kgramdb=DBMaker.fileDB(path+"/KgramDB").make();
        KgramMap=kgramdb.treeMap("kgrammap")
                                    .keySerializer(Serializer.STRING)
                                    .valueSerializer(Serializer.LONG)
                                    .createOrOpen();
    }
   
    public DiskIndexWriter()
    {
    	
    }

   
    
    public DB getDb() {
        return db;
        
    }
    public BTreeMap<String,Long> getBplustree()
    {
        return map;
    }
    public void writeIndex(Index index, Path path)
    {
        //the list of 8 byte integer values consisting of byte positions where
        //start of postings list occurs in postings.bin

        //..make a b+ tree using BTreeMap

//         BTreeMap<String, Long> map = db.treeMap("map")
//                                    .keySerializer(Serializer.STRING)
//                                    .valueSerializer(Serializer.LONG)
//                                    .createOrOpen();
         // Map the frequency of terms appearing
        // Map<Integer, Map<String,Integer>> mapForCalculation = new HashMap<>();
         
        
        //..make a b+ tree using BTreeMap       
        try
        {
            DataOutputStream out = 
                    new DataOutputStream(
                    new BufferedOutputStream(
                    new FileOutputStream(path.toString() + "/postings.bin")));

            //int previousId = 0;

            for(String term : index.getVocabulary())
            {

                List<Posting> postingObjs = index.getPostings(term);
                
                //get dft and write to disk
                //System.out.println("term "+ term);
                int dft = postingObjs.size();                   
                long postingsByteBegin = out.size();
                map.put(term, postingsByteBegin);
                out.writeInt(dft);
                
                //current value of the counter written(byte position where postings for term begin?)
               // long postingsByteBegin = out.size();
                //map.put(term, postingsByteBegin);
                                
                

                for(int i = 0; i < postingObjs.size(); i++)
                {
                    if(i==0)
                    {
                        out.writeInt(postingObjs.get(0).getDocumentId());
                       // System.out.println("debug "+ postingObjs.get(0).getDocumentId());

                    }
                    else 
                    {
                        
                        int idGap = postingObjs.get(i).getDocumentId()
                              - postingObjs.get(i-1).getDocumentId();
                        out.writeInt(idGap);
                       // System.out.println("debug: "+postingObjs.get(i).getDocumentId()
                              //+"    "+ postingObjs.get(i-1).getDocumentId());
                    }                    
                                   
                    
                    //take the gap of current docId & previousId and write that 
                    //to disk
                 
                    //System.out.println("id gap "+idGap);
                    //set the previous id as current id for next gap
                    //   previousId = postingObjs.get(i).getDocumentId();
                    
                
                    List<Integer> positions = postingObjs.get(i).getPositions();
                    
                    //get and write tftd to disk
                    int tftd = positions.size();                   
                    out.writeInt(tftd);
                   // System.out.println("tftd "+tftd);
                    //System.out.println("tftd "+tftd);
                    
                    int previousPos = 0;

                    for(int j = 0; j < tftd; j++)
                    {
                        int positionGap = positions.get(j) 
                                        - previousPos;
                        out.writeInt(positionGap);
                      //  System.out.println("position gap " + positionGap);
                       //System.out.println("position gap " + positionGap);
                        previousPos = positions.get(j);
                    }
                }    
            }
            
            out.close();
            db.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        weightWriter(index,path);
        try {
        //writeKgramIndex(index.getIndex(),path);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
       // return map;
    }
            
    
    
    /**
     * Write the weight of documents to a file named docWeights.bin
     * @param index
     * @param path
     * @return
     */
    private boolean weightWriter(Index index, Path path)
    {
    	String pathWeights = path.toString() + "\\index\\docWeights.bin";
    	File file = new File(pathWeights);
    	
    	try {
    		
    		if(file.exists())
    		{
    			file.delete();
    			file.createNewFile();
    		}
    		
    		System.out.println("File created at " + pathWeights);
    		DataOutputStream out = 
    	                    new DataOutputStream(
    	                    new BufferedOutputStream(
    	                    new FileOutputStream(pathWeights)));
    	
    		int totalLength = 0;
    	//This is in order due to index only containing a TreeMap
    	// Order of Creatoin : weights, averageTfd, length, byte
    	DocumentValuesModel temp = index.getDocumentValuesModel();
    	for(int i = 0; i < temp.getDocWeights().size(); i++) {
    		out.writeDouble(temp.getDocWeights().get(i));
    		totalLength+= temp.getDocLengths().get(i);
    		out.writeDouble(temp.getDocAverageTFDs().get(i));
    		out.writeInt(temp.getDocLengths().get(i));
    		out.writeLong(temp.getByteSizes().get(i));
    	}
    	
    	out.writeInt(totalLength);
    	
    	out.close();
    	}
    	catch(IOException e)
    	{
    		System.out.println(e.getStackTrace());
    		return false;
    	}
    	return true;
    }
    
    
    
    public void writeKgramIndex(KGramIndex kgramIndex,Path path) throws FileNotFoundException, IOException {
    	
    	File file = new File(path.toString() + "\\Kgrampostings.bin");
        DataOutputStream out = 
                    new DataOutputStream(
                    new BufferedOutputStream(
                    new FileOutputStream(file)));
        
        List<String> vocab=kgramIndex.getVocab();
        for(String s: vocab)
        {
            long currentpos=out.size();
            //System.out.println(s+"->   "+currentpos);
            KgramMap.put(s, currentpos);
            List<String> postings=kgramIndex.getPostings(s);
            out.writeInt(postings.size());
            for(String str: postings)
            {
                byte[] b= str.getBytes(StandardCharsets.UTF_8);
                out.writeInt(b.length);
                out.write(b);
                
            }
        }
        //System.out.println(out.size()+"SIZE ");
       // kgramdb.close();
        out.close();
        //kgramIndex.print();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        //System.out.print(path);
//        RandomAccessFile file=new RandomAccessFile(path.toString()+"/Kgrampostings.bin","r");
//        file.seek(0);
//        //System.out.print(file.length());
//        
//        for(Object s : KgramMap.keySet())
//        {
//            System.out.print(s+"  ->  ");
//            //System.out.println(s+"->"+KgramMap.get(s));
//            file.seek(KgramMap.get(s));
//            int postings=file.readInt();
//            //System.out.println(postings);
//            for(int i=0;i<postings;i++)
//            {
//                int size=file.readInt();
//                byte[] b=new byte[size];
//                file.read(b);
//                
//                System.out.print(new String(b,StandardCharsets.UTF_8)+" ,");
//                
//            }
//            System.out.println();
            
        
        //file.close();
        
        kgramdb.close();
        
    }
    
    public void DeleteBinFile(String path)throws FileNotFoundException, IOException
    {
		Files.deleteIfExists(Paths.get(path+ "\\index\\postings.bin").toAbsolutePath());
    }
    
}
