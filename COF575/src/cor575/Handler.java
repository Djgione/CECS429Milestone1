/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cor575;

/**
 *
 * @author kabir
 */
public abstract class Handler {
    protected  Handler successor;
    public  Handler() {
	successor = null;
    }
    void SetSuccessor(Handler successor)   {
	this.successor = successor;
    }
    public abstract void HandleRequest(Money m);
}
