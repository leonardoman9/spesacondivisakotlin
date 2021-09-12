package com.example.applicationprova.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.applicationprova.MainActivity
import com.example.applicationprova.R
import com.example.applicationprova.databinding.ActivityMyAccountBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

/**
 * Schermata  per modificare l'account utente
 */
class MyAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMyAccountBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_my_account
        )

        // Initialize Firebase Auth
        var auth = Firebase.auth
        val currentUser = auth.currentUser
        setSupportActionBar(binding.topAppBar)

        binding.updateNomeutente.setText(currentUser?.displayName.toString())
        binding.updateEmail.setText(currentUser?.email.toString())
        binding.cancel.setOnClickListener{
            onBackPressed()
            false
        }
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.updateButton.setOnClickListener {

            if (TextUtils.isEmpty(binding.updateNomeutente.text.toString())) {
                binding.updateNomeutente.setError("Nome Utente non puo essere vuoto")
                binding.updateNomeutente.requestFocus()
            } else if (TextUtils.isEmpty(binding.updateEmail.text.toString())) {
                binding.updateEmail.setError("Email non puo essere vuoto")
                binding.updateEmail.requestFocus()
            } else if (TextUtils.isEmpty(binding.updatePassword.text.toString())) {
                binding.updatePassword.setError("Password non puo essere vuoto")
                binding.updatePassword.requestFocus()
            } else if (binding.updatePassword.text.toString() != binding.UpdateConfirmPassword.text.toString()) {
                binding.UpdateConfirmPassword.setError("Password non corrisponde")
                binding.UpdateConfirmPassword.requestFocus()
            } else {
                currentUser?.updateEmail(binding.updateEmail.text.toString())
                currentUser?.updatePassword(binding.updatePassword.text.toString())

                val profileUpdates = userProfileChangeRequest {
                    displayName = binding.updateNomeutente.text.toString()
                }
                currentUser!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            Log.d("set name", "User profile updated.")
                        }
                    }


            }


        }
    }

    }

