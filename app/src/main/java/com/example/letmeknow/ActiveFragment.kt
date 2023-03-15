package com.example.letmeknow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ActiveFragment : Fragment() {
    private lateinit var recylerView:RecyclerView
    private lateinit var recylerViewChild:RecyclerView
    private lateinit var userList: ArrayList<InputData>
    private var db=Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_active, container, false)

        recylerView=view.findViewById(R.id.mRecycler)
        recylerView.layoutManager=LinearLayoutManager(context)

        userList = arrayListOf()
        db = FirebaseFirestore.getInstance()
        db.collection("User").get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    for(data in it.documents){
                        val user: InputData? = data.toObject(InputData::class.java)
                        if (user != null) {
                            userList.add(user)
                        }
                    }
                    recylerView.adapter=MyAdapter(userList)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
            }
        return view
    }
}