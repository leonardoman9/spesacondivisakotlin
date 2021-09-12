package com.example.applicationprova.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationprova.R
import com.example.applicationprova.databinding.ActivityListOfProductCategoryBinding
import com.example.applicationprova.model.Prodotto
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class ListOfProductCategory : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var searchproducts: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {


        val list2 = mutableListOf<Prodotto>()
        super.onCreate(savedInstanceState)
        //data binding al posto del classico inflate
        val binding: ActivityListOfProductCategoryBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_list_of_product_category
        )
        //setContentView(R.layout.activity_list_of_groups)

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
            true
        }

        database = FirebaseDatabase.getInstance("https://prova-14ff5-default-rtdb.europe-west1.firebasedatabase.app/")
        searchproducts = database.getReference("gruppi")


        val rv: RecyclerView = binding.listaProdottiComprati
        rv.layoutManager = LinearLayoutManager(this)

        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("key")
            val categoria = extras.getString("categoria")

            searchproducts.child(value.toString()).child("prodotti").get().addOnSuccessListener {
                for (postSnapshot in it.children) {
                    if ((postSnapshot.child("buy").getValue().toString()).equals("1") &&
                            (postSnapshot.child("categoria").getValue().toString()).equals(categoria.toString())) {
                        val product: Prodotto = postSnapshot.getValue<Prodotto>() as Prodotto
                        list2.add(product)

                    }
                }
                rv.adapter = ListOfProductBoughtAdapter(list2)

            }
        }
    }
}






