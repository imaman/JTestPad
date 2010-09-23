package com.blogspot.javadots.june.stringcalculator;

import com.blogspot.javadots.testpad.TestPad;

public class StringCalculator_Tests extends TestPad<StringCalculator> {

   protected Object stimulate() {
      return sut.add();
   }

   // I don't want to write "zero should be empty"
   // I want to arrange my tests in hierarchies
   // I don't want to duplicate my stimulation code
   // I want to cut down on boilerplate code (such as defining a subject variable at each case)
   // I want to minimize syntactic noise (public @Test, etc.)
   // I don't want to choose veryLongNamesForTestMethodsToExpressTheirPurpose
   // ??I don't want to skew my design because the testing frameworks makes it harder to code in constructors, etc.
   class AdditionOfIntegers {
      void empty() {
         expect(0).on(new StringCalculator(""));
      }

      void single() {
         expect(1).on(new StringCalculator("1"));
      }

      void twoValues() {
         expect(3).on(new StringCalculator("1,2"));
      }

      void threeValues() {
         expect(6).on(new StringCalculator("1,2,3"));
      }
   }

   class Delimiters {

      class NewLineCanBe_a_Delimiter {
         void single() {
            expect(3).on(new StringCalculator("1\n2"));
         }

         void mixed() {
            expect(6).on(new StringCalculator("1\n2,3"));
         }
      }

      class Custom {
         void singleSemicolon() {
            expect(3).on(new StringCalculator("//;\n1;2"));
         }

         void singleX() {
            expect(3).on(new StringCalculator("//X\n1X2"));
         }

         void canBe_a_Word() {
            expect(3).on(new StringCalculator("//AB\n1AB2"));
         }

         void canBeLong() {
            expect(3).on(new StringCalculator("//ABCDEF\n1ABCDEF2"));
         }
      }
   }

   class FiresResultEvents {
      Integer n = null;
      
      int stimulate() { 
         sut.add();
         return n;
      }

      void correctInput() {
         expect(6).on(new StringCalculator("1,2,3")).addListener(
            new StringCalculatorListener() {
               public void newResult(int r) {
                  n = r;
               }
            });
      }

      void incorrectInput() {
         expect(null).on(new StringCalculator("-5")).addListener(
            new StringCalculatorListener() {
               public void newResult(int r) {
                  n = r;
               }
            });
      }
   }

   class ErrorHandling {
      
      class RejectsNegatives {
         
         void singleNegative() {
            expect(IllegalArgumentException.class).on(
               new StringCalculator("-1"));
         }

         void singleNegativeSeveralPositives() {
            expect(IllegalArgumentException.class).on(
               new StringCalculator("3,-1,2"));
         }
      }

      class ReportsNegatives {
         
         void singleNegative() {
            expect(IllegalArgumentException.class, "Negatives: -1").on(
               new StringCalculator("-1"));
         }

         void twoNegatives() {            
            expect(IllegalArgumentException.class, "Negatives: -1 -2").on(
               new StringCalculator("-1,-2"));
         }

         void mixed() {            
            expect(IllegalArgumentException.class, "Negatives: -1 -2").on(
               new StringCalculator("3,-1,5,-2"));
         }
      }
   }

   class toStringReflectsInput {
      
      String stimulate() {
         return sut.toString();
      }

      void empty() {
         expect("").on(new StringCalculator(""));
      }

      void single() {
         expect("1").on(new StringCalculator("1"));
      }
   }
}
