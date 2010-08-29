package com.blogspot.javadots.june.money;

import com.blogspot.javadots.june.TestBed;


public class Money_Tests extends TestBed<Money> {

   class Feature_ToString 
   {
      Object simple() {
         return expect("$23.45").from(new Money(23, 45, "$")).toString();         
      }
      
      Object centsLessThanTen() {
         return expect("$23.09").from(new Money(23, 9, "$")).toString();         
      }
   }
   
   class Feature_Addition
   {
      Object zero() {
         return expect("$54.00").from(new Money(54, 0, "$")).add(0, 0).toString();
      }

      Object amountIsZero() {
         return expect("$54.20").from(new Money(54, 0, "$")).add(0, 20).toString();
      }

      Object centsOverflow() {
         return expect("$55.01").from(new Money(54, 60, "$")).add(0, 41).toString();
      }
   }
   
   class Feature_Subtraction
   {
      Object zero() {
         return expect("$54.00").from(new Money(54, 0, "$")).subtract(0, 0).toString();
      }

      Object centsIsZero() {
         return expect("$53.05").from(new Money(54, 5, "$")).subtract(1, 0).toString();
      }
   }

}
