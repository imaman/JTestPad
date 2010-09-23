/**
 * 
 */
package com.blogspot.javadots.jtestpad;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Logger
{
   private PrintWriter pw;
   
   public static Logger inst = new Logger();
   
   private Logger() {
      try {
         pw  = new PrintWriter(new FileWriter(new File("c:/temp/tb.log")), true);
         println("===Started at ", new Date(), "======");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   public void println(Object... os) {
      String s = "";
      for(Object o : os)
         s += (s.isEmpty() ? "" : " ") + o;
      pw.println(s);
      pw.flush();
   }
}