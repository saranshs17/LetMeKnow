package com.example.letmeknow.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.model.InputData
import com.example.letmeknow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class MyAdapter(val c: Context, private val userList: ArrayList<InputData>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private val db = FirebaseFirestore.getInstance()
    private var firebaseAuth = FirebaseAuth.getInstance()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Question: TextView
        val textDate: TextView
        val textTime: TextView

        val childRecyclerview: RecyclerView
        val btnDot: ImageView

        val email = firebaseAuth.currentUser?.email.toString()

        init {
            Question = itemView.findViewById(R.id.questionPoll)
            textDate = itemView.findViewById(R.id.datePoll)
            textTime = itemView.findViewById(R.id.timePoll)
            childRecyclerview = itemView.findViewById(R.id.myrecyclerView2)
            btnDot = itemView.findViewById(R.id.btnMore)
            btnDot.setOnClickListener { popupMenus(it) }
        }

        private fun popupMenus(itemView: View) {
            val popupMenus = PopupMenu(c, itemView)
            popupMenus.inflate(R.menu.popup)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.dltPoll -> {
                        val docID = db.collection("${db.collection(email).id}")
                            .document("${userList[position].uid}").id
                        AlertDialog.Builder(c)
                            .setTitle("Delete")
                            .setMessage("Are you sure delete this Poll")
                            .setPositiveButton("Yes") { dialog, _ ->
                                userList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                dialog.dismiss()
                                db.collection("${db.collection(email).id}").document(docID).delete()
                                db.collection("Global").document(docID).delete()
                                db.collection("Global").document(docID).collection("PollData").document(docID).delete()
                                db.collection("Result").document(docID).delete()
                                db.collection("Result").document(docID).collection("PollData").document(docID).delete()
                                db.collection("Display").document(docID).delete()
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
//                        Toast.makeText(c, "Poll Delete", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> {
                        true
                    }
                }
            }
            popupMenus.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.mylist, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.Question.text = userList[position].Question
        holder.textDate.text = userList[position].Date
        holder.textTime.text = userList[position].Time
        holder.childRecyclerview.setHasFixedSize(true)
        holder.childRecyclerview.layoutManager = LinearLayoutManager(holder.itemView.context)
        // to add data stored in list to recycler view
        val valuelist = ArrayList<String>()

        // this loop will delete data from list as soon as it add data to recycler view
        for (value in userList[position].List) {
            valuelist.add(value)
        }

        //passing the list to child recycler view

        val adapter = ChildRecycler(c, valuelist)
        holder.childRecyclerview.adapter = adapter

    }
}