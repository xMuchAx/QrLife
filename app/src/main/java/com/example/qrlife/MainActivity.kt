package com.example.qrlife

import AnimatedFragment
import HomePageFragment
import ListQrPageFragment
import ScanPageFragment
import UserPageFragment
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qrlife.databinding.MainActivityBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Chargez le premier fragment par défaut (par exemple, Fragment1)
        loadFragment(HomePageFragment())
        binding.homePage.isSelected = true



        // Définir des écouteurs de clic pour chaque onglet
        binding.homePage.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

            if (currentFragment is AnimatedFragment) {
                // Appellez la méthode dans le fragment
                currentFragment.startFragmentEnterAnimation()
            }

            Handler().postDelayed({

                loadFragment(HomePageFragment())


                it.isSelected = true
                binding.qrPage.isSelected = false
                binding.scanPage.isSelected = false
                binding.userPage.isSelected = false

            }, 550)

        }

        binding.qrPage.setOnClickListener {

            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

            if (currentFragment is AnimatedFragment) {
                // Appellez la méthode dans le fragment
                currentFragment.startFragmentEnterAnimation()
            }
            Handler().postDelayed({

                loadFragment(ListQrPageFragment())

                // Obtenez une référence au fragment actuellement attaché

                it.isSelected = true
                binding.homePage.isSelected = false
                binding.scanPage.isSelected = false
                binding.userPage.isSelected = false
            }, 550)
        }



        binding.scanPage.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

            if (currentFragment is AnimatedFragment) {
                // Appellez la méthode dans le fragment
                currentFragment.startFragmentEnterAnimation()
            }

            Handler().postDelayed({

                loadFragment(ScanPageFragment())

                it.isSelected = true
                binding.homePage.isSelected = false
                binding.qrPage.isSelected = false
                binding.userPage.isSelected = false

            }, 550)

        }

        binding.userPage.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

            if (currentFragment is AnimatedFragment) {
                // Appellez la méthode dans le fragment
                currentFragment.startFragmentEnterAnimation()
            }

            Handler().postDelayed({

                loadFragment(UserPageFragment())

                it.isSelected = true
                binding.homePage.isSelected = false
                binding.qrPage.isSelected = false
                binding.scanPage.isSelected = false

            }, 550)


        }




    }




    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
