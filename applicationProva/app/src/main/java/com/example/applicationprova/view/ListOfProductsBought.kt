package com.example.applicationprova.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationprova.R
import com.example.applicationprova.databinding.ActivityListOfProductsBoughtBinding
import com.example.applicationprova.model.Prodotto
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class ListOfProductsBought : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var searchproducts: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        val list = mutableListOf<String>()
        val list2 = mutableListOf<Prodotto>()
        super.onCreate(savedInstanceState)
        //data binding al posto del classico inflate
        val binding: ActivityListOfProductsBoughtBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_list_of_products_bought
        )
        //setContentView(R.layout.activity_list_of_groups)



        database = FirebaseDatabase.getInstance("https://prova-14ff5-default-rtdb.europe-west1.firebasedatabase.app/")
        searchproducts= database.getReference("gruppi")


        val rv: RecyclerView = binding.listaProdottiComprati
        rv.layoutManager = LinearLayoutManager(this)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("key")
            val idshop = extras.getString("idshop")
            searchproducts.child(value.toString()).child("spese").child(idshop.toString()).child("prodotti").get().addOnSuccessListener {
                for (postSnapshot in it.children) {
                        list.add(postSnapshot.getValue().toString())
                    }

                for (prodotto in list) {
                    searchproducts.child(value.toString()).child("prodotti").child(prodotto).get().addOnSuccessListener {

                        val product: Prodotto = it.getValue<Prodotto>() as Prodotto
                        list2.add(product)
                        rv.adapter = ListOfProductBoughtAdapter(list2)
                    }
                                                }

                }






        }





    }


}