package com.example.lecturer.view.ui

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.databasework.R
import com.example.databasework.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpToolbar()

        setUpNavigationComponents()

        binding.userImage.setOnClickListener{
            signOut()
        }

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
                R.id.nav_questions -> navController.navigate(R.id.questionsFragment)
            }
            true
        }

        // Whenever the fragment is changed
        // I can get the current fragment id from here
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    changeToolbar(0, true)
                    val account = FirebaseAuth.getInstance().currentUser
                    Glide.with(this)
                        .load(account!!.photoUrl)
                        .into(binding.userImage)

                }
                R.id.lectures -> {
                    changeToolbar(1, false)

                }
                R.id.quizzesFragment -> {
                    changeToolbar(2, false)
                }
                R.id.questionsFragment -> {
                    changeToolbar(3, false)
                }
                R.id.videoFragment -> {
                    binding.bottomNav.isVisible = false
                    binding.mainTolbar.visibility = View.GONE
                }

                R.id.choiceFragment -> {
                    binding.bottomNav.isVisible = false
                    binding.toolbarTitle.visibility = View.GONE
                    binding.mainTolbar.visibility = View.GONE

                }
                R.id.quizFragment -> {
                    binding.bottomNav.isVisible = false
                    binding.mainTolbar.visibility = View.GONE

                }

            }
        }



    }

    private fun signOut() {
        AlertDialog.Builder(this)
            .setTitle("Sign out")
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Sign out") {dialog, _ ->
                FirebaseAuth.getInstance().signOut()
                dialog.dismiss()
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0)

            }
            .setNegativeButton("No, thanks") {dialog, _ ->
                dialog.dismiss()
            }.show()

    }


    override fun onBackPressed() {
        if (navController.currentDestination!!.id != R.id.videoFragment) {
            AlertDialog.Builder(this)
                .setTitle("Are you sure you want to leave Lecturer?")
                .setPositiveButton("Yes") { dialog, _ ->
                    dialog.cancel()
                    finish()
                }
                .setNegativeButton("Stay") { dialog, _ ->
                    dialog.dismiss()
                    dialog.cancel()
                }.show()

        } else {
            super.onBackPressed()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        navController = this.findNavController(R.id.flFragments)
        return navController.navigateUp()
    }


    private fun setUpToolbar() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        binding.mainToolbar.setNavigationOnClickListener {
            navigateToChoiceFragment()
        }

        binding.toolbarTitle.setOnClickListener {
            navigateToChoiceFragment()
        }
    }

    fun navigateToChoiceFragment() {
        val dialog = AlertDialog.Builder(this)

        dialog.apply {
            setTitle("Change Subject?")
            setPositiveButton("Yes") { _, _ ->
                navController.navigate(R.id.choiceFragment)
            }

            setNegativeButton("Stay") { dialogInterface, _ ->
                dialogInterface.dismiss()
                dialogInterface.cancel()
            }
        }.show()


    }

    private fun setUpNavigationComponents() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.flFragments) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    private fun changeToolbar(index: Int, enableToolbar: Boolean) {
        binding.bottomNav.menu[index].isChecked = true
        binding.bottomNav.isVisible = true
        supportActionBar!!.title = ""
        if (enableToolbar) {
            binding.mainTolbar.visibility = View.VISIBLE
        } else {
            binding.mainTolbar.visibility = View.GONE
        }

        binding.toolbarTitle.visibility = View.VISIBLE
    }
}



