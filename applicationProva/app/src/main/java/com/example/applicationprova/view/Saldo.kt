package com.example.applicationprova.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.applicationprova.R
import com.example.applicationprova.model.Statistic
import com.example.applicationprova.databinding.ActivitySaldoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

/**
 * Activity che mostra il saldo complessivo del gruppo per le spese
 */
class Saldo : AppCompatActivity() {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://prova-14ff5-default-rtdb.europe-west1.firebasedatabase.app/")
    var searchUser: DatabaseReference = database.getReference("gruppi")
    val listUtenti = ArrayList<String>()
    val listNomiUtenti = ArrayList<String>()
    val listspese = mutableListOf<Float>()

    val listspese2 = mutableMapOf<String,Float>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivitySaldoBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_saldo
        )

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
            true
        }
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("key")

            searchUser.child(value.toString()).child("nomeGruppo").get()
                .addOnSuccessListener {
                    binding.Nomegruppo.setText("Utenti gruppo: ${it.value.toString()}")
                }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }

            searchUser.child(value.toString()).child("gruppo").get().addOnSuccessListener {

                for (postSnapshot in it.children) {

                    listUtenti.add(postSnapshot.value.toString())

                    listspese2.put(postSnapshot.key.toString(), 0.00f)


                    //listNomiUtenti.add(postSnapshot.value.toString())
                    //listspese.add(0.00f)

                }


                searchUser.child(value.toString()).child("spese").get().addOnSuccessListener {

                    for (postSnapshot in it.children) {
                        Log.d(
                            "map",
                            listspese2[postSnapshot.child("idutente").value.toString()].toString()
                        )
                        val spesa: Float? =
                            listspese2.get(postSnapshot.child("idutente").value.toString())
                        if (spesa != null) {
                            listspese2[postSnapshot.child("idutente").value.toString()] =
                                spesa + postSnapshot.child("totale").value.toString().toFloat()
                        }
                    }
                    binding.listaSaldi.isClickable = true
                    binding.listaSaldi.adapter = SaldoAdapter(this, listspese2, listUtenti)
                }


            }
            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigation.selectedItemId = R.id.saldi
            bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.listaprodotti -> {
                        val intent = Intent(this, ListOfProducts::class.java)
                        intent.putExtra("key", extras?.getString("key"))
                        startActivity(intent)
                        true
                    }
                    R.id.listaspese -> {
                        val intent = Intent(this, ListOfShop::class.java)
                        intent.putExtra("key", extras?.getString("key"))
                        startActivity(intent)
                        // Respond to navigation item 2 click
                        true
                    }
                    R.id.saldi -> {
                        /*val intent = Intent(this, Saldo::class.java)
                        intent.putExtra("key", extras?.getString("key"))
                        startActivity(intent)
                        // Respond to navigation item 2 click

                         */
                        true
                    }
                    R.id.statistiche -> {
                        val intent = Intent(this, Statistic::class.java)
                        intent.putExtra("key", extras?.getString("key"))
                        startActivity(intent)
                        // Respond to navigation item 2 click
                        true
                    }
                    else -> false
                }
            }
        }
}

}