package com.blogspot.javadots.june.sc3;

public class SC3 {

   private String input;
   private String delimiter = ",";
   private String negatives = "";

   public SC3(String string) {
      
      this.input = string;
      if(input.startsWith("//")) {
         int endOfLine = input.indexOf('\n');
         delimiter = "" + input.substring(2, endOfLine);
         input = input.substring(endOfLine+1);
      }      
      
      if(delimiter.isEmpty())
         throw new IllegalArgumentException("Delimiter cannot be empty");
      
      try {
         Integer.parseInt(delimiter);
         throw new IllegalArgumentException("Delimiter cannot be a number");
      }
      catch(NumberFormatException e) {
         // OK
      }

   }

   public int add() {
      int result = add(input.replace("\n", delimiter));
      if(negatives.length() > 0)
         throw new IllegalArgumentException("Negatives not allowed: " + negatives);         
      return result;
   }

   private int add(String s) {
      int result = 0;
      while(true) {
         int comma = s.indexOf(delimiter);
         if(comma < 0) {
            result += toInt(s);
            return result;
         }
         
         result += toInt(s.substring(0, comma)); 
         s = s.substring(comma + delimiter.length());
      }
   }

   private int toInt(String s) {
      int n = s.isEmpty() ? 0 : Integer.parseInt(s);
      if(n < 0)
         negatives += (negatives.length() > 0 ? ", " : "") + n;
      return n;
   }

}
