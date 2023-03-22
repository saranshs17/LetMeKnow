package com.example.letmeknow.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.R
import com.example.letmeknow.adapter.MyAdapter
import com.example.letmeknow.adapter.globalPAdapter
import com.example.letmeknow.model.GlobalData
import com.example.letmeknow.model.InputData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ActiveFragment : Fragment() {

    private lateinit var recylerView: RecyclerView
    private lateinit var globalList: ArrayList<GlobalData>

    private var db = Firebase.firestore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_active, container, false)

        recylerView = view.findViewById(R.id.globalRecycler)
        recylerView.layoutManager = LinearLayoutManager(context)
        globalList=ArrayList<GlobalData>()
        db = FirebaseFirestore.getInstance()

        db.collection("Global").get()
            .addOnSuccessListener {
            if(!it.isEmpty){
                globalList.clear()
                for(data in it.documents){
//                        val user: GlobalData? = data.toObject(GlobalData::class.java)
                    val user=GlobalData(data["Question"].toString(),data["DateandTime"].toString(), data["List"] as ArrayList<String>,data["uid"].toString(),data["picURL"].toString())
                    if (user != null) {
                        globalList.add(user)
                    }
                }
                recylerView.adapter= globalPAdapter(requireContext(),globalList)
            }
        }


        return view
    }
}