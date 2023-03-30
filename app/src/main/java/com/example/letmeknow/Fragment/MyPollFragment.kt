package com.example.letmeknow.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.letmeknow.R
import com.example.letmeknow.adapter.ResultPAdapter
import com.example.letmeknow.model.CountData
import com.example.letmeknow.model.GlobalData
import com.example.letmeknow.model.ResultData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyPollFragment() : Fragment() {


    private lateinit var recylerView: RecyclerView
    private lateinit var resultList: ArrayList<ResultData>
    private lateinit var rList: ArrayList<CountData>
    private lateinit var swipe:SwipeRefreshLayout

    private var db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_mypoll, container, false)

        recylerView = view.findViewById(R.id.resultsRecycler)
        recylerView.layoutManager = LinearLayoutManager(context)
        resultList=ArrayList<ResultData>()
        rList= ArrayList<CountData>()
        swipe=view.findViewById(R.id.swipetoRefresh)
        swipe.setOnRefreshListener {

            recylerView.adapter?.notifyDataSetChanged()
            Toast.makeText(context, "Results Refreshed!", Toast.LENGTH_SHORT).show()
            swipe.isRefreshing=false
        }

        db = FirebaseFirestore.getInstance()

        db.collection("Result").get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    resultList.clear()
                    for(data in it.documents){

                        val user=ResultData(data["Question"].toString(),data["Date"].toString(),data["Time"].toString(), data["List"] as ArrayList<String>,data["uid"].toString(),data["picURL"].toString())
                        if (user != null) {
                            resultList.add(user)
                        }
                        for(i in 0 until (data["List"] as ArrayList<String>).size ) {
                            var optn:ArrayList<String>
                            optn=(data["List"] as ArrayList<String>)
                            db.collection("Result").document(data["uid"].toString()).collection("PollData")
                                .document(data["uid"].toString()).collection(optn[i]).get()
                                .addOnSuccessListener {
                                    if (!it.isEmpty) {
                                        for (data in it.documents) {
                                            val userr=CountData(data["Count"] as Long)
                                            if (userr != null) {
                                                rList.add(userr)
                                            }
                                        }

                                    }
                                }
                        }
                        recylerView.adapter=ResultPAdapter(data["uid"].toString(),resultList,rList)


                    }
                }
            }
        return view
    }
}