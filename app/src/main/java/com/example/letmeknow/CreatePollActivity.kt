package com.example.letmeknow

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class CreatePollActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    private lateinit var ques: EditText
    private lateinit var btnCreate: Button
    private lateinit var textDateTime: String
    private var optionlist = mutableListOf<String>()
    private var db = Firebase.firestore

    val list = ArrayList<String>()

    private var parentLinearLayout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_poll)

        ques = findViewById(R.id.question)
        btnCreate = findViewById(R.id.extendedFloatingActionButton2)
        //mutable list which will contain entered choices



        btnCreate.setOnClickListener {
            val sques = ques.text.toString().trim()
            val stextDateTime = textDateTime.trim()

            var i = 0
            while (i < parentLinearLayout!!.childCount) {
                var view = parentLinearLayout!!.getChildAt(i)
                var editText = view.findViewById<EditText>(R.id.option)
                optionlist.add(editText.text.toString())
//            Toast.makeText(this,editText.text,Toast.LENGTH_SHORT).show()

                i++
            }
            val sdocData = optionlist
            val userMap = hashMapOf(
                "Question" to sques,
                "DateandTime" to stextDateTime,
                "List" to sdocData
            )

            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            db.collection("User").add(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully Created", Toast.LENGTH_SHORT).show()
                    ques.text.clear()
                    //to home
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to Create", Toast.LENGTH_SHORT).show()
                }

        }

        //to add option button Layout dynamically
        parentLinearLayout = findViewById(R.id.optionLayout)

        //to pick date
        pickDate()

    }

    private fun getDateTimeCalendar() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.MINUTE)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {
        val btn = findViewById<Button>(R.id.btntextTime)
        btn.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(this, this, hour, minute, true).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        val bt = findViewById<Button>(R.id.btntextTime)
        bt.text = "$savedDay-$savedMonth-$savedYear, $savedHour:$savedMinute"
        textDateTime = bt.text.toString()
    }

    fun onAdd(view: View) {
        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.add_option, null)
        parentLinearLayout!!.addView(rowView, parentLinearLayout!!.childCount - 1)
    }

    fun onDelete(view: View) {
        parentLinearLayout!!.removeView(view.parent as View)
    }
}
