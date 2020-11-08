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
public class Dimes extends Handler {

    public Dimes() {
    }

    @Override
    public void HandleRequest(Money m) {
        m.dimes=m.balance/10;
        m.balance=m.balance%10;
        this.successor.HandleRequest(m);
    }
    
}
