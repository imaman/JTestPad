package com.blogspot.javadots.june.money;

public class Money {

   private final int amount;
   private final int cents;
   private final String currency;
   
   
   public Money(Integer amount, Integer cents, String currency) {
      this.amount = amount;
      this.cents = cents;
      this.currency = currency;
   }
   
   public Money add(int amount_, int cents_) {
      int cs = cents + cents_;
      int am = amount + amount_;
      if(cs >= 100) {
         am += cs/100;
         cs = cs % 100;
      }
      
      return new Money(am, cs, currency);
   }
   
   public Money subtract(int amount_, int cents_) {
      return new Money(amount - amount_, cents - cents_, currency);
   }
   
   public String toString() {
      return currency + amount + "." + (cents < 10 ? "0" : "") + cents
      ;
   }
   
   
   
   
   
}
