package com.example.letmeknow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.R

class ChildRecycler(val c: Context, val List: ArrayList<String>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val listItem=layoutInflater.inflate(R.layout.mychildlist,parent,false)
        return MyViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return List.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.myTextView.text=List[position]

//        var i=0
//        while (i<List.size)
//        {
//            holder.btn.setOnClickListener {
//                i++
//            }
//        }
    }
}

class MyViewHolder(val view: View):RecyclerView.ViewHolder(view){
    val myTextView=view.findViewById<TextView>(R.id.tvOption)
}
