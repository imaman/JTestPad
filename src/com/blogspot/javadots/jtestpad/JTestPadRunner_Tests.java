package com.blogspot.javadots.jtestpad;

import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;


public class JTestPadRunner_Tests extends JTestPad<JTestPadRunner>
{
   class GeneratesHierarchicalErrorMessagesWhenFails {
      protected Failure failure;

      String stimulate() {
         RunNotifier rn = new RunNotifier();
         rn.addFirstListener(new RunListener() {

            @Override
            public void testFailure(Failure f) throws Exception {
               failure = f;
            }            
         });
         
         sut.run(rn);
         return failure.getMessage();
      }
      
      void withStackoverflow() {
         expectThat(Matchers.notNullValue()).on(new JTestPadRunner(_Outer.class));
      }
   }
   
   @Ignore
   static class _Outer extends JTestPad<_Sample> {
      
      class _Inner {
         
         void testMethod() {
            testMethod(); // Explode the stack
         }
      }
   }
   
   class IsA_TestRunner
   {
      class shouldDetectTestMethods_When
      {
         int theyAreDeclaredInsideNestedClasses() {
            expect(2).on(new JTestPadRunner(_Sample_Tests.class));
            return sut.getDescription().getChildren().get(0).getChildren().size();
         }
         
         int theyAreAtTheTopLevel() {
            expect(3).on(new JTestPadRunner(_A.class));
            return sut.getDescription().getChildren().size();
         }
      }
      
      class shouldSkipOverWhen {
         int aClassIsTaggedWithIgnore() {
            expect(1).on(new JTestPadRunner(_Sample_Tests.class));
            return sut.getDescription().getChildren().size();
         }
         
         int aMethodIsTaggedWithIgnore() {
            expect(1).on(new JTestPadRunner(_Ignoring.class));
            return sut.getDescription().getChildren().size();            
         }
         
         int theClassIsAnonymous() {
            expect(1).on(new JTestPadRunner(_DefinesAnoynmousClass.class));
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
            expect(2).on(new JTestPadRunner(_J.class));
            rl = new RunListener()
            {
               @Override
               public void testFinished(Description description) {
                  ++n;
               }
            };
         }
         
         void aTestIsStarting() {
            expect(2).on(new JTestPadRunner(_J.class));
            rl = new RunListener()
            {
               @Override
               public void testStarted(Description description) {
                  ++n;
               }              
            };
         }
         
         void aTestIsFaling() {
            expect(1).on(new JTestPadRunner(_J.class));
            rl = new RunListener()
            {
               @Override
               public void testFailure(Failure failure) throws Exception {
                  ++n;
               }               
            };
         }
         
         String aTestStarts() {
            expect(_J.class.getSimpleName()).on(new JTestPadRunner(_J.class));
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
            expect("f:failure;count:2").on(new JTestPadRunner(_J.class));
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
             recorder.on(new JTestPadRunner(target));
             rn.addFirstListener(rl);
             sut.run(rn);
             return n;
          }
          
          void fromFailure() {
             expect(1);
             target = _FromFails.class;
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
              target = _FromSucceeds.class;
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
   static class _FromFails extends JTestPad<_Sample> {
	   void f() {
		   expect(0).from(1);
	   }
   }
   
   @Ignore
   static class _FromSucceeds extends JTestPad<_Sample> {
	   void f() {
		   expect(0).from(0);
	   }
   }
   
   @Ignore
   static class _J extends JTestPad<_Sample>
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
   public static class _Sample {
      
   }
   
   @Ignore
   class _Sample_Tests extends JTestPad<_Sample>
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
   
   @Ignore   
   class _Ignoring extends JTestPad<_Sample> {
      @Ignore void ignoreMe() { }
      void considerMe() { }
   }
   
   @Ignore
   class _DefinesAnoynmousClass extends JTestPad<_Sample> {

      void f() {
         new Cloneable() {
            @SuppressWarnings("unused")
            void g() { }            
         };
      }
   }
   
   @Ignore
   class _A extends JTestPad<_Sample>
   {
      void x() { }
      void y() { }
      void z() { }
   }
}
