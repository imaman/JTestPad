package com.blogspot.javadots.june;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class TestBedRunner extends Runner {
   
   private static int n = 0;

   private Description d;

   public static class Case {
      public final Class<?> c;
      public final Method m;
      private Description desc;
      public final Seq<Class<?>> seq;

      public Case(Seq<Class<?>> seq, Method m, Description desc) {
         this.c = seq.head;
         this.seq = seq;
         this.m = m;
         this.desc = desc;
      }
   }
   
   public class Seq<T> 
   {
      public final T head;
      public Seq<T> tail;
      
      public Seq(T head, Seq<T> tail) {
         this.head = head;
         this.tail = tail;
      }
      
      public Seq<T> prepend(T t) {
         return new Seq<T>(t, this);
      }
      
      public T last() {
         if(tail == null)
            return head;
         return tail.last();
      }
   }

   List<Case> ps = new ArrayList<Case>();
   private Map<String, String> translations = new HashMap<String, String>();

   protected void initTranslations() {
      translations.put("is", "=");
      translations.put("zero", "0");
      translations.put("one", "1");
      translations.put("two", "2");
      translations.put("three", "3");
      translations.put("four", "4");
      translations.put("five", "5");
      translations.put("six", "6");
      translations.put("seven", "7");
      translations.put("eight", "8");
      translations.put("nine", "9");
      translations.put("ten", "10");
   }

   public TestBedRunner(Class<?> cls) {
      initTranslations();
      Seq<Class<?>> seq = new Seq<Class<?>>(cls, null);
      d = process(seq);
   }

   private Description process(Seq<Class<?>> seq) {
      Class<?> c = seq.head;
      
      Description result = Description.createSuiteDescription(c.getSimpleName());
      for (Method m : c.getDeclaredMethods()) {
         if(Modifier.isStatic(m.getModifiers()))
            continue;
         
         if(m.getName().equals(stimulateMethodName()))
            continue;
         
         m.setAccessible(true);
         if (Modifier.isPrivate(m.getModifiers()))
            continue;

         Description subsub = newDescription(m, seq);
         result.addChild(subsub);
         ps.add(new Case(seq, m, subsub));
      }

      
      for(Class<?> childClass : c.getDeclaredClasses()) {
         Description newChild = process(seq.prepend(childClass));
         result.addChild(newChild);
      }
            
      return result;      
   }

   private Description newDescription(final Method m, final Seq<Class<?>> classes) {
      return Description.createTestDescription(m.getDeclaringClass(), m.getName());
   }

   private String nextOrdinal() {
      return "[" + ++n + "]";
   }

   protected String nameOf(Method m) {
      String name = m.getName();
      return nextOrdinal() + " " + normalize(MessageBuilder.decamel(name));
   }

   
   protected String nameOf(Class<?> c) {
      String name = c.getSimpleName();
      if (!name.startsWith(prefix()))
         return normalize(MessageBuilder.decamel(name));

      name = name.substring(prefix().length());
      return "Feature: " + normalize(MessageBuilder.decamel(name));
   }

   private String normalize(List<String> words) {

      StringBuilder sb = new StringBuilder();
      int top = words.size();
      for (int i = 0; i < top; ++i) {
         String curr = words.get(i);
         String next = null;
         if (i < top - 1)
            next = words.get(i + 1);

         if ("than".equalsIgnoreCase(next)) {
            if ("less".equalsIgnoreCase(curr)) {
               concat(sb, "<");
               ++i;
               continue;
            }
         }

         String temp = translations.get(curr.toLowerCase());
         if (temp != null)
            curr = temp;

         concat(sb, curr);
      }

      return sb.toString();
   }

   private static StringBuilder concat(StringBuilder sb, String s) {
      sb.append(sb.length() > 0 ? " " : "").append(s);
      return sb;

   }

   protected String prefix() {
      return "Feature_";
   }

   @Override
   public Description getDescription() {
      return d;
   }

   @Override
   public void run(RunNotifier rn) {
      for (Case p : ps) {
         try {
            rn.fireTestStarted(p.desc);
            run(p, rn);
            rn.fireTestFinished(p.desc);
         } catch (AssertionError e) {
            rn.fireTestFailure(new Failure(p.desc, e));
         } catch (InvocationTargetException e) {
            rn.fireTestFailure(new Failure(p.desc, e.getTargetException()));
         }
         catch (Exception e) {
            rn.fireTestFailure(new Failure(p.desc, e));
         }
      }
   }

   private void run(Case p, RunNotifier rn) throws Exception {
      Method m = p.m;
      Seq<Object> instances = instantiate(p.seq);
      Object oo = instances.head;
      Object o = instances.head;
      for(Seq<Object> temp = instances; temp != null; temp = temp.tail)
         o = temp.head;
            
      verifyNormalExecution(m, oo, (TestBed<?>) o, instances);
   }

   private Seq<Object> instantiate(Seq<Class<?>> seq) throws Exception {
      if(seq.tail == null)
         return new Seq<Object>(seq.head.newInstance(), null);
      
      Seq<Object> enclosingInstances = instantiate(seq.tail);
      Constructor<?> ctor = seq.head.getDeclaredConstructor(enclosingInstances.head.getClass());
      ctor.setAccessible(true);
      
      Object newInstance = ctor.newInstance(enclosingInstances.head);
      return enclosingInstances.prepend(newInstance);
      
   }

   private void verifyException(Method m, Object oo, TestBed<?> o, Throwable thrown) {
      Class<?> exceptionClass = (Class<?>) o.expected;
      if(thrown == null)
         throw new AssertionError(new MessageBuilder(m, "Expected exception "
            + exceptionClass.getName() + " but nothing was thrown").message());

      if (exceptionClass.isAssignableFrom(thrown.getClass())) {
         if(o.exceptionMessage == null)
            return;
            
         if(o.exceptionMessage.equals(thrown.getMessage()))
            return;
         
         
         throw new ComparisonFailure(new MessageBuilder(m, "Exception message mismatch").message(), 
            o.exceptionMessage, thrown.getMessage());
            
      }

      throw new AssertionError(new MessageBuilder(m, "Expected exception "
         + exceptionClass.getName() + " but got " + thrown.getClass().getName()).message());
   }

   private void verifyNormalExecution(Method m, Object oo, TestBed<?> o, 
      Seq<Object> instances) throws Exception 
   {
      Object actual = null;
      Throwable thrown = null;
      try {
         actual = m.invoke(oo);
         if(void.class.equals(m.getReturnType()))
            actual = stimulate(instances);
      }
      catch(InvocationTargetException e) {
         thrown = e.getTargetException();
      }
      
      boolean b = o.expected instanceof Class<?>;
      b = b && Throwable.class.isAssignableFrom((Class<?>) (o.expected));
      if(b)
      {
         verifyException(m, oo, o, thrown);
         return;
      }
      
      
      try {
         Assert.assertEquals(new MessageBuilder(m, "").message(), o.expected, actual);
      } catch (AssertionError e) {
         e.setStackTrace(getStackTrace(o));
         throw e;
      }
   }
   
   private Method findMethod(Class<?> c, String name) {
      for(Method m : c.getDeclaredMethods()) {
         if(!m.getName().equals(name))
            continue;
         
         m.setAccessible(true);
         return m;         
      }
      
      return null;
   }
   
   public class MethodAndObject
   {
      
   }

   private Object stimulate(Seq<Object> objects) throws Exception {
      for(Seq<Object> curr = objects; curr != null; curr = curr.tail) {
         Object o = curr.head;
         Method m = findMethod(o.getClass(), stimulateMethodName());
         if(m != null)
            return m.invoke(o);            
      }
      
      return null;
   }
   

   protected String stimulateMethodName() {
      return "stimulate";
   }

   private StackTraceElement[] getStackTrace(TestBed<?> o) {
      StackTraceElement[] arr = new StackTraceElement[o.trace.length - 1];
      for (int i = 0; i < arr.length; ++i)
         arr[i] = o.trace[i + 1];
      return arr;
   }
}
