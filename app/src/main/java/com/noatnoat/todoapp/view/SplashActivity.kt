package com.noatnoat.todoapp.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.noatnoat.musicapp.databinding.ActivitySplashBinding
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import com.airbnb.lottie.LottieAnimationView
import com.noatnoat.musicapp.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 10000
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val animationView = findViewById<LottieAnimationView>(R.id.animationView)
        animationView.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                finish()
            }
        })
    }
}