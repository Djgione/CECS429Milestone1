/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cor575;

import java.util.Scanner;

/**
 *
 * @author kabir
 */
public class AppClass {
    Handler h0, h1, h2, h3, h4;
    
    
    public AppClass(){
        h4 = new Dollars();
        h0 = new Quarters();
        h1 = new Dimes();
        h2 = new Nickels();
        h3 = new Pennies();
        h0.SetSuccessor(h1);
        h1.SetSuccessor(h2);
        h2.SetSuccessor(h3);
        h4.SetSuccessor(h0);
    }
    
    public void run(){
        Scanner input = new Scanner(System.in);
        double amountDue, change;
        System.out.print("Enter amount due:");
	
        amountDue = input.nextDouble();

        System.out.print("Enter cash tendered:");
        change = input.nextDouble();
        
        change = change - amountDue;
        Money m = new Money(change);

        while(m.balance > 0) 
           h4.HandleRequest(m);

        System.out.println("Dollars =  " + m.Dollars);
        System.out.println("Quarters = " + m.Quarters);
        System.out.println("Dimes = " + m.dimes);
        System.out.println("Nickels = " + m.Nickels);
        System.out.println("Pennies = " + m.Pennies);
    }
    
}
