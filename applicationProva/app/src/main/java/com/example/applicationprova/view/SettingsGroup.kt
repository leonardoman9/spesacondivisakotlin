package com.example.applicationprova.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.example.applicationprova.R

import com.example.applicationprova.databinding.ActivitySettingsGroupBinding

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

/**
 * Schermata per visualizzare gli utenti del gruppo, eliminarli o aggiungerli
 */
class SettingsGroup : AppCompatActivity() {

    var database: FirebaseDatabase= FirebaseDatabase.getInstance("https://prova-14ff5-default-rtdb.europe-west1.firebasedatabase.app/")
    var searchUser: DatabaseReference = database.getReference("gruppi")
    var myRefutenti: DatabaseReference = database.getReference("utentiGruppi")
    val list = ArrayList<String>()
    val listNomeUtente = ArrayList<String>()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivitySettingsGroupBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_settings_group
        )
        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
            true
        }
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("key")
            searchUser.child(value.toString()).child("nomeGruppo").get().addOnSuccessListener {
                binding.Nomegruppo.setText(it.value.toString())
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }

            searchUser.child(value.toString()).child("gruppo").get().addOnSuccessListener {
                for (postSnapshot in it.children) {
                    listNomeUtente.add(postSnapshot.getValue().toString())
                    list.add(postSnapshot.key.toString().replace("'", "."))
                }



                binding.listaUtenti.isClickable = true
                binding.listaUtenti.adapter = SettingGroupAdapter(this, listNomeUtente)

                binding.listaUtenti.setOnItemClickListener { parent, view, position, id ->

                    val email = list[position]


                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setTitle("Eliminazione")
                    alertDialog.setMessage("Sei sicuro di voler eliminare l'utente: " + listNomeUtente[position])

                    alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->


                        searchUser.child(value.toString()).child("gruppo").child(list[position].replace(".", "'")).removeValue().addOnSuccessListener {
                            myRefutenti.child(email.replace(".", "")).child(value.toString()).removeValue().addOnSuccessListener {
                                val intent = Intent(this, SettingsGroup::class.java)
                                if (extras != null) {
                                    val value = extras.getString("key")
                                    intent.putExtra("key",value.toString()  )
                                }
                                startActivity(intent)
                            }


                        }

                    })

                    alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

                    alertDialog.show()
//
                }

            }

                .addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                }







            val fab: View = binding.fabNewUser
            fab.setOnClickListener {
                fabOnClick(value.toString(),binding.Nomegruppo.text.toString())
            }
        }
    }

    private fun fabOnClick(keygroup:String, namegroup:String) {




        val builder: AlertDialog.Builder =AlertDialog.Builder(this)
        builder.setTitle("Inserisci un Nuovo Componente")
        val layout = LinearLayout(this)
        val nome = EditText(this)
        nome.hint = "Nome"
        nome.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(nome)
        val email = EditText(this)
        email.hint = "Email"
        email.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(email)
        builder.setView(layout)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->

            var email = email.text.toString()
            var nome = nome.text.toString()
            searchUser.child(keygroup).child("gruppo").child(email.replace(".","")).setValue(nome).addOnSuccessListener {
                myRefutenti.child(email.replace(".","")).setValue(namegroup).addOnSuccessListener {
                    val intent = Intent(this, SettingsGroup::class.java)
                    intent.putExtra("key",keygroup)
                    startActivity(intent)
                }



            }
        })

        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
//
    }
}