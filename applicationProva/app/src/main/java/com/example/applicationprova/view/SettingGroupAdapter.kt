package com.example.applicationprova.view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.applicationprova.R

class SettingGroupAdapter(private val context : Activity, private  val arrayList: ArrayList<String>): ArrayAdapter<String>(context, R.layout.item_user,arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater :LayoutInflater= LayoutInflater.from(context )
        val view:View= inflater.inflate(R.layout.item_user,null)

        val email : TextView = view.findViewById(R.id.userEmail)

        email.text=arrayList[position]

        return view
    }


}