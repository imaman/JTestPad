package com.blogspot.javadots.june.sc2;

import com.blogspot.javadots.jtestpad.JTestPad;

public class StrCalc_Tests extends JTestPad<StrCalc>
{

   // Easily reuse stimulation code
   int stimulate() {
      return sut.add();
   }
   
   // I want to group functionality by topic
   // I want to organize tests in a hierarchical structure
   
   class ComputesSumOfNumbersInInput_WhenInputIs
   {
      // Each test defines a new "interesting" data point which needs to be
      // handled in order to provide the desired functionality. These can
      // hardly be considered as "new functionality" - these are merely
      // new angles from which we attack the same functionality of "computes sum"
      void empty() {
         expect(0).on(new StrCalc(""));
      }
      void aRawNumber() {
         expect(1).on(new StrCalc("1"));
         expect(2).on(new StrCalc("2"));
      }
      void twoCommaSeparatedValues() {
         expect(3).on(new StrCalc("1,2"));
         expect(30).on(new StrCalc("10,20"));
      }
      void threeCommaSeparatedValues() {
         expect(6).on(new StrCalc("1,2,3"));
      }
   }
   
   // We now define delimiter-related issues. This aspect is not a functionality
   // on its own right but rather a slight variation of the afore-mentioned 
   // core functionality. Thus Using the style of the core functionality
   // will not work : it adds very little useful information (as in "returnsSumWhenDelimiterIsSemicolon")
   // OTOH "allowsNewLineToBeMixedWithComma" is a meaningful description of behavior.
   class Delimiters {
      void allowsNewLineAsDelimiter() {
         expect(3).on(new StrCalc("1\n2"));
      }
      void allowsNewLineToBeMixedWithComma() {
         expect(6).on(new StrCalc("1\n2,3"));
      }
      
      // We can now explore custom delimiter which can be specified
      // via several triangulating data points
      class AllowsCustomDelimiter_WhenDelimiterIs
      {
         void aSingleCharacter() {
            expect(3).on(new StrCalc("//;\n1;2"));         
         }
         
         int severalLetters() {
            expect(3).on(new StrCalc("//ABC\n1ABC2"));
            return sut.add(); 
               // Not really needed but just to show that 
               // you can bypass the default stimulation
         }         
      }
   }  
   
   class ErrorHandling {
      class Complains_WhenInputContainsANegativeValue
      {
         void asTheOnlyValue() {
            expect(IllegalArgumentException.class).on(new StrCalc("-1"));
         }
         void asLastOfSeveralValues() {
            expect(IllegalArgumentException.class).on(new StrCalc("1,2,4,-10"));
         }
      }
      
      class Complains_WhenCustomDelimiter
      {
         void isEmpty() {
            expect(IllegalArgumentException.class, 
               "Custom delimiter cannot be empty").on(new StrCalc("//\n1"));
         }
         void isANumber() {
            expect(IllegalArgumentException.class, 
               "Custom delimiter cannot be a number").on(new StrCalc("//3\n1"));
         }
      }
   }
}
