package com.example.applicationprova.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationprova.authentication.LoginActivity
import com.example.applicationprova.authentication.MyAccount
import com.example.applicationprova.controller.Newgroup
import com.example.applicationprova.R
import com.example.applicationprova.databinding.ActivityListOfGroupsBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

/**
 * Activity per mostrare tutti i gruppi a cui appartiene l'utente loggato
 */
class ListOfGroups : AppCompatActivity() {

    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var searchgroups: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        val list = mutableListOf<String>()
        val list2 = mutableListOf<String>()
        super.onCreate(savedInstanceState)
        //data binding al posto del classico inflate
        val binding: ActivityListOfGroupsBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_list_of_groups
        )
        //Hide message
        binding.empty.visibility = TextView.INVISIBLE
        var auth = Firebase.auth
        val currentUser = auth.currentUser


        database = FirebaseDatabase.getInstance("https://prova-14ff5-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef = database.getReference("utentiGruppi")
        searchgroups= database.getReference("gruppi")


        val rv: RecyclerView = binding.listaGruppi
        rv.layoutManager = LinearLayoutManager(this)

        val divider = MaterialDividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL /*or LinearLayoutManager.HORIZONTAL*/)
        rv.addItemDecoration(divider)


        val child=currentUser?.email.toString().replace(".","")
        myRef.child(child).get().addOnSuccessListener {
            for (postSnapshot in it.children) {

                    list.add(postSnapshot.getValue().toString())
                    list2.add(postSnapshot.key.toString())
              

            }
            if(list.isEmpty()){
                binding.empty.visibility = TextView.VISIBLE
            }

                 rv.adapter = ListofGroupsAdapter(list,list2)
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        rv.adapter = ListofGroupsAdapter(list,list2)
        //Action button
        val fab: View = binding.fab
        fab.setOnClickListener {
            fabOnClick()
        }


        setSupportActionBar(binding.topAppBar)

        binding.topAppBar.inflateMenu(R.menu.bar_groups)
        // and finally set click listener
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            //Handle menu item selected
            menuItem.isChecked = true
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            when (menuItem.itemId) {
                R.id.account -> {
                    val intent = Intent(this, MyAccount::class.java)
                    startActivity(intent)
                    true
                }


                else -> false
            }
        }
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }

        binding.navDrawer.setNavigationItemSelectedListener { menuItem ->
            //Handle menu item selected
            menuItem.isChecked = true
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            when (menuItem.itemId) {
                R.id.account -> {
                    val intent = Intent(this, MyAccount::class.java)
                    startActivity(intent)
                    true
                }
                R.id.logout -> {
                    Snackbar.make(findViewById(android.R.id.content),"Click!", Snackbar.LENGTH_SHORT).show()
                    auth = Firebase.auth
                    auth.signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
            true
        }


    }
    private fun fabOnClick() {
        val intent = Intent(this, Newgroup::class.java)
        startActivity(intent)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.bar_groups, menu)
        return true
    }
}