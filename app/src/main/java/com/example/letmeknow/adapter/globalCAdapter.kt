package com.example.letmeknow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class globalCAdapter( val c: Context, val List: ArrayList<String>, val docID:String) : RecyclerView.Adapter<globalCAdapter.MViewHolder>() {

    var i=0
    var j=0
    private val db = FirebaseFirestore.getInstance()

    inner class MViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val myTextView=view.findViewById<TextView>(R.id.btnOption)
        val btn=view.findViewById<Button>(R.id.btnOption)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val listItem=layoutInflater.inflate(R.layout.btnoption,parent,false)
        return MViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        holder.myTextView.text = List[position]
        if (j == 0) {
            holder.btn.setOnClickListener {
                if (i == 0) {
                    if (it.isPressed) {
                        if (i == 0) {


                            val docRef =
                                db.collection("Global").document(docID).collection("PollData")
                                    .document(docID)
                                    .collection(List[position]).document(position.toString())
                            docRef.update("Count", FieldValue.increment(1))

                        }
                        i++
                        j++
                    }
                } else {
                    Toast.makeText(c, "Response Already Recorded ", Toast.LENGTH_SHORT).show()

                }
            }
        }

    }


    override fun getItemCount(): Int {
        return List.size
    }

}

