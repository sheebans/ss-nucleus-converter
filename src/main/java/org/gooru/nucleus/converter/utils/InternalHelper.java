package org.gooru.nucleus.converter.utils;

public class InternalHelper {

   public static String replaceSpecialCharWithUnderscore(String name) { 
     return name.replaceAll("[^a-zA-Z0-9]+","_");
   }
}
