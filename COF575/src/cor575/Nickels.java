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
public class Nickels extends Handler {

    public Nickels() {
    }

    @Override
    public void HandleRequest(Money m) {
        m.Nickels=m.balance/5;
        m.balance=m.balance%5;
        this.successor.HandleRequest(m);    }
    
}
