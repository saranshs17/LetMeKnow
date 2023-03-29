package com.example.letmeknow.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.Activity.MainActivity
import com.example.letmeknow.Fragment.HomeFragment
import com.example.letmeknow.R
import com.example.letmeknow.model.GlobalData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField


class globalCAdapter(val c: Context, val List: ArrayList<String>, val docID: String) :
    RecyclerView.Adapter<globalCAdapter.MViewHolder>() {


    private val db = FirebaseFirestore.getInstance()
    private lateinit var firebaseAuth: FirebaseAuth


    inner class MViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val myTextView = view.findViewById<TextView>(R.id.btnOption)
        val btn = view.findViewById<Button>(R.id.btnOption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.btnoption, parent, false)
        return MViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {

        holder.myTextView.text = List[position]

        firebaseAuth = FirebaseAuth.getInstance()

        db.collection("Global").document(docID).collection("check").document(docID).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val checkList = documentSnapshot.get("email") as List<String>

                    holder.btn.setOnClickListener {
                        if (it.isPressed) {
                            var ctr = 1
                            for (k in 0 until List.size) {
                                val dRef = db.collection("Global").document(docID)
                                    .collection("PollData")
                                    .document(docID)
                                    .collection(List[k]).document(k.toString())

                                dRef.get().addOnSuccessListener { documentSnapshot ->
                                    if (documentSnapshot != null && documentSnapshot.exists()) {
                                        val fieldV = documentSnapshot.getLong("Count")

                                        if (fieldV != 0L) {
                                            if (!checkList.contains(firebaseAuth.currentUser!!.email.toString())) {

                                                // for updating entered data to Global
                                                val docRef =
                                                    db.collection("Global").document(docID)
                                                        .collection("check").document(docID)
                                                docRef.update(
                                                    "email",
                                                    FieldValue.arrayUnion(firebaseAuth.currentUser!!.email.toString())
                                                )

                                                val docRef2 =
                                                    db.collection("Global").document(docID)
                                                        .collection("PollData")
                                                        .document(docID)
                                                        .collection(List[position])
                                                        .document(position.toString())
                                                docRef2.update(
                                                    "Count",
                                                    FieldValue.increment(1)
                                                )


                                                // for updating entered data to Result
                                                val docRef3 =
                                                    db.collection("Result").document(docID)
                                                        .collection("check").document(docID)
                                                docRef3.update(
                                                    "email",
                                                    FieldValue.arrayUnion(firebaseAuth.currentUser!!.email.toString())
                                                )

                                                val docRef4 =
                                                    db.collection("Result").document(docID)
                                                        .collection("PollData")
                                                        .document(docID)
                                                        .collection(List[position])
                                                        .document(position.toString())
                                                docRef4.update(
                                                    "Count",
                                                    FieldValue.increment(1)
                                                )

                                            } else {
                                                Toast.makeText(
                                                    c,
                                                    "Response Already Recorded ",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        } else if (fieldV == 0L) {

                                            if (ctr == List.size) {

                                                // for updating entered data to Global
                                                val docRef =
                                                    db.collection("Global").document(docID)
                                                        .collection("check").document(docID)
                                                docRef.update(
                                                    "email",
                                                    FieldValue.arrayUnion(firebaseAuth.currentUser!!.email.toString())
                                                )

                                                val docRef2 =
                                                    db.collection("Global").document(docID)
                                                        .collection("PollData")
                                                        .document(docID)
                                                        .collection(List[position])
                                                        .document(position.toString())
                                                docRef2.update("Count", FieldValue.increment(1))


                                                // for updating entered data to Result
                                                val docRef3 =
                                                    db.collection("Result").document(docID)
                                                        .collection("check").document(docID)
                                                docRef3.update(
                                                    "email",
                                                    FieldValue.arrayUnion(firebaseAuth.currentUser!!.email.toString())
                                                )

                                                val docRef4 =
                                                    db.collection("Result").document(docID)
                                                        .collection("PollData")
                                                        .document(docID)
                                                        .collection(List[position])
                                                        .document(position.toString())
                                                docRef4.update("Count", FieldValue.increment(1))

                                            }
                                            ctr++
                                        }

                                    }
                                }
                            }
                        }
                    }


                }
            }


    }


    override fun getItemCount(): Int {
        return List.size
    }

}

