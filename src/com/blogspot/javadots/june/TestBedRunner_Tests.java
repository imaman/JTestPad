package com.blogspot.javadots.june;

import org.junit.Ignore;
import org.junit.runner.Description;


public class TestBedRunner_Tests extends TestBed<TestBedRunner>
{
   class IsA_TestRunner
   {
      class shouldDetectTestMethods_When
      {
         int theyAreDeclaredInsideNestedClasses() {
            expect(2).on(new TestBedRunner(Sample_Tests.class));
            return sut.getDescription().getChildren().get(0).getChildren().size();
         }
         
         int theyAreAtTheTopLevel() {
            expect(3).on(new TestBedRunner(A.class));
            return sut.getDescription().getChildren().size();
         }
      }
      
      class shouldSkipOverWhen {
         int aAclasIsTaggedWithIgnore() {
            expect(1).on(new TestBedRunner(Sample_Tests.class));
            return sut.getDescription().getChildren().size();
         }
         
         int aMethodIsTaggedWithIgnore() {
            expect(1).on(new TestBedRunner(Ignoring.class));
            return sut.getDescription().getChildren().size();            
         }
         
         int theClassIsAnonymous() {
            expect(1).on(new TestBedRunner(DefinesAnoynmousClass.class));
            Description d = sut.getDescription();
            return d.getChildren().size();
         }
      }      
   }
   

   @Ignore
   public static class Sample {
      
   }
   
   class Sample_Tests extends TestBed<Sample>
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
   
   class Ignoring extends TestBed<Sample> {
      @Ignore void ignoreMe() { }
      void considerMe() { }
   }
   
   class DefinesAnoynmousClass extends TestBed<Sample> {

      void f() {
         new Cloneable() {
            @SuppressWarnings("unused")
            void g() { }            
         };
      }
   }
   
   class A extends TestBed<Sample>
   {
      void x() { }
      void y() { }
      void z() { }
   }
}
