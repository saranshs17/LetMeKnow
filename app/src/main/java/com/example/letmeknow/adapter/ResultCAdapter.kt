package com.example.letmeknow.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ResultCAdapter(
    val List: ArrayList<String>,
    val docID:String,
    val AlldocId: MutableList<DocumentSnapshot>,
): RecyclerView.Adapter<RcViewHolder>(){

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RcViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val listItem=layoutInflater.inflate(R.layout.resultchildlist,parent,false)
        return RcViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: RcViewHolder, position: Int) {
        for(i in 0 until AlldocId.size) {
            val docref =
                db.collection("Global").document(AlldocId[i].id).collection("PollData").document(AlldocId[i].id)
                    .collection(List[position]).document(position.toString())
            docref.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val fieldValue = documentSnapshot.getLong("Count")


//                        val barDataSet = BarDataSet(listOf(BarEntry(1f, 2f), BarEntry(2f, 4f), BarEntry(3f, 6f)), "Data Set")

                        val list: ArrayList<BarEntry> = ArrayList()
                        for(j in 0 until List.size) {
                            val drf = db.collection("Global").document(AlldocId[i].id)
                                .collection("PollData").document(AlldocId[i].id)
                                .collection(List[j]).document(j.toString())
                            drf.get()
                                .addOnSuccessListener { documentSnapshot ->
                                    if (documentSnapshot != null && documentSnapshot.exists()) {
                                        val fieldV = documentSnapshot.getLong("Count")
                                        if (fieldV != null) {
                                            val yf = fieldV.toFloat()
                                            list.add(BarEntry((j + 1).toFloat(), yf))
                                            holder.optntv.text = holder.optntv.text.toString()+"\n"+((List[j] + ", Votes = " + fieldV.toString()))

                                            val barDataSet= BarDataSet(list,"List")

                                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
                                            barDataSet.valueTextColor= Color.BLACK

                                            val barData = BarData(barDataSet)
                                            holder.barChart.data = barData

                                            holder.barChart.description.isEnabled = false
                                            holder.barChart.legend.isEnabled = false
                                            holder.barChart.axisRight.isEnabled = false
                                            holder.barChart.setPinchZoom(false)
                                            holder.barChart.setDrawBarShadow(true)
                                            holder.barChart.setDrawGridBackground(false)
                                            holder.barChart.animateY(1000)
                                            holder. barChart.setFitBars(false)
                                            holder.barChart.invalidate()

                                        }
                                    }
                            }

                        }
//                        list.add(BarEntry(1f,2f))
//                        list.add(BarEntry(2f,3f))
//                        list.add(BarEntry(3f,0f))
//                        list.add(BarEntry(4f,5f))



                    }
                }
        }


        // Set up the chart


    }
}

class RcViewHolder(val view: View):RecyclerView.ViewHolder(view) {
    val optntv=view.findViewById<TextView>(R.id.tvOptionR)
    val barChart: BarChart = itemView.findViewById(R.id.barChart)
}
