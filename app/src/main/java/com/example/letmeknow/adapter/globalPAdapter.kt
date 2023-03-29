package com.example.letmeknow.adapter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letmeknow.Activity.CreatePollActivity
import com.example.letmeknow.Activity.MainActivity
import com.example.letmeknow.R
import com.example.letmeknow.model.CountData
import com.example.letmeknow.model.GlobalData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class globalPAdapter(private val c: Context, private val globalList: ArrayList<GlobalData>, private val gList: ArrayList<CountData>):
    RecyclerView.Adapter<globalPAdapter.MyViewHolder>() {

    private val db= FirebaseFirestore.getInstance()
    private var firebaseAuth = FirebaseAuth.getInstance()
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Question: TextView
        val textDateTime: TextView

        val childRecyclerview: RecyclerView

        val image:ImageView
        val btnsubmit:Button =itemView.findViewById(R.id.submit)

        val email = firebaseAuth.currentUser?.email.toString()

        init {
            Question = itemView.findViewById(R.id.questionPoll)
            textDateTime = itemView.findViewById(R.id.datetimePoll)
            childRecyclerview = itemView.findViewById(R.id.recyclerView2)
            image=itemView.findViewById(R.id.imageView)

        }

    }

    private var data: HashMap<String, Any> = HashMap()
    fun setData(data: HashMap<String, Any>) {
        this.data = data
        notifyDataSetChanged()
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

        val docID=globalList[position].uid
        var i=0

        // this loop will delete data from list as soon as it add data to recycler view
        for (value in globalList[position].List) {
            valuelist.add(value)

            for(j in 0 until gList.size) {
                val map = HashMap<String, Any>()
                map["Count"] = gList[j].Count
                db.collection("Global").document(docID.toString()).collection("PollData")
                    .document(docID.toString()).collection(value).document(i.toString()).update(map)
            }

            val emap = HashMap<String, Any>()
            emap["userList"]=firebaseAuth.currentUser!!.toString()
            db.collection("Global").document(docID.toString()).update(emap)
            //passing the list to child recycler view
            val adapter = globalCAdapter(c,valuelist,docID.toString())
            holder.childRecyclerview.adapter = adapter
            i++
        }
        // to reload adapter on submit btn click
        holder.btnsubmit.setOnClickListener {
            val adapter = globalCAdapter(c,valuelist,docID.toString())
            holder.childRecyclerview.adapter = adapter
            adapter.notifyDataSetChanged()
        }


    }
}