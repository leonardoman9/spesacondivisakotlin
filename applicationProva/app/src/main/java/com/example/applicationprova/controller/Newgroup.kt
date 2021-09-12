package com.example.applicationprova.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.applicationprova.MainActivity
import com.example.applicationprova.R
import com.example.applicationprova.databinding.ActivityNewgroupBinding
import com.example.applicationprova.model.Gruppo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

/**
 * Activity per creare un nuovo gruppo,
 * aggiungendo 0+ utenti
 */
class Newgroup : AppCompatActivity() {

    var groupid: String="prova"
    var groupid2: String="prova"

    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityNewgroupBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_newgroup
        )

        val list = mutableMapOf<String,String>()

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
            true
        }
        database = FirebaseDatabase.getInstance("https://prova-14ff5-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef = database.getReference("gruppi")
        var myRefutenti = database.getReference("utentiGruppi")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                if (dataSnapshot.exists())
                   groupid2= dataSnapshot.key.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("prova", "Failed to read value.", error.toException())
            }
        })

        binding.agg.setOnClickListener{
            if(TextUtils.isEmpty(binding.aggiungimembro.text.toString())){
                binding.aggiungimembro.setError("email non puo essere vuoto")
                binding.aggiungimembro.requestFocus()
            }
            else if(TextUtils.isEmpty(binding.Nomemembro.text.toString())){
                binding.Nomemembro.setError("Nome non puo essere vuoto")
                binding.Nomemembro.requestFocus()
            }
            else {
                if (binding.CardView.getVisibility() == View.INVISIBLE) {
                    binding.CardView.setVisibility(View.VISIBLE);
                }
                list.put(binding.aggiungimembro.text.toString().replace(".","'"), binding.Nomemembro.text.toString())
                binding.nomeUtente.append(binding.Nomemembro.text.toString() + System.getProperty("line.separator"))
                binding.componenti.append(binding.aggiungimembro.text.toString() + System.getProperty("line.separator"))
                binding.aggiungimembro.setText("")
                binding.Nomemembro.setText("")
            }
        }

        binding.creaGruppo.setOnClickListener{

            val auth = Firebase.auth
            val currentUser = auth.currentUser
            list.put(currentUser?.email.toString().replace(".","'"),currentUser?.displayName.toString())
            val group = Gruppo(binding.Nomegruppo.text.toString(),list)


            groupid= myRef.push().key.toString()
            myRef.child(groupid).setValue(group).addOnSuccessListener {

                list.forEach{
                    myRefutenti.child(it.key.replace("'","")).child(groupid).setValue(binding.Nomegruppo.text.toString())
                    Log.d("email",currentUser?.email.toString())
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }

        }

    }



}