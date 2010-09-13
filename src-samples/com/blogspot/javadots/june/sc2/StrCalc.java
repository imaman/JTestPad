package com.blogspot.javadots.june.sc2;

public class StrCalc {

   private String input;
   String delimiter = ",";

   public StrCalc(String s) {      
      this.input = s;
   }

   public int add() {
      if(input.startsWith("//")) {
         int endOfLine = input.indexOf('\n');
         delimiter = input.substring(2, endOfLine);
         if(delimiter.isEmpty())
            throw new IllegalArgumentException(
               "Custom delimiter cannot be empty");
         try {
            Integer.parseInt(delimiter);
            throw new IllegalArgumentException(
               "Custom delimiter cannot be a number");
         }
         catch(NumberFormatException e) {
            // OK
         }
         input = input.substring(endOfLine + 1);
      }

      return add(input.replace("\n", delimiter));
   }

   private int add(String s) {
      int comma = s.indexOf(delimiter);
      if(comma < 0)
         return toInt(s);
      
      return toInt(s.substring(0, comma)) 
         + add(s.substring(comma+delimiter.length()));
   }

   private static int toInt(String input) {
      int n = input.isEmpty() ? 0 : Integer.parseInt(input);
      if(n < 0)
         throw new IllegalArgumentException();
      
      return n;
   }

}
