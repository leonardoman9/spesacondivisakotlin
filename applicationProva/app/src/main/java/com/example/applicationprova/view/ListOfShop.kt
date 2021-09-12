package com.example.applicationprova.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationprova.R
import com.example.applicationprova.model.Statistic
import com.example.applicationprova.databinding.ActivityListOfShopBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

/**
 * Activity che mostra le varie spese effettuate dal gruppo
 */
class ListOfShop : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var searchshop: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_shop)

        val nameShop = mutableListOf<String>()
        val whobuy = mutableListOf<String>()
        val price = mutableListOf<Float>()
        val idshop = mutableListOf<String>()

        //data binding al posto del classico inflate
        val binding: ActivityListOfShopBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_list_of_shop
        )
        //setContentView(R.layout.activity_list_of_groups)
        binding.empty.visibility = TextView.INVISIBLE
        var auth = Firebase.auth
        val currentUser = auth.currentUser


        database =
            FirebaseDatabase.getInstance("https://prova-14ff5-default-rtdb.europe-west1.firebasedatabase.app/")
        searchshop = database.getReference("gruppi")


        val rv: RecyclerView = binding.listaSpese




        rv.layoutManager = LinearLayoutManager(this)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
            true
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("key")

            searchshop.child(value.toString()).child("spese").get().addOnSuccessListener {
                for (postSnapshot in it.children) {
                    if (!postSnapshot.child("buy").getValue().toString().toBoolean()) {
                        nameShop.add(postSnapshot.child("nomespesa").getValue().toString())
                        whobuy.add(postSnapshot.child("nomeutente").getValue().toString())
                        price.add(postSnapshot.child("totale").getValue().toString().toFloat())
                        idshop.add(postSnapshot.key.toString())
                    }
                }
                if(nameShop.isEmpty()){
                    binding.empty.visibility = android.widget.TextView.VISIBLE
                }
                rv.adapter = ListOfShopAdapter(nameShop, whobuy, price, value.toString(),idshop)
                binding.spesaTotale.text=String.format("%.2f", price.sum())+"€"

                searchshop.child(value.toString()).child("gruppo").get().addOnSuccessListener {
                    binding.miaSpesa.text =String.format("%.2f", price.sum()/it.children.count())+"€"
                }



            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
                //  rv.adapter = ListofProductAdapter(list, list2)
            }


        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.listaspese
        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.listaprodotti -> {
                    val intent = Intent(this, ListOfProducts::class.java)
                    intent.putExtra("key", extras?.getString("key"))
                    startActivity(intent)
                    true
                }
                R.id.listaspese -> {
                    /*val intent = Intent(this, ListOfShop::class.java)
                    intent.putExtra("key", extras?.getString("key"))
                    startActivity(intent)
                    // Respond to navigation item 2 click

                     */
                    true
                }
                R.id.saldi -> {
                    val intent = Intent(this, Saldo::class.java)
                    intent.putExtra("key", extras?.getString("key"))
                    startActivity(intent)
                    // Respond to navigation item 2 click
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