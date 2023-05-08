package com.github.ebingelis.simplenbaapp.view

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.github.ebingelis.simplenbaapp.R
import com.github.ebingelis.simplenbaapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playersButton = binding.playersButton
        val homeButton = binding.homeMainButton

        playersButton.setOnClickListener {

            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container_view,
                PlayersFragment.newInstance()
            )
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

            playersButton.setTypeface(null, Typeface.BOLD)
            homeButton.setTypeface(null, Typeface.NORMAL)

        }

        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container_view,
            HomeFragment.newInstance()
        )
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

        homeButton.setOnClickListener {

            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container_view,
                HomeFragment.newInstance()
            )
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

            playersButton.setTypeface(null, Typeface.NORMAL)
            homeButton.setTypeface(null, Typeface.BOLD)
        }
    }
}