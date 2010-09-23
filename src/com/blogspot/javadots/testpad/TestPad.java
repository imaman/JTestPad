package com.blogspot.javadots.testpad;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestPadRunner.class)
public class TestPad<T> {

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

   protected Recorder<?> recorder;
   
   public class Recorder<R> 
   {
      Object actual;
      private boolean hasActual = false;

      public Recorder(Object expected) {
         this(expected, null);
      }

      public Recorder(Object expected, String msg) {
         TestPad.this.expected = expected;
         TestPad.this.exceptionMessage = msg;         
      }

      public<S extends T> S on(S t) {
         tested = t.getClass();
         sut = t;
         
         return t;
      }
      
      public<V extends R> void from(V actual) {
    	  this.actual = actual;
    	  hasActual = true;
      }

      public boolean hasActual() {
         return hasActual;
      }
   }
   

   public Recorder<Object> expect(Class<? extends Throwable> t, String msg) {
      trace = new Exception().getStackTrace();
      Recorder<Object> result = new Recorder<Object>(t, msg);
      recorder = result;
      return result;
   }

   public Recorder<Object> expect(Class<? extends Throwable> t) {
      return expect(t, null);
   }
   
   public<R> Recorder<R> expect(R expected) {
      trace = new Exception().getStackTrace();
      Recorder<R> result = new Recorder<R>(expected);
      recorder = result;
      return result;
   }
   
   
   public void problem(Exception e) {
      
      RuntimeException re = new RuntimeException(e);
      re.setStackTrace(e.getStackTrace());
      throw re;
   }
   
   
   
}
