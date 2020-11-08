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
public class Quarters extends Handler {

    public Quarters() {
    }

    @Override
    public void HandleRequest(Money m) {
        m.Quarters=m.balance / 25;
        m.balance=m.balance % 25;
        this.successor.HandleRequest(m);
    }
    
}
