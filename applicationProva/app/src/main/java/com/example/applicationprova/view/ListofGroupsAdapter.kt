package com.example.applicationprova.view

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationprova.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

/**
 * Adapter per la RecycleView che contiene la lista per tutti i gruppi
 */
class ListofGroupsAdapter (val data: List<String>,val data2: List<String>):

    RecyclerView.Adapter<ListofGroupsAdapter.MyViewHolder>() {
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
        class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
            val textView = row.findViewById<TextView>(R.id.item)
            val textView2 = row.findViewById<TextView>(R.id.idgroup)

        }

    /**
     * Viene chiamata alla creazione del ViewHolder
     *
     */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
                MyViewHolder {
            val layout = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_groups, parent, false)
            val holder = MyViewHolder(layout)
            database = FirebaseDatabase.getInstance("https://prova-14ff5-default-rtdb.europe-west1.firebasedatabase.app/")
            holder.row.setOnClickListener(){
                val intent = Intent(holder.row.context, ListOfProducts::class.java)
                intent.putExtra("key", holder.textView2.text.toString() )
                holder.row.context.startActivity(intent)


            }
            holder.row.setOnLongClickListener(){
                val pop= PopupMenu(it.context,it)
                pop.inflate(R.menu.cab_delete)
                pop.setOnMenuItemClickListener {item->

                    when(item.itemId)

                    {
                        R.id.context_delete ->{
                            myRef= database.getReference("gruppi")
                            myRef.child(holder.textView2.text.toString()).removeValue().addOnSuccessListener {
                                Log.d("Firebase", "Group deleted")
                            }
                            myRef= database.getReference("utentiGruppi")
                            var auth = Firebase.auth
                            val currentUser = auth.currentUser
                            val child=currentUser?.email.toString().replace(".","")
                            myRef.child(child).child(holder.textView2.text.toString()).removeValue().addOnSuccessListener {
                                Log.d("Firebase", "Group deleted from utentiGruppi for user")
                            }
                            val list = mutableListOf<String>()
                            myRef.get().addOnSuccessListener {
                                for (postSnapshot in it.children) {
                                    if (postSnapshot.child(holder.textView2.text.toString())
                                            .exists()
                                    ) {
                                        list.add(postSnapshot.getValue().toString())
                                    }
                                }
                            }
                            for(item in list){
                                myRef.child(item).child(holder.textView2.text.toString()).removeValue().addOnSuccessListener {
                                    Log.d("Firebase", "Group deleted from utentiGruppi for user")
                                }
                            }
                            val intent = Intent(holder.row.context, ListOfGroups::class.java)
                            holder.row.context.startActivity(intent)

                        }

                    }
                    true
                }
                pop.show()
                true
            }
            return holder
        }
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView.text = data.get(position).toString()
            holder.textView2.text = data2.get(position).toString()
            Log.d("ListofGroupAdapter",data.get(position).toString())
        }

        override fun getItemCount(): Int = data.size
}