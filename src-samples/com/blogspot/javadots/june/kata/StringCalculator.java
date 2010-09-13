package com.blogspot.javadots.june.kata;

public class StringCalculator {

   private String input;
   String delimiter = ",";

   public StringCalculator(String s) {
      if(s.startsWith("//")) {
         int newlineIndex = s.indexOf('\n');
         delimiter = s.substring(2, newlineIndex);
         s = s.substring(newlineIndex);
      }
      this.input = s.replace("\n", delimiter);
   }

   public int add() {
      return sum(input);
   }

   private int sum(String s) {
      int index = s.indexOf(delimiter);
      if(index >= 0)
         return toInt(s.substring(0, index)) + sum(s.substring(index + delimiter.length())); 
      return toInt(s);
   }

   private int toInt(String s) {
      return s.isEmpty() ? 0 : Integer.parseInt(s);
   }

}
