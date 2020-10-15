/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.documents;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 *
 * @author Kermite
 */
public class JsonFileDocument implements FileDocument {
    private final int mDocumentId;
    private final Path mFilePath;
    private String title;
    private String body;
    
    public JsonFileDocument(int id, Path absoluteFilePath)
    {
        mDocumentId = id;
        mFilePath = absoluteFilePath;
        getContent();
    } 
    
    @Override
    public Path getFilePath() {return mFilePath;}
    
    @Override
    public int getId() {return mDocumentId;}
    
    @Override
    public Reader getContent()
    {
        try
        {
            JsonReader jsonReader = new JsonReader(Files.newBufferedReader(mFilePath));
            
            String content = "";
            jsonReader.beginObject();
            
            while(jsonReader.hasNext())
            {
                String nextName = jsonReader.nextName();
                
                switch (nextName) {
                    case "body":
                        
                        content = jsonReader.nextString();
                        break;
                    case "url":
                        jsonReader.nextString();
                        break;
                    case "title":
                        title = jsonReader.nextString();
                        break;
                    default:
                        break;
                }
            }
            
            jsonReader.endObject();
            jsonReader.close();
            
            body = content;
            
            return new StringReader(content);
            
        } catch (IOException e) 
        {
            throw new RuntimeException(e);
        }
    }
    
    public String getBody()
    {
        return body;
    }
    
    @Override
    public String getTitle()
    {
        return title;
    }
    
    public static FileDocument loadJsonFileDocument(Path absPath, int docId)
    {
        return new JsonFileDocument(docId, absPath);
    }
    
    @Override
    public String toString()
    {
    	return  title + " " + mFilePath;
    }
}

