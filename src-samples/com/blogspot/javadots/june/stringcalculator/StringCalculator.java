package com.blogspot.javadots.june.stringcalculator;

import java.util.ArrayList;
import java.util.List;

public class StringCalculator {

   private String input;
   private String delimiter = ",";
   private String negs = "";
   private List<StringCalculatorListener> listeners = new ArrayList<StringCalculatorListener>();

   public StringCalculator(String s) {
      this.input = s;      
   }
   
   public int add() {
      if(input.startsWith("//")) {
         int nl = input.indexOf('\n');
         delimiter = input.substring(2, nl);
         input = input.substring(nl + 1);
      }

      input = input.replace("\n", delimiter);
      int result = sum(input);
      
      if(!negs.isEmpty())
         throw new IllegalArgumentException("Negatives:" + negs);
      
      for(StringCalculatorListener curr : listeners)
         curr.newResult(result);
      
      return result;
   }

   private int sum(String input) {      
      int index = input.indexOf(delimiter);
      return index >= 0 ? toInt(input.substring(0, index)) + sum(input.substring(index + delimiter.length())) : toInt(input);
   }

   private int toInt(String s) {
      int n = s.isEmpty() ? 0 : Integer.parseInt(s);
      if(n < 0) 
         negs += " " + n;
      return n;
   }

   public void addListener(StringCalculatorListener listener) {
      listeners.add(listener);
   }
   
   @Override
   public String toString() {
      return input;
   }

}
