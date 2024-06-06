package com.noatnoat.todoapp.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.noatnoat.todoapp.view.IntroFragment1
import com.noatnoat.todoapp.view.IntroFragment2
import com.noatnoat.todoapp.view.IntroFragment3
import com.noatnoat.todoapp.view.IntroFragment4

class OnboardingFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IntroFragment1()
            1 -> IntroFragment2()
            2 -> IntroFragment3()
            else -> IntroFragment4()
        }
    }
}