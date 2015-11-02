 package com.chinadaas.command.tools;
 
 public class ClsSystem
 {
   private static ClsSystem clsSystem = null;
 
   public static ClsSystem initialize()
   {
     if (clsSystem == null) {
       synchronized (ClsSystem.class) {
         if (clsSystem == null) {
           clsSystem = new ClsSystem();
         }
       }
     }
     return clsSystem;
   }
 
   public static String getClassesPath()
   {
     return initialize().getClass().getResource("/").getPath();
   }
 
   public static String getClassesParentPath()
   {
     return getClassesPath().replace("classes/", "");
   }
 }

