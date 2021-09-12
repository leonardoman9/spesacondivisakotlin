package com.example.applicationprova.view

import android.content.Intent
import android.util.Log
import android.util.SparseBooleanArray
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationprova.R
import com.example.applicationprova.model.SingletonIdProducts
import com.example.applicationprova.controller.EditProduct
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ListofProductAdapter (val data: List<String>,val data2: List<String>, val idGroup: String):
    RecyclerView.Adapter<ListofProductAdapter.MyViewHolder>() {
    var checkBoxStateArray = SparseBooleanArray()
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MyViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_products, parent, false)
        val holder = MyViewHolder(layout)
        var auth = Firebase.auth
        database = FirebaseDatabase.getInstance("https://prova-14ff5-default-rtdb.europe-west1.firebasedatabase.app/")
        myRef= database.getReference("gruppi")
        holder.row.setOnClickListener(){
            //(prodActivity as ListOfProducts).fabOnClick()
            val intent = Intent(holder.row.context, EditProduct::class.java)
            intent.putExtra("key", idGroup)
            intent.putExtra("idProduct", holder.textView2.text.toString())
            holder.row.context.startActivity(intent)

        }
        holder.row.setOnLongClickListener(){
            val pop= PopupMenu(it.context,it)
            pop.inflate(R.menu.cab_menu)
            pop.setOnMenuItemClickListener {item->

                when(item.itemId)

                {
                    R.id.context_delete ->{
                        myRef.child(idGroup).child("prodotti").child(holder.textView2.text.toString()).removeValue().addOnSuccessListener {
                            Log.d("Firebase", "Product deleted")
                        }
                        val intent = Intent(holder.row.context, ListOfProducts::class.java)
                        intent.putExtra("key", idGroup)
                        intent.putExtra("idProduct", holder.textView2.text.toString())
                        holder.row.context.startActivity(intent)

                    }

                    R.id.context_edit ->{
                        val intent = Intent(holder.row.context, EditProduct::class.java)
                        intent.putExtra("key", idGroup)
                        intent.putExtra("idProduct", holder.textView2.text.toString())
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
        if(!checkBoxStateArray.get(position,false))
        {//checkbox unchecked.
            holder.checkBox.isChecked = false
        }
        else
        {//checkbox checked
            holder.checkBox.isChecked = true
        }
        //gets position from data object
        var data_position = data[position]

        Log.d("ListofGroupAdapter",data.get(position).toString())
    }

    override fun getItemCount(): Int = data.size

    inner class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val textView = row.findViewById<TextView>(R.id.item)
        val textView2 = row.findViewById<TextView>(R.id.idproduct)
        val checkBox = row.findViewById<CheckBox>(R.id.checkBox)

        init
        {//called after the constructor.

            checkBox.setOnClickListener {

                if(!checkBoxStateArray.get(adapterPosition,false))
                {//checkbox checked
                    checkBox.isChecked = true
                    SingletonIdProducts.addId(textView2.text.toString())
                    //stores checkbox states and position
                    checkBoxStateArray.put(adapterPosition,true)
                }
                else
                {//checkbox unchecked
                    checkBox.isChecked = false
                    SingletonIdProducts.removeId(textView2.text.toString())
                    //stores checkbox states and position.
                    checkBoxStateArray.put(adapterPosition,false)
                }

            }
        }
    }


}