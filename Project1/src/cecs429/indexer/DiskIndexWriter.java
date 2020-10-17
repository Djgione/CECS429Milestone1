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
import java.util.List;
/**
 *
 * @author Kermite
 */
public class DiskIndexWriter {
    
    public List<Long> writeIndex(Index index, Path path)
    {
        //the list of 8 byte integer values consisting of byte positions where
        //start of postings list occurs in postings.bin
        List<Long> allTerms = new ArrayList<>();
        
        try
        {
            DataOutputStream out = 
                    new DataOutputStream(
                    new BufferedOutputStream(
                    new FileOutputStream(path.toString() + "/index/postings.bin")));
            
            for(String term : index.getVocabulary())
            {
                List<Posting> postingObjs = index.getPostings(term);
                
                //get dft and write to disk
                int dft = postingObjs.size();               
                out.writeInt(dft);
                
                //current value of the counter written(byte position?)
                long postingsByteBegin = out.size();               
                allTerms.add(postingsByteBegin);
                
                boolean firstDocIdOfFirstTerm = term.equals(index.getVocabulary().get(0));
                
                for(int i = 0; i <= postingObjs.size() - 2; i++)
                {
                    int docId = postingObjs.get(i).getDocumentId();
                    int idGap = postingObjs.get(i + 1).getDocumentId()
                              - postingObjs.get(i).getDocumentId();
                    //if it's the first term of the vocab list write docId
                    //to disk otherwise take the gap of current docId & next
                    //docId and write that to disk
                    if(firstDocIdOfFirstTerm)
                        out.write(docId);
                    else
                        out.write(idGap);
                    
                    List<Integer> positions = postingObjs.get(i).getPositions();
                    
                    //get and write tftd to disk
                    int tftd = positions.size();                   
                    out.write(tftd);
                    
                    for(int j = 0; j <= tftd - 2; j++)
                    {
                        int position = positions.get(j);
                         int positionGap = positions.get(j + 1) 
                                        - positions.get(j);
                        //if it's the first position in list, write position,
                        //else write gap of current Position and next position
                        if(j == 0)
                            out.writeInt(position);
                        else
                            out.writeInt(positionGap);
                    }
                }             
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return allTerms;
    }
    
}
