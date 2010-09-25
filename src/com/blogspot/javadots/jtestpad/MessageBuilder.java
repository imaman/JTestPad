package com.blogspot.javadots.jtestpad;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MessageBuilder {
   private final List<String> prefix;
   public Method m;
   public String s;
   private StringBuilder sb = new StringBuilder();
   int i = 0;
   private final Object EOL = new Object();
   
   List<Object> x = new ArrayList<Object>();

   public MessageBuilder(Method m, String s) {
      this(m, s, Arrays.asList("Behavior", "of"));
   }
   public MessageBuilder(Method m, String s, List<String> prefix) {
      this.prefix = prefix;
      this.m = m;
      this.s = s;
      LinkedList<List<String>> lifo = new LinkedList<List<String>>();
      lifo.addFirst(decamel(m.getName()));
      
      for(Class<?> c = m.getDeclaringClass(); c != null; 
         c = c.getDeclaringClass())
      {
         if(c.getSuperclass().equals(JTestPad.class))
            lifo.addFirst(nameOfTopLevelClass(c));
         else
            lifo.addFirst(decamel(c.getSimpleName()));
      }
      
      
      for(List<String> curr : lifo) {
         x.addAll(curr);
         x.add(EOL);
      }
      
      parse();
   }
   
   private List<String> nameOfTopLevelClass(Class<?> c) {
      ParameterizedType spr = (ParameterizedType) c.getGenericSuperclass();
      Type t = spr.getActualTypeArguments()[0];
      
      if(!(t instanceof Class<?>)) 
         return decamel(c.getSimpleName());
      
      List<String> result = new ArrayList<String>();
      result.addAll(prefix);

      Class<?> testee = (Class<?>) t;
      result.add(testee.getSimpleName());
      result.add("(" + testee.getName() + ")");
      
      return result;
   }

   private Object curr() {
      return x.get(i); 
   }
   
   private void consume() {
      ++i;
   }
   
   void parse() {
      while(true) {
         if(endOfInput())
            break;
         sb.append("\n");
         line();
      }
   }
   
   void line() {
      int pos = 0;
      while(true) {
         if(endOfInput())
            return;
      
         Object o = curr();
         consume();
         if(o == EOL)
            return;
                  
         if(pos > 0)
            sb.append(" ");
         ++pos;
         sb.append(o);
         
         if(isWhen(o))
            when();
      }
   }
   
   public void when() {
      if(endOfInput())
         return;
      
      sb.append(": ");
      line();
      sb.append(" ");
      line();
      sb.append("\n");
   }
   
   private boolean isWhen(Object o) {
      return "when".equals(o.toString().toLowerCase());
   }

   private boolean endOfInput() {
      return i >= x.size();
   }

   public String message() {
      return sb.toString();
   }

   static List<String> decamel(String s) {
      List<String> result = new ArrayList<String>();
      int len = s.length();
      boolean prevWasCapital = true;
      StringBuilder current = new StringBuilder();
      for (int i = 0; i < len; ++i) {
         char c = s.charAt(i);
         boolean isCapital = Character.isUpperCase(c);
         if (isCapital && !prevWasCapital) {
            result.add(current.toString());
            current = new StringBuilder();
         }
   
         prevWasCapital = isCapital;
   
         if (c == '_') {
            result.add(current.toString());
            current = new StringBuilder();
            prevWasCapital = true;
            continue;
         }
   
         current.append(c);
      }
   
      result.add(current.toString());
      return result;
   }
}