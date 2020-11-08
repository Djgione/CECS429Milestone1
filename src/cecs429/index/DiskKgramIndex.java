/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.index;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

/**
 *
 * @author kabir
 */
public class DiskKgramIndex {
    private DB db; 
    private BTreeMap <String,Long> map;
    RandomAccessFile file;
    public DiskKgramIndex(String path) throws FileNotFoundException
    {
        db = DBMaker.fileDB(path+"/KgramDB").make();
        map = db.treeMap("kgrammap")
                                    .keySerializer(Serializer.STRING)
                                    .valueSerializer(Serializer.LONG)
                                    .createOrOpen();
        file=new RandomAccessFile(path+"/Kgrampostings.bin","r");
    }
    public List<String> getPostings(String term) throws IOException
    {
    	List<String> list=new ArrayList();
    	if(!map.containsKey(term)) return list;
        file.seek(map.get(term));
        int postings=file.readInt();
        for(int i=0;i<postings;i++)
        {
            int size=file.readInt();
            byte[] b=new byte[size];
            file.read(b);
            list.add(new String(b,StandardCharsets.UTF_8));
        }
        
        return list;
    }
}
