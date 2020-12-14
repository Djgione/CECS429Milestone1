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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import cecs429.weights.DocumentValuesModel;

/**
 *
 * @author kabir
 */
public class DiskInvertedIndex implements Index{

	private DB db; 
	private BTreeMap <String,Long> map;
	RandomAccessFile file;
	RandomAccessFile weightsFile;
	private DocumentValuesModel model;
	DiskKgramIndex dki;


	public DiskInvertedIndex(String path) throws FileNotFoundException, IOException
	{
		db = DBMaker.fileDB(path+"/theDB").make();
		map = db.treeMap("map").keySerializer(Serializer.STRING)
				.valueSerializer(Serializer.LONG)
				.createOrOpen();
		file=new RandomAccessFile(path+"/postings.bin","r");       
		weightsFile = new RandomAccessFile(path+"/docWeights.bin","r");


	}

	/**
	 *
	 * @param term
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Posting> getPostings(String term){
		List<Posting> answer=new ArrayList<>();

		try {
			if(map.get(term)==null)
				return answer;

			file.seek(map.get(term));
			int dft=file.readInt();
			
			int docId=0;
			for(int i=0;i<dft;i++)
			{
				docId = file.readInt()+docId;
				int tftd = file.readInt();
				List<Integer> positions=new ArrayList<>();
				int gap=0;
				for(int j=0;j<tftd ;j++)
				{
					gap=gap+file.readInt();
					positions.add(gap);
				}

				answer.add(new Posting(docId,positions)) ;
			}
		} catch (IOException ex) {
			Logger.getLogger(DiskInvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
		}           


		return answer;
	}

	@Override
	public List<Posting> getPostings() {
		List<Posting> answer=new ArrayList<>();

		try 
		{
			System.out.println("getVocabulary().size()" + getVocabulary().size());
			for(int count = 0; count < getVocabulary().size(); count++)
			{
				int dft=file.readInt();
				System.out.println(dft);
				int docId=0;
				for(int i=0;i<dft;i++)
				{
					docId = file.readInt()+docId;
					int tftd = file.readInt();
					List<Integer> positions=new ArrayList<>();
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

	public void closeandDeleteDB(String path)
	{

		db.close();
		try {
			file.close();

			Files.deleteIfExists(Paths.get(path+ "\\theDB").toAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addTerm(String s, int id, int i) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void print() {
		for(Object s:map.keySet())
		{
			System.out.print(s+" ->");

			for(Posting p: getPostings(s.toString()))
			{
				System.out.print("docid:"+p.getDocumentId()+"  ");
				for(Integer i:p.getPositions())
				{
					System.out.print(i+" ");
				}
				System.out.println();
			}
		}
	}

	@Override
	public void setIndex(KGramIndex index) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public KGramIndex getIndex() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	public void setKgram(DiskKgramIndex dki)
	{
		this.dki=dki;
	}


	@Override
	public List<Pair> getDocIds(String term) {
		if(map.get(term) == null)
			return new ArrayList<Pair>();
		long address=map.get(term);
		List<Pair> ids=new ArrayList();
		try {
			file.seek(address);
			int dft=file.readInt();
			int gap=0;
			for(int i=0;i<dft;i++)
			{
				int docid=file.readInt();
				int tftd=file.readInt();
				if(i==0)ids.add(new Pair(docid,tftd));
				else ids.add(new Pair(docid+ids.get(ids.size()-1).getDocId(),tftd));
				file.seek(file.getFilePointer()+(tftd*4));               
			}

		} catch (IOException ex) {
			Logger.getLogger(DiskInvertedIndex.class.getName()).log(Level.SEVERE, null, ex);
		}
		return ids;
	}



	@Override
	public void setDocumentValuesModel(DocumentValuesModel model) {
		this.model = model;

	}

	@Override
	public DocumentValuesModel getDocumentValuesModel() {
		// TODO Auto-generated method stub
		return model;
	}





}
