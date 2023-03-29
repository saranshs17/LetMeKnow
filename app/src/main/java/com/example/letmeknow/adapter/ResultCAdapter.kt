package com.example.letmeknow.adapter

import android.graphics.Color
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ResultCAdapter(
    val List: ArrayList<String>,
    val docID: String,
    val AlldocId: MutableList<DocumentSnapshot>,
) : RecyclerView.Adapter<RcViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RcViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.resultchildlist, parent, false)
        return RcViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: RcViewHolder, position: Int) {
        for (i in 0 until AlldocId.size) {

            val list = ArrayList<BarEntry>()
            val label = MutableList<String>(List.size) { "null" }
            for (j in 0 until List.size) {
                val drf = db.collection("Global").document(AlldocId[i].id)
                    .collection("PollData").document(AlldocId[i].id)
                    .collection(List[j]).document(j.toString())
                drf.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            val fieldV = documentSnapshot.getLong("Count")
                            if (fieldV != null) {
                                val yf = fieldV
                                list.add(BarEntry(yf.toFloat(), j))
                                holder.optntv.text = holder.optntv.text.toString() + "\n" + ((j).toString() + ".) " + List[j])

                                label.set(j, j.toString())
                                val barDataSet = BarDataSet(list, "Count")
                                val data = BarData(label, barDataSet)
                                holder.barChart.data = data
                                holder.barChart.legend.isEnabled=false
                                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS,255)
                                holder.barChart.setDescription("Poll Count")
                                holder.barChart.animateY(1000)
                                holder.barChart.axisRight.isEnabled = false
                                barDataSet.valueTextColor = Color.BLACK
                                holder.barChart.setDescriptionColor(Color.BLACK)
                                barDataSet.valueTextSize=14f
                                holder.barChart.setPinchZoom(false)
                                holder.barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                                holder.barChart.invalidate()

                            }
                        }
                    }

            }


        }


    }
}

class RcViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val optntv = view.findViewById<TextView>(R.id.tvOptionR)
    val barChart: BarChart = itemView.findViewById(R.id.barChart)
}
