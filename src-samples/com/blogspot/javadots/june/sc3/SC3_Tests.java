package com.blogspot.javadots.june.sc3;

import com.blogspot.javadots.jtestpad.JTestPad;

public class SC3_Tests extends JTestPad<SC3> {

   int stimulate() {
      return sut.add();
   }
   
   class SumsNumbersInInput_WhenInputIs {
      void empty() {
         expect(0).on(new SC3(""));
      }
      
      void aSingleValue() {
         expect(1).on(new SC3("1"));
      }

      void twoCommaSeparatedValues() {
         expect(3).on(new SC3("1,2"));
      }
      
      void manyCommaSeparatedValues() {
         expect(210).on(new SC3("10,20,30,40,50,60"));
      }
   }
   
   class Delimiters {
      void allowsNewlineAsDelimiter() {
         expect(3).on(new SC3("1\n2"));
      }
      void allowsNewlineToBeMixedWithComma() {
         expect(6).on(new SC3("1\n2,3"));
      }
      
      class ComputeSum_WhenCustomDelimiterIs {
         void aSingleLetter() {
            expect(3).on(new SC3("//;\n1;2"));
         }
         void severalLettersLong() {
            expect(3).on(new SC3("//xyz\n1xyz2"));
         }         
      }
   }
   
   class ErrorHandling {
      class RejectsIncorrectInput_WhenInputHasANegativeValue {
         void asTheOnlyValue() {
            expect(IllegalArgumentException.class).on(new SC3("-1"));
         }
         void asOneOfSeveralPositiveValues() {
            expect(IllegalArgumentException.class).on(new SC3("4,3,-1,2"));
         }
         void asOneOfSeveralNegativeAndPositiveValues() {
            expect(IllegalArgumentException.class).on(new SC3("4,3,-1,2,-5"));
         }
      }
      class ReportsNegativeValuesInErrorMessage_WhenInputHasANegativeValue {
         void asTheOnlyValue() {
            expect(IllegalArgumentException.class, 
               "Negatives not allowed: -1").on(new SC3("-1"));
         }
         void asOneOfSeveralPositiveValues() {
            expect(IllegalArgumentException.class, 
               "Negatives not allowed: -1").on(new SC3("4,3,-1,2"));
         }
         void asOneOfSeveralNegativeAndPositiveValues() {
            expect(IllegalArgumentException.class, 
               "Negatives not allowed: -1, -5").on(new SC3("4,3,-1,2,-5"));
         }
      }
      
      class RejectsDelimiter_WhenDelimiterIs {
         void empty() {
            expect(IllegalArgumentException.class, "Delimiter cannot be empty").on(new SC3("//\na"));
         }
         void aNumber() {
            expect(IllegalArgumentException.class, "Delimiter cannot be a number").on(new SC3("//34\n1"));
         }
      }
   }
}
