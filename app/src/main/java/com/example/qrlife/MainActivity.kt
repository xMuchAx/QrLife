package com.example.qrlife

import HomePageFragment
import ListQrPageFragment
import ScanPageFragment
import UserPageFragment
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
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
        binding.homePage.setOnClickListener { loadFragment(HomePageFragment())

            it.isSelected = true
            binding.qrPage.isSelected = false
            binding.scanPage.isSelected = false
            binding.userPage.isSelected = false

        }

        binding.qrPage.setOnClickListener {

            loadFragment(ListQrPageFragment())

            it.isSelected = true
            binding.homePage.isSelected = false
            binding.scanPage.isSelected = false
            binding.userPage.isSelected = false

        }

        binding.scanPage.setOnClickListener {

            loadFragment(ScanPageFragment())

            it.isSelected = true
            binding.homePage.isSelected = false
            binding.qrPage.isSelected = false
            binding.userPage.isSelected = false

        }

        binding.userPage.setOnClickListener {

            loadFragment(UserPageFragment())

            it.isSelected = true
            binding.homePage.isSelected = false
            binding.qrPage.isSelected = false
            binding.scanPage.isSelected = false


        }




    }




    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
