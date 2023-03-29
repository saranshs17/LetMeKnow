package com.example.letmeknow.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.R
import com.example.letmeknow.model.CountData
import com.example.letmeknow.model.GlobalData
import com.example.letmeknow.model.ResultData
import com.google.firebase.firestore.FirebaseFirestore

class ResultPAdapter(val docID:String, private val resultList: ArrayList<ResultData>, private val rList: ArrayList<CountData>): RecyclerView.Adapter<ResultPAdapter.RViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    inner class RViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val Question:TextView
        val childResultRV: RecyclerView

        init{
            Question=itemView.findViewById(R.id.questionResult)
            childResultRV=itemView.findViewById(R.id.resultsChildRecycler)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.resultlist, parent, false)
        return RViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    override fun onBindViewHolder(holder: RViewHolder, position: Int) {
        holder.Question.text=resultList[position].Question
        holder.childResultRV.setHasFixedSize(true)
        holder.childResultRV.layoutManager=LinearLayoutManager(holder.itemView.context)

        val db = FirebaseFirestore.getInstance()


        val valuelist = ArrayList<String>()
        for (value in resultList[position].List) {
            valuelist.add(value)

            val collectionRef = db.collection("Result")

            collectionRef.get().addOnSuccessListener { querySnapshot ->
                val AlldocId = querySnapshot.documents
                holder.childResultRV.adapter = ResultCAdapter(valuelist,docID,AlldocId)

            }
        }
    }
}