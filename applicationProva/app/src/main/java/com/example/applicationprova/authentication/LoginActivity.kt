package com.example.applicationprova.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.applicationprova.MainActivity
import com.example.applicationprova.R
import com.example.applicationprova.databinding.LoginActivityBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Classe che si occupa del login dell'utente
 */
class LoginActivity : AppCompatActivity() {


    lateinit var Email: TextInputEditText
    lateinit var Password: TextInputEditText
    lateinit var goRegister: TextView
    lateinit var btnLogin: Button

    lateinit var auth : FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: LoginActivityBinding = DataBindingUtil.setContentView(
            this, R.layout.login_activity
        )

        Email=binding.loginEmail
        Password=binding.loginPassword
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.loginButton.setOnClickListener{
            loginUser()
        }
        binding.navigateToRegister.setOnClickListener{
            val intent = Intent(this, RegistrerActivity::class.java)
            startActivity(intent)
        }


    }

    /**
     * Login dell'utente, con vari controlli per i campi email e password
     */

    private fun loginUser(){

        if(TextUtils.isEmpty(Email.text.toString())){
            Email.setError("email non puo essere vuoto")
            Email.requestFocus()
        }
        else if(TextUtils.isEmpty(Password.text.toString())){
            Password.setError("Password non puo essere vuoto")
            Password.requestFocus()
        }
        else{
            auth.signInWithEmailAndPassword(Email.text.toString(), Password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("prova", "signInWithEmail:success")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("prova", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }



        }

    }


    }
