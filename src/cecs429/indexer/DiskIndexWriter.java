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
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private DB db = DBMaker.fileDB("theDB").make();

    public DB getDb() {
        return db;
    }
    public BTreeMap<String,Long> writeIndex(Index index, Path path)
    {
        //the list of 8 byte integer values consisting of byte positions where
        //start of postings list occurs in postings.bin
        
        //..make a b+ tree using BTreeMap
         BTreeMap<String, Long> map = db.treeMap("map")
                                    .keySerializer(Serializer.STRING)
                                    .valueSerializer(Serializer.LONG)
                                    .createOrOpen();
         // Map the frequency of terms appearing
         Map<String, Integer> freqMap = new HashMap<>();
         
         
        
        try
        {
            DataOutputStream out = 
                    new DataOutputStream(
                    new BufferedOutputStream(
                    new FileOutputStream(path.toString() + "/index/postings.bin")));
            
            int previousId = 0;
            for(String term : index.getVocabulary())
            {
                List<Posting> postingObjs = index.getPostings(term);
                
                //get dft and write to disk
                int dft = postingObjs.size();               
                
                out.writeInt(dft);
                
                //current value of the counter written(byte position where postings for term begin?)
                long postingsByteBegin = out.size();
                map.put(term, postingsByteBegin);
                                
                
                
                for(int i = 0; i < postingObjs.size(); i++)
                {
                    int idGap = postingObjs.get(i).getDocumentId()
                              - previousId;
                    //take the gap of current docId & previousId and write that 
                    //to disk
                 
                    out.write(idGap);
                    //set the previous id as current id for next gap
                    previousId = postingObjs.get(i).getDocumentId();
                    
                    List<Integer> positions = postingObjs.get(i).getPositions();
                    
                    //get and write tftd to disk
                    int tftd = positions.size();     
                    
                    out.write(tftd);
                    int previousPos = 0;

                    for(int j = 0; j < tftd; j++)
                    {
                        int positionGap = positions.get(j) 
                                        - previousPos;
                        out.writeInt(positionGap);
                        previousPos = positions.get(j);
                    }
                }             
            }
           
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return map;
    }
    
}
