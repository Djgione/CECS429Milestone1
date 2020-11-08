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
public class Money {
    int balance;
    int Dollars;
    int Quarters;
    int	dimes;
    int	Nickels;
    int	Pennies;
    Money(double bal){
        Dollars=Quarters=dimes=Nickels=Pennies=0;
        balance = (int) (bal * 100);
    }	   
}

