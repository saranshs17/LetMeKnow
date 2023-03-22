package com.example.letmeknow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letmeknow.R
import com.example.letmeknow.model.GlobalData
import com.example.letmeknow.model.InputData
import com.google.firebase.firestore.FirebaseFirestore

class globalPAdapter(private val c: Context, private val globalList: ArrayList<GlobalData>):
    RecyclerView.Adapter<globalPAdapter.MyViewHolder>() {
    private val db= FirebaseFirestore.getInstance()
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Question: TextView
        val textDateTime: TextView

        val childRecyclerview: RecyclerView

        val image:ImageView

        init {
            Question = itemView.findViewById(R.id.questionPoll)
            textDateTime = itemView.findViewById(R.id.datetimePoll)
            childRecyclerview = itemView.findViewById(R.id.recyclerView2)
            image=itemView.findViewById(R.id.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.activepoll, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return globalList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.Question.text = globalList[position].Question
        holder.textDateTime.text = globalList[position].DateandTime
        holder.childRecyclerview.setHasFixedSize(true)
        holder.childRecyclerview.layoutManager = LinearLayoutManager(holder.itemView.context)

        Glide.with(c)
            .load(globalList[position].picURL)
            .into(holder.image)

        // to add data stored in list to recycler view
        val valuelist = ArrayList<String>()

        // this loop will delete data from list as soon as it add data to recycler view
        for (value in globalList[position].List) {
            valuelist.add(value)

            //passing the list to child recycler view
            val adapter = globalCAdapter(c,valuelist)
            holder.childRecyclerview.adapter = adapter
        }

    }
}