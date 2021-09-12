package com.example.applicationprova.controller

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.applicationprova.view.ListOfProducts
import com.example.applicationprova.R
import com.example.applicationprova.databinding.ActivityInserisciProdottoBinding
import com.example.applicationprova.model.Prodotto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

/**
 * Activity per inserire un nuovo elemento nella lista della spesa del gruppo
 */
class InserisciProdotto: AppCompatActivity() {
    lateinit var nome : EditText
    lateinit var categoria: Spinner
    lateinit var quantita: Spinner
    lateinit var note: EditText

    var productid: Long=0

   lateinit var database: FirebaseDatabase
   lateinit var myRef: DatabaseReference

   private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityInserisciProdottoBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_inserisci_prodotto
        )
        //setContentView(R.layout.activity_inserisci_prodotto)

        nome=binding.Nomeprodotto
        categoria=binding.spinnerCategory
        quantita=binding.spinnerQuantity
        note=binding.note

        val spinCat: Spinner = binding.spinnerCategory
        ArrayAdapter.createFromResource(
                this,
            R.array.categorie,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinCat.adapter = adapter
        }

        val spinQuant: Spinner = binding.spinnerQuantity
        ArrayAdapter.createFromResource(
                this,
            R.array.quantita,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinQuant.adapter = adapter
        }

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
            true
        }
        database = FirebaseDatabase.getInstance("https://prova-14ff5-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef = database.getReference("gruppi")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists())
                    productid = dataSnapshot.childrenCount.toLong()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("prova", "Failed to read value.", error.toException())
            }
        })


        binding.btninsert.setOnClickListener{

           insertProduct()
        }
        binding.btncancel.setOnClickListener(){
            onBackPressed()
            false
        }
   }

    /**
     * Inserisce l'oggetto creato nel database
     */
    private fun insertProduct(){
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("key")
        auth = Firebase.auth
        val currentUser = auth.currentUser
            if(TextUtils.equals(categoria.getSelectedItem().toString(), "Seleziona categoria")){
                val errorText = categoria.getSelectedView() as TextView
                errorText.error = "Seleziona una categoria"

            }
            else if (TextUtils.equals(quantita.getSelectedItem().toString(), "Seleziona quantità")){
                val errorText = quantita.getSelectedView() as TextView
                errorText.error = "Seleziona Una quantità"

            }
            else{
            val prodotto = Prodotto(nome.text.toString(), categoria.getSelectedItem().toString(), quantita.getSelectedItem().toString(), note.text.toString(), currentUser?.uid.toString(), currentUser?.displayName.toString())
        myRef.child(value.toString()).child("prodotti").push().setValue(prodotto).addOnSuccessListener {
            val intent = Intent(this, ListOfProducts::class.java)
            intent.putExtra("key", value.toString())
            startActivity(intent)

        }
            }
        }


    }
}
