package com.noatnoat.todoapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.noatnoat.musicapp.R
import com.noatnoat.musicapp.databinding.ActivityOnboardingBinding
import com.noatnoat.todoapp.view.adapter.OnboardingFragmentAdapter

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)

        binding.viewPager.adapter = OnboardingFragmentAdapter(this)
    }
}