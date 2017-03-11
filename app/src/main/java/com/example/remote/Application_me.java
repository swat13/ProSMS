package com.example.remote;

import android.app.Application;
import android.util.Log;


public class Application_me extends Application {


  @Override
  public void onCreate() {
    super.onCreate();

    Log.e("EEEEEEEEE", "FFFFFFFF");
    
	// Initialize the Parse SDK.
//    Parse.initialize(this, "O4KF2ILgjWnfESOpB546JAqOHB1ApEU3HOA4KjOy", "qMJ9RE0FtOOXLfo2ODVwm2BHhqJusFeWLxiRSxN3");


 		/*ParseUser.enableAutomaticUser();
 		ParseACL defaultACL = new ParseACL();
 	    
 		// If you would like all objects to be private by default, remove this line.
 		defaultACL.setPublicReadAccess(true);
 		
 		ParseACL.setDefaultACL(defaultACL, true);*/

  }
}