
package com.example.letmeknow.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.R
import com.example.letmeknow.adapter.MyAdapter
import com.example.letmeknow.model.InputData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResultsFragment : Fragment() {


    private lateinit var recylerView: RecyclerView
    private lateinit var userList: ArrayList<InputData>
    private lateinit var firebaseAuth: FirebaseAuth


    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_results, container, false)

        recylerView = view.findViewById(R.id.mRecycler)
        recylerView.layoutManager = LinearLayoutManager(context)


        firebaseAuth=FirebaseAuth.getInstance()
        val email= firebaseAuth.currentUser?.email.toString()


        userList = ArrayList<InputData>()
        db = FirebaseFirestore.getInstance()

        db.collection(email).get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    userList.clear()
                    for(data in it.documents){
//                        val user: InputData? = data.toObject(InputData::class.java)
                        val user=InputData(data["Question"].toString(),data["DateandTime"].toString(), data["List"] as ArrayList<String>,data["uid"].toString())
                        if (user != null) {
                            userList.add(user)
                        }
                    }
                    recylerView.adapter= MyAdapter(requireContext(),userList)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
            }

        return view
    }
}


