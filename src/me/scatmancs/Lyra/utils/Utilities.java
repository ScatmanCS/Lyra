package me.scatmancs.Lyra.utils;

public class Utilities {
 
 private static Utilities instance;	
 
 
 public static Utilities getInstance()
 {
    if (instance == null) {
      instance = new Utilities();
    }
    return instance;
 } 
 
 
	
}
