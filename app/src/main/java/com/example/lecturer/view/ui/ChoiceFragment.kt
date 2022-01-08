package com.example.lecturer.view.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.databasework.R
import com.example.databasework.databinding.FragmentChoiceBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


class ChoiceFragment : Fragment(R.layout.fragment_choice), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentChoiceBinding
    private var subjectsArray: Int = -1
    private lateinit var year: String
    private lateinit var subjectName: String
    private lateinit var auth: FirebaseAuth
    private val registerAuth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            account.let {
                googleAuthForFirebase(it)
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choice, container, false)

        if (auth.currentUser != null) {
            changeLayout()
        }else {
            binding.signInBtn.setOnClickListener {
                signIn()
            }
        }

        binding.button.setOnClickListener {

            findNavController().navigate(
                ChoiceFragmentDirections.actionChoiceFragmentToHomeFragment(
                    year,
                    subjectName
                )
            )
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setYearsSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.years,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerYear.adapter = adapter
            binding.spinnerYear.onItemSelectedListener = this
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (parent == binding.spinnerYear) {

            when (parent.selectedItemPosition) {
                0 -> {
                    subjectsArray = R.array.first_year_subjects
                    year = "year_first"
                }
                1 -> {
                    subjectsArray = R.array.second_year_subjects
                    year = "year_second"
                }

                2 -> {
                    subjectsArray = R.array.third_year_cs_subjects
                    year = "year_third"
                }
                3 -> {
                    subjectsArray = R.array.third_year_cs_subjects
                    year = "year_fourth"
                }
            }

            setSubjectsSpinner()
        } else if (parent == binding.spinnerSubject) {
            subjectName = parent.selectedItem.toString()

            binding.button.isEnabled = true
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    private fun setSubjectsSpinner() {
        Log.d("array", subjectsArray.toString())
        binding.spinnerSubject.isFocusable = true
        binding.spinnerSubject.isClickable = true
        ArrayAdapter.createFromResource(
            requireContext(),
            subjectsArray,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSubject.adapter = it
            binding.spinnerSubject.onItemSelectedListener = this
        }
    }


    // Sign in Methods

    private fun signIn() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.clientId))
            .requestEmail()
            .build()

        val mSignInClient = GoogleSignIn.getClient(requireActivity(), options)

        mSignInClient.signInIntent.also {
            registerAuth.launch(it)
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main) {
                    Snackbar.make(
                        parentFragment!!.requireView(),
                        "Welcome ${account.displayName}",
                        Snackbar.LENGTH_LONG
                    ).show()
                    changeLayout()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Snackbar.make(
                        parentFragment!!.requireView(),
                        e.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    private fun changeLayout() {
        binding.loginLayout.visibility = View.GONE
        binding.choiceLayout.visibility = View.VISIBLE
        setYearsSpinner()


    }


}