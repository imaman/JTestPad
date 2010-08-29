package com.blogspot.javadots.june.kata;

import com.blogspot.javadots.june.TestBed;

public class StringCalculator_Tests extends TestBed<StringCalculator>{

   Object stimulate() {
      return sut.add();
   }
   
   class ComputeSumOfIntegers {
      void singleValue() {
         expect(1).from(new StringCalculator("1"));
      }
      
      void emptyInput() {
         expect(0).from(new StringCalculator(""));
      }
   
      void twoValues() {
         expect(3).from(new StringCalculator("1,2"));
      }
   
      void threeValues() {
         expect(6).from(new StringCalculator("1,2,3"));
      }
   }
   
   class Delimiters
   {   
      void newline() {
         expect(3).from(new StringCalculator("1\n2"));
      }
      
      void newlineMixedWithComma() {
         expect(6).from(new StringCalculator("1\n2,3"));
      }
      
      class Custom 
      {      
         void semicolon() {
            expect(3).from(new StringCalculator("//;\n1;2"));
         }
         
         void atSign() {
            expect(6).from(new StringCalculator("//@\n1@2@3"));
         }
         
         void moreThanOneLetter() {
            expect(6).from(new StringCalculator("//AB\n1AB2AB3"));
         }
         
         void severalLettersLong() {
            expect(6).from(new StringCalculator("//ZXCVBNM\n1ZXCVBNM2ZXCVBNM3"));
         }
      }
   }
}
