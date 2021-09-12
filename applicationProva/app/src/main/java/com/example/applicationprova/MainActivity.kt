package com.example.applicationprova

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.applicationprova.authentication.LoginActivity
import com.example.applicationprova.databinding.ActivityMainBinding
import com.example.applicationprova.view.ListOfGroups
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Entry point main dell'applicazione
 */
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogOut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_main)
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.btnLogaut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Se l'utente è loggato, va alla lista dei gruppi dell'utente,
     * altrimenti va al login
     */
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
        else{

            val intent = Intent(this, ListOfGroups::class.java)
            startActivity(intent)

        }

    }
}
