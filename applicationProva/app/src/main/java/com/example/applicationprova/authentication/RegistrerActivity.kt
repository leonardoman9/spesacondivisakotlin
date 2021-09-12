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
import com.example.applicationprova.databinding.ActivityRegistrerBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

/**
 * Activity per effettuare la registrazione di un nuovo utente
 */
class RegistrerActivity : AppCompatActivity() {

    lateinit var Email: TextInputEditText
    lateinit var NomeUtente: TextInputEditText
    lateinit var Password: TextInputEditText
    lateinit var ConfirmPassword: TextInputEditText
    lateinit var goLogin: TextView
    lateinit var btnRegister: Button

    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        val binding: ActivityRegistrerBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_registrer)

        NomeUtente=binding.registerNomeutente
        Email=binding.registerEmail
        Password=binding.registerPassword
        ConfirmPassword=binding.registerConfirmPassword

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.registerButton.setOnClickListener{
            creteUser()
        }

        binding.navigateToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }

    /**
     * Aggiunge il nuovo utente al database, dopo vari controlli dei campi email, password, nome utente, conferma password
     */
    private fun creteUser(){
        if(TextUtils.isEmpty(Email.text.toString())){
            Email.setError("email non puo essere vuoto")
            Email.requestFocus()
        }
        else if(TextUtils.isEmpty(Password.text.toString())){
            Password.setError("Password non puo essere vuoto")
            Password.requestFocus()
        }
        else if(TextUtils.isEmpty(NomeUtente.text.toString())){
            NomeUtente.setError("Password non puo essere vuoto")
            NomeUtente.requestFocus()
        }
        else if(Password.text.toString() !=ConfirmPassword.text.toString()  ){
            ConfirmPassword.setError("Password non corrisponde")
            ConfirmPassword.requestFocus()
        }
        else{
            auth.createUserWithEmailAndPassword(Email.text.toString(), Password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("prova", "createUserWithEmail:success")
                        val user = auth.currentUser

                        val profileUpdates = userProfileChangeRequest {
                            displayName = NomeUtente.text.toString()
                        }

                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("set name", "User profile updated.")
                                }
                            }

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("prova", "createUserWithEmail:failure", task.exception)

                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }
        }


    }
}