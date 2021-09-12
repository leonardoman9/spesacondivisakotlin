package com.example.applicationprova.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import com.example.applicationprova.view.ListOfProducts
import com.example.applicationprova.R
import com.example.applicationprova.databinding.ActivityEditProductBinding
import com.example.applicationprova.model.Prodotto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

/**
 * Activity per modificare un prodotto
 */
class EditProduct: AppCompatActivity() {
    lateinit var nome : EditText
    lateinit var categoria: Spinner
    lateinit var quantita: Spinner
    lateinit var note: EditText



    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityEditProductBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_edit_product
        )
        //setContentView(R.layout.activity_inserisci_prodotto)

        nome=binding.Nomeprodotto
        categoria=binding.spinnerCategory
        quantita=binding.spinnerQuantity
        note=binding.note



        database = FirebaseDatabase.getInstance("https://prova-14ff5-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef = database.getReference("gruppi")

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
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("key")
            val idProduct= extras.getString("idProduct")

            myRef.child(value.toString()).child("prodotti").child(idProduct.toString()).get().addOnSuccessListener {

                binding.Nomeprodotto.setText(it.child("nome").getValue().toString())

                val cat = resources.getStringArray(R.array.categorie).toList()
                val quant = resources.getStringArray(R.array.quantita).toList()
                var spinCatPos=0
                var spinQuantPos=0

                for(item in 0..(cat.size-1)){
                    if (cat[item].equals(it.child("categoria").getValue().toString())){
                        spinCatPos=item
                        }
                }


                for(item in 0..(quant.size-1)){
                    if (quant[item].equals(it.child("quantita").getValue().toString())){
                        spinQuantPos=item
                    }
                }


                spinCat.setSelection(spinCatPos)
                spinQuant.setSelection(spinQuantPos)
                binding.note.setText(it.child("note").getValue().toString())





            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)

            }



        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
            true
        }

        binding.btnUpdate.setOnClickListener{

            updateProduct()
        }
        binding.btnCancel.setOnClickListener(){
            onBackPressed()
            false
        }
        binding.btnDelete.setOnClickListener(){
            delete()
        }
    }

    /**
     * Aggiorna il prodotto nel database
     */
    private fun updateProduct(){
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("key")
            val idProduct= extras.getString("idProduct")
            auth = Firebase.auth
            val currentUser = auth.currentUser
            val prodotto = Prodotto(nome.text.toString(),categoria.getSelectedItem().toString(),quantita.getSelectedItem().toString(),note.text.toString(),currentUser?.uid.toString(),currentUser?.displayName.toString())
            myRef.child(value.toString()).child("prodotti").child(idProduct.toString()).setValue(prodotto).addOnSuccessListener {
                val intent = Intent(this, ListOfProducts::class.java)
                intent.putExtra("key", value.toString())
                startActivity(intent)

            }
        }
    }

    /**
     * Rimuove il prodotto dal database
     */
    private fun delete(){
        myRef=database.getReference("gruppi")
        val extras = intent.extras
        val value = extras?.getString("key")
        val idProduct= extras?.getString("idProduct").toString()
        myRef.child(value.toString()).child("prodotti").child(idProduct).removeValue().addOnSuccessListener {
            Log.d("Firebase", "Deleted product")
            val intent = Intent(this, ListOfProducts::class.java)
            intent.putExtra("key", value.toString())
            startActivity(intent)
        }

    }

}
