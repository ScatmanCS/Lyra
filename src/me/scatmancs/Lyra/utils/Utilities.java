package me.scatmancs.Lyra.utils;

import java.util.Random;

public class Utilities {
 
 private static Utilities instance;	
 private Random random = new Random();
 
 public static Utilities getInstance()
 {
    if (instance == null) {
      instance = new Utilities();
    }
    return instance;
 } 

 public Random getRandom()
 {
   return this.random;
 }
}
