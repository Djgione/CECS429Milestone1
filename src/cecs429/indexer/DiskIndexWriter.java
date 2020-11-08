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
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
    public DiskIndexWriter(String path)
    {
        db = DBMaker.fileDB(path+"/theDB").make();
        map = db.treeMap("map") .keySerializer(Serializer.STRING)
                                .valueSerializer(Serializer.LONG)
                                .createOrOpen();
   
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
        try
        {
            DataOutputStream out = 
                    new DataOutputStream(
                    new BufferedOutputStream(
                    new FileOutputStream(path.toString() + "/index/postings.bin")));
            
            //int previousId = 0;
            for(String term : index.getVocabulary())
            {

                List<Posting> postingObjs = index.getPostings(term);
                
                //get dft and write to disk
                int dft = postingObjs.size();               
                long postingsByteBegin = out.size();
                map.put(term, postingsByteBegin);
                out.writeInt(dft);
                
                //current value of the counter written(byte position where postings for term begin?)
                
                for(int i = 0; i < postingObjs.size(); i++)
                {
                    if(i==0)
                    {
                        out.writeInt(postingObjs.get(0).getDocumentId());
                    }
                    else 
                    {
                        
                        int idGap = postingObjs.get(i).getDocumentId()
                              - postingObjs.get(i-1).getDocumentId();
                        out.writeInt(idGap);
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
                    System.out.println("tftd "+tftd);
                    int previousPos = 0;

                    for(int j = 0; j < tftd; j++)
                    {
                        int positionGap = positions.get(j) 
                                        - previousPos;
                        out.writeInt(positionGap);
                        System.out.println("position gap " + positionGap);
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
        
    }
    
    public void DeleteBinFile(String path)throws FileNotFoundException, IOException
    {
		Files.deleteIfExists(Paths.get(path+ "\\index\\postings.bin").toAbsolutePath());
    }
    
}
