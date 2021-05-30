package com.android.publisherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.android.publisherapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      val binding= ActivitySplashBinding.inflate(layoutInflater)
      setContentView(binding.root)
//        setContentView(R.layout.activity_splash)
      val backgroundImg = binding.ivLogo
      val sideAnimation = AnimationUtils.loadAnimation(this,R.anim.slide)
      backgroundImg.startAnimation(sideAnimation)

      Handler().postDelayed({
        startActivity(Intent(this,MainActivity::class.java))
        finish()
      },3000)
    }
}
