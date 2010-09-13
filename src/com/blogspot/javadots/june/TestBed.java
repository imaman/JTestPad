package com.blogspot.javadots.june;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestBedRunner.class)
public class TestBed<T> {

   @Test
   public void __some_very_stranGeName_43904mn239()
   {
      // Make sure plug-in sees subclasses as JUnit test classes
   }

   
   protected Class<?> tested;
   protected T sut;
   protected Object expected;
   protected StackTraceElement[] trace;
   protected String exceptionMessage;

   
   public class A 
   {

      public A(Object expected) {
         this(expected, null);
      }

      public A(Object expected, String msg) {
         TestBed.this.expected = expected;
         TestBed.this.exceptionMessage = msg;         
      }

      public<S extends T> S on(S t) {
         tested = t.getClass();
         sut = t;
         
         return t;
      }
   }
   
   public A expect(Object expected) {
      trace = new Exception().getStackTrace();
      return new A(expected);
   }

   public A expect(Class<? extends Throwable> t, String msg) {
      trace = new Exception().getStackTrace();
      return new A(t, msg);
   }
   
   public void problem(Exception e) {
      
      RuntimeException re = new RuntimeException(e);
      re.setStackTrace(e.getStackTrace());
      throw re;
   }
   
   
   
}
