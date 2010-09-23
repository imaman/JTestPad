package com.blogspot.javadots.testpad;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;


public class TestPadRunner_Tests extends TestPad<TestPadRunner>
{
   class IsA_TestRunner
   {
      class shouldDetectTestMethods_When
      {
         int theyAreDeclaredInsideNestedClasses() {
            expect(2).on(new TestPadRunner(Sample_Tests.class));
            return sut.getDescription().getChildren().get(0).getChildren().size();
         }
         
         int theyAreAtTheTopLevel() {
            expect(3).on(new TestPadRunner(A.class));
            return sut.getDescription().getChildren().size();
         }
      }
      
      class shouldSkipOverWhen {
         int aClassIsTaggedWithIgnore() {
            expect(1).on(new TestPadRunner(Sample_Tests.class));
            return sut.getDescription().getChildren().size();
         }
         
         int aMethodIsTaggedWithIgnore() {
            expect(1).on(new TestPadRunner(Ignoring.class));
            return sut.getDescription().getChildren().size();            
         }
         
         int theClassIsAnonymous() {
            expect(1).on(new TestPadRunner(DefinesAnoynmousClass.class));
            Description d = sut.getDescription();
            return d.getChildren().size();
         }
      } 
      
      class shouldFireEventsWhen {
         
         private int n = 0;
         RunNotifier rn = new RunNotifier();
         RunListener rl;
         protected Description description;
         protected Result result;
         
         
         int stimulate() {
            rn.addFirstListener(rl);
            sut.run(rn);
            return n;
         }
         
         void aTestIsFinishedEitherWithSuccessOrFailure() {
            expect(2).on(new TestPadRunner(J.class));
            rl = new RunListener()
            {
               @Override
               public void testFinished(Description description) {
                  ++n;
               }
            };
         }
         
         void aTestIsStarting() {
            expect(2).on(new TestPadRunner(J.class));
            rl = new RunListener()
            {
               @Override
               public void testStarted(Description description) {
                  ++n;
               }              
            };
         }
         
         void aTestIsFaling() {
            expect(1).on(new TestPadRunner(J.class));
            rl = new RunListener()
            {
               @Override
               public void testFailure(Failure failure) throws Exception {
                  ++n;
               }               
            };
         }
         
         String aTestStarts() {
            expect(J.class.getSimpleName()).on(new TestPadRunner(J.class));
            rl = new RunListener() {

               @Override
               public void testRunStarted(Description d)
                  throws Exception {
                  description = d;
               }               
            };
            
            rn.addFirstListener(rl);
            sut.run(rn);
            
            return description.getClassName();
         }  
         
         String aTestFinishes() {
            expect("f:failure;count:2").on(new TestPadRunner(J.class));
            rl = new RunListener() {

               @Override
               public void testRunFinished(Result r) throws Exception {
                  result = r;
               }
               
            };
            
            rn.addFirstListener(rl);
            sut.run(rn);
            
            return "f:" + result.getFailures().get(0).getDescription().getMethodName() + ";count:" + result.getRunCount();
         }
      }
      
      class ShouldAllowFromExpections {
          RunNotifier rn = new RunNotifier();
          RunListener rl;
          protected Description description;
          protected Result result;
          private int n;
          private Class<?> target;
          
          
          int stimulate() {
        	 recorder.on(new TestPadRunner(target));
             rn.addFirstListener(rl);
             sut.run(rn);
             return n;
          }
          
          void fromFailure() {
             expect(1);
             target = FromFails.class;
             rl = new RunListener()
             {
                @Override
                public void testFailure(Failure failure) {
                	++n;
                }
             };
          }

          void fromSucceeds() {
              expect(0);
              target = FromSucceeds.class;
              rl = new RunListener()
              {
                 @Override
                 public void testFailure(Failure failure) {
                 	++n;
                 }
              };
           }
      }
   }
   
   @Ignore
   static class FromFails extends TestPad<Sample> {
	   void f() {
		   expect(0).from(1);
	   }
   }
   
   @Ignore
   static class FromSucceeds extends TestPad<Sample> {
	   void f() {
		   expect(0).from(0);
	   }
   }
   
   @Ignore
   static class J extends TestPad<Sample>
   {
      int failure() {
         expect(0);
         return 1;
      }
      
      int success() {
         expect(0);
         return 0;
      }
   }
   

   @Ignore
   public static class Sample {
      
   }
   
   class Sample_Tests extends TestPad<Sample>
   {
      class A {
         void f() { }
         void g() { }
      }
      
      @Ignore
      class B {
         void h() { }
      }
   }   
   
   class Ignoring extends TestPad<Sample> {
      @Ignore void ignoreMe() { }
      void considerMe() { }
   }
   
   class DefinesAnoynmousClass extends TestPad<Sample> {

      void f() {
         new Cloneable() {
            @SuppressWarnings("unused")
            void g() { }            
         };
      }
   }
   
   class A extends TestPad<Sample>
   {
      void x() { }
      void y() { }
      void z() { }
   }
}