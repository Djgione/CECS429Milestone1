/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.index;

import java.io.DataInputStream;
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
    
    public DiskInvertedIndex(String path) throws FileNotFoundException, IOException
    {
        db = DBMaker.fileDB(path+"/theDB").make();
        map = db.treeMap("map")
                                    .keySerializer(Serializer.STRING)
                                    .valueSerializer(Serializer.LONG)
                                    .createOrOpen();
        file=new RandomAccessFile(path+"/index/postings.bin","r");
        
//        for(Object s: map.keySet())
//        {
//            file.seek(map.get(s));
//
//            int dft=file.readInt();
//            System.out.println("term :  "+ s +" dft: "+dft);
//            int docId=0;
//            for(int i=0;i<dft;i++)
//            {
//                
//                docId = file.readInt()+docId;
//                
//                int tftd = file.readInt();
//                System.out.print("docid: "+docId);
//                System.out.print("     positions:   ");
//                int gap=0;
//                for(int j=0;j<tftd ;j++)
//                {
//                    gap=gap+file.readInt();
//                    System.out.print(gap+" ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//            
//            
//        }
       
        
    }

    /**
     *
     * @param term
     * @return
     * @throws Exception
     */
    @Override
    public List<Posting> getPostings(String term){
        long address = map.get(term);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getVocabulary() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
		// TODO Auto-generated method stub
		return null;
	}
    
}
