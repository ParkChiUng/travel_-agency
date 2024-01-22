//package com.sessac.travel_agency
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import androidx.appcompat.app.AppCompatActivity
//
//
//@SuppressLint("CustomSplashScreen")
//class SplashActivity : AppCompatActivity() {
//
//    private val SPLASH_DELAY: Long = 2000 // 2 seconds
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//
//        // Use a Handler to delay the transition to the main activity
//        Handler().postDelayed({
//            // Start the main activity after the delay
//            val intent = Intent(this@SplashActivity, MainActivity::class.java)
//            startActivity(intent)
//            finish() // Finish the splash activity to prevent going back
//        }, SPLASH_DELAY)
//    }
//}