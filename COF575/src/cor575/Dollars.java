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
public class Dollars extends Handler {

    public Dollars() {
    }

    @Override
    public void HandleRequest(Money m) {
        m.Dollars=m.balance/100;
        m.balance=m.balance % 100;
        this.successor.HandleRequest(m);
    }
    
}
