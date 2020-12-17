/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.queries;

import cecs429.index.BiWordIndex;
import cecs429.index.Index;
import cecs429.index.Posting;
import cecs429.text.IntermediateTokenProcessor;
import java.util.List;

/**
 *
 * @author kabir
 */
public class BiWordQuery implements Query {
    private String query;
    private boolean isbiword=true;
    public BiWordQuery(String query)
    {
        this.query=query;
    }
   @Override
    public List<Posting> getPosting(BiWordIndex biwordindex) {
       IntermediateTokenProcessor processor=new IntermediateTokenProcessor();
	   String[] arr=query.split(" ");
	   query="";
	   query=processor.processToken(arr[0]).get(0)+" "+processor.processToken(arr[1]).get(0);
       return biwordindex.getPostings(query);
    }

    @Override
    public void negative(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getnegative() {
        return false;
    }

    @Override
    public List<Posting> getPostings(Index index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Posting> getPostings(Index index, IntermediateTokenProcessor proc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isBiWord() {
        return isbiword;
    }

    @Override
    public void setBiWord() {
        isbiword=true;
    }

    


    
}
