package com.example.lecturer.view.ui

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.databasework.R
import com.example.databasework.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpToolbar()

        setUpNavigationComponents()





        // Navigating between fragments using bottom navigation
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_lectures -> navController.navigate(R.id.lectures)
                R.id.nav_quiz -> {
                    navController.navigate(R.id.quizzesFragment)
                }
                R.id.nav_home -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.nav_questions -> navController.navigate(R.id.quizzesFragment)
            }
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                   changeToolbar(0)

                }
                R.id.lectures -> {
                   changeToolbar(1)
                }
                R.id.quizzesFragment -> {
                   changeToolbar(2)
                }
                R.id.questionsFragment -> {
                   changeToolbar(3)
                }
                R.id.videoFragment -> {
                    binding.bottomNav.isVisible = false
                }

                R.id.choiceFragment -> {
                    binding.bottomNav.isVisible = false
                }

            }
        }

    }


    override fun onBackPressed() {
        finish()
        //super.onBackPressed()
    }

    override fun onNavigateUp(): Boolean {
        return super.onNavigateUp()
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = this.findNavController(R.id.flFragments)
        return navController.navigateUp()
    }


    private fun setUpToolbar() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar!!.setHomeButtonEnabled(true)

        binding.mainToolbar.setNavigationOnClickListener {
            val dialog = AlertDialog.Builder(this)

            dialog.apply {
                setTitle("Change Subject?")
                setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                    navController.navigate(R.id.choiceFragment)
                })

                setNegativeButton("Stay") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    dialogInterface.cancel()
                }
            }.show()


        }
    }

    private fun setUpNavigationComponents(){
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.flFragments) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    private fun changeToolbar(index: Int){
        binding.bottomNav.menu[index].isChecked = true
        binding.bottomNav.isVisible = true
        supportActionBar!!.title = ""
    }

}