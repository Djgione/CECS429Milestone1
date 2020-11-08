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
public class Pennies extends Handler {

    public Pennies() {
    }

    @Override
    public void HandleRequest(Money m) {
        m.Pennies=m.balance/1;
        m.balance=m.balance % 1;
    }
    
}
