package com.github.ebingelis.simplenbaapp.view

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import com.github.ebingelis.simplenbaapp.R
import com.github.ebingelis.simplenbaapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mainView: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playersButton = binding.playersButton
        val homeButton = binding.homeMainButton
        mainView = binding.mainView

        var currentFragment = HOME_FRAGMENT

        playersButton.setOnClickListener {

            if (currentFragment != PLAYERS_FRAGMENT) {
                currentFragment = PLAYERS_FRAGMENT
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container_view,
                    PlayersFragment.newInstance()
                )
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

                playersButton.setTypeface(null, Typeface.BOLD)
                homeButton.setTypeface(null, Typeface.NORMAL)
            }

        }

        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container_view,
            HomeFragment.newInstance()
        )
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

        homeButton.setOnClickListener {

            if (currentFragment != HOME_FRAGMENT) {
                currentFragment = HOME_FRAGMENT
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

    companion object {
        const val HOME_FRAGMENT = 0
        const val PLAYERS_FRAGMENT = 1
    }
}