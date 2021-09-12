package com.example.applicationprova.view

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.applicationprova.R

class SaldoAdapter(private val context : Activity, private  val map: Map<String,Float>,private  val arrayList: ArrayList<String>): ArrayAdapter<String>(context,
    R.layout.item_divisione_spese,arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater :LayoutInflater= LayoutInflater.from(context )
        val view:View= inflater.inflate(R.layout.item_divisione_spese,null)

        val Utente : TextView = view.findViewById(R.id.utente)
        val saldo : TextView = view.findViewById(R.id.saldo)



        val divisione = dividi(map)
        val dovuto: Float=map.values.toFloatArray()[position]-divisione

        if (dovuto>0){
            Utente.text=arrayList[position]
            saldo.setBackgroundResource(R.drawable.round_corner)
            saldo.background.setTint(Color.GREEN)
            saldo.text=String.format("%.2f", dovuto)+"€"

        }
        else{
            saldo.text=arrayList[position]
            Utente.text=String.format("%.2f", dovuto)+"€"
            Utente.setBackgroundResource(R.drawable.round_corner)
            Utente.background.setTint(Color.RED)

        }


        return view
    }

    fun dividi(spese: Map<String, Float>): Float {
        val divisione = spese.values.sum()/spese.size
        return divisione
    }

}