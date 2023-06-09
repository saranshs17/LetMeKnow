package com.example.letmeknow.Fragment

import android.os.Bundle
import android.view.*
//import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.letmeknow.R
import androidx.appcompat.widget.SearchView
import com.example.letmeknow.adapter.globalPAdapter
import com.example.letmeknow.model.CountData
import com.example.letmeknow.model.GlobalData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ActiveFragment : Fragment() {

    private lateinit var recylerView: RecyclerView
    private lateinit var globalList: ArrayList<GlobalData>
    private lateinit var gList: ArrayList<CountData>
    private lateinit var swipe: SwipeRefreshLayout

    private var db = Firebase.firestore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_active, container, false)

        recylerView = view.findViewById(R.id.globalRecycler)
        recylerView.layoutManager = LinearLayoutManager(context)
        globalList=ArrayList<GlobalData>()
        gList= ArrayList<CountData>()
        db = FirebaseFirestore.getInstance()
        swipe=view.findViewById(R.id.swipeRefreshGlobal)

        swipe.setOnRefreshListener {
            recylerView.adapter?.notifyDataSetChanged()
            Toast.makeText(context, "Polls Refreshed!", Toast.LENGTH_SHORT).show()
            swipe.isRefreshing=false
        }

        db.collection("Global").get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    globalList.clear()
                    for(data in it.documents){
//                        val user: GlobalData? = data.toObject(GlobalData::class.java)
                        val user=GlobalData(data["Question"].toString(),data["Date"].toString(),data["Time"].toString(), data["List"] as ArrayList<String>,data["uid"].toString(),data["picURL"].toString())
                        if (user != null) {
                            globalList.add(user)
                        }
                        for(i in 0 until (data["List"] as ArrayList<String>).size ) {
                            var optn:ArrayList<String>
                            optn=(data["List"] as ArrayList<String>)
                            db.collection("Global").document(data["uid"].toString()).collection("PollData")
                                .document(data["uid"].toString()).collection(optn[i]).get()
                                .addOnSuccessListener {
                                    if (!it.isEmpty) {
                                        for (data in it.documents) {
                                            val userr=CountData(data["Count"] as Long)
                                            if (userr != null) {
                                                gList.add(userr)
                                            }
                                        }

                                    }
                                }
                        }
                    }
                    recylerView.adapter= context?.let { it1 -> globalPAdapter(it1,globalList,gList) }

                }
            }
        return view
    }

}