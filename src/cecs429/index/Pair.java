/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cecs429.index;

/**
 *
 * @author kabir
 */
public class Pair {
    private int docid,tftd;
    public Pair(int docid ,int tftd)
    {
        this.docid=docid;
        this.tftd=tftd;
    }
    public int getDocId()
    {
        return docid;
    }
    public int getTftd()
    {
        return tftd;
    }
    
}
