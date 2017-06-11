package me.scatmancs.Lyra.utils;

import me.scatmancs.Lyra.keys.KeyManager;
import me.scatmancs.Lyra.listeners.StaffModeListener;

public class Utilities {
 
 private static Utilities instance;	
 private KeyManager keyManager; 
 private StaffModeListener staffModeListener;
 
 public static Utilities getInstance()
 {
    if (instance == null) {
      instance = new Utilities();
    }
    return instance;
 } 
 
 public KeyManager getKeyManager()
 {
     return this.keyManager;
 }
 
 public StaffModeListener getStaffModeListener()
 {
	 return this.staffModeListener;
 }
}
