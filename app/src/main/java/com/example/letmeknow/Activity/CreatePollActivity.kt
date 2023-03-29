package com.example.letmeknow.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.letmeknow.KotlinClass.EndTimeReceiver
import com.example.letmeknow.R
import com.example.letmeknow.adapter.globalPAdapter
import com.example.letmeknow.model.InputData
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class CreatePollActivity : AppCompatActivity(){
//    var day = 0
//    var month = 0
//    var year = 0
//    var hour = 0
//    var minute = 0
//
//    var savedDay = 0
//    var savedMonth = 0
//    var savedYear = 0
//    var savedHour = 0
//    var savedMinute = 0


    private lateinit var ques: EditText
    private lateinit var img: ImageView
    private lateinit var btnCreate: Button
    private lateinit var btnSelect: Button
    private lateinit var btnTime: Button
    private lateinit var btnAlarm: Button
    private lateinit var textDateTime: String
    private var optionlist = mutableListOf<String>()
    private var db = Firebase.firestore
    private lateinit var storageRef: StorageReference
    private var ImageUri: Uri? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var pickerTime:MaterialTimePicker
    private lateinit var calendar:Calendar
    private lateinit var cal:Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private var parentLinearLayout: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_poll)

        ques = findViewById(R.id.question)
        btnCreate = findViewById(R.id.extendedFloatingActionButton2)
        btnSelect = findViewById(R.id.Simg)

        img = findViewById(R.id.imageView)
        storageRef = FirebaseStorage.getInstance().reference.child("Images")

        btnSelect.setOnClickListener {

            selectImage()
        }

        btnTime=findViewById(R.id.btntextTime)
        btnAlarm=findViewById(R.id.setalarm)

        btnTime.setOnClickListener {
            showTimePicker()

        }


        firebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email.toString()

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

            db.collection(email).add(userMap)
                .addOnSuccessListener {
                    val map = HashMap<String, Any>()
                    map["uid"] = it.id
                    userMap.putAll(map)
                    db.collection(email).document(it.id).update(userMap)
                    val docId = it.id

                    // add data to global with same document id as of current user
                    db.collection("Global").document(it.id).set(userMap)
                    //add data for Result
                    db.collection("Result").document(it.id).set(userMap)
                    val countMap= hashMapOf(
                        "Count" to 0
                    )
                    for(i in 0 until optionlist.size) {
                        db.collection("Global").document(it.id).collection("PollData")
                            .document(it.id).collection(optionlist[i]).document(i.toString()).set(countMap)
                    }
                    for(i in 0 until optionlist.size) {
                        db.collection("Result").document(it.id).collection("PollData")
                            .document(it.id).collection(optionlist[i]).document(i.toString()).set(countMap)
                    }

                    val emailCheck=ArrayList<String>()
                    emailCheck.add("check")
                    val checkMap= hashMapOf(
                        "email" to emailCheck
                    )
                    db.collection("Global").document(docId).collection("check").document(docId).set(checkMap)
                    db.collection("Result").document(docId).collection("check").document(docId).set(checkMap)

                    val newdoc=ArrayList<String>()
                    newdoc.add("check")

                    val prog = ProgressDialog(this)
                    prog.setMessage("Uploading Image...")
                    prog.setCancelable(false)
                    prog.show()

                    val formatter = SimpleDateFormat("yyyy_MM_HH_mm_ss", Locale.getDefault())
                    val now = Date()
                    val filename = formatter.format(now)
                    val storageReference =
                        FirebaseStorage.getInstance().getReference("images/$filename")
                    ImageUri?.let {
                        storageReference.putFile(it)
                            .addOnSuccessListener {
                                img.setImageURI(null)
                                storageReference.downloadUrl.addOnSuccessListener { uri ->
                                    val map = HashMap<String, Any>()
                                    map["picURL"] = uri.toString()
                                    userMap.putAll(map)
                                    db.collection("Global").document(docId).update(userMap)
                                    db.collection("Result").document(docId).update(userMap)
                                }

                                if (prog.isShowing) prog.dismiss()
                            }
                            .addOnFailureListener {

                                if (prog.isShowing) prog.dismiss()
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

                            }
                    }

                    Toast.makeText(this, "Successfully Created", Toast.LENGTH_SHORT).show()
                    ques.text.clear()

                    val maps=HashMap<String,Any>()
                    maps["Hour"]=calendar[Calendar.HOUR_OF_DAY]
                    maps["Minute"]=calendar[Calendar.MINUTE]
                    db.collection("Enddata").document(it.id).set(maps)


                    db.collection("Enddata").document(it.id).get()
                        .addOnSuccessListener {documentSnapshot->
                            if(documentSnapshot != null && documentSnapshot.exists()){
                                val hour=documentSnapshot.getLong("Hour")
                                val minute=documentSnapshot.getLong("Minute")

                                cal=Calendar.getInstance()
                                if (hour != null) {
                                    cal[Calendar.HOUR_OF_DAY]=hour.toInt()
                                }
                                if (minute != null) {
                                    cal[Calendar.MINUTE]=minute.toInt()
                                }
                                cal[Calendar.SECOND]=0
                                cal[Calendar.MILLISECOND]=0

                                alarmManager=getSystemService(ALARM_SERVICE) as AlarmManager
                                val intent1=Intent(this,EndTimeReceiver::class.java)
                                intent1.putExtra("docId",docId)
                                pendingIntent=PendingIntent.getBroadcast(this,0,intent1,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

                                alarmManager.set(

                                    AlarmManager.RTC_WAKEUP,cal.timeInMillis,pendingIntent,
                                )
                                Toast.makeText(this, "End Time Set Successfully ", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }


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
//        pickDate()



    }

    private fun showTimePicker() {
        pickerTime=MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select End Time")
            .build()

        pickerTime.show(supportFragmentManager,"saransh")

        pickerTime.addOnPositiveButtonClickListener{
            if(pickerTime.hour>12){
                textDateTime =String.format("%02d",pickerTime.hour - 12) + " : " + String.format("%02d",pickerTime.minute)+"PM"
            }else{
                textDateTime =String.format("%02d",pickerTime.hour) + " : " + String.format("%02d",pickerTime.minute)+"AM"
            }

            calendar= Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY]=pickerTime.hour
            calendar[Calendar.MINUTE]=pickerTime.minute
            calendar[Calendar.SECOND]=0
            calendar[Calendar.MILLISECOND]=0
        }
    }

    private fun selectImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            img.setImageURI(ImageUri)
        }
    }

//    private fun getDateTimeCalendar() {
//        val cal: Calendar = Calendar.getInstance()
//        day = cal.get(Calendar.DAY_OF_MONTH)
//        month = cal.get(Calendar.MONTH)
//        year = cal.get(Calendar.YEAR)
//        hour = cal.get(Calendar.MINUTE)
//        minute = cal.get(Calendar.MINUTE)
//    }

//    private fun pickDate() {
//        val btn = findViewById<Button>(R.id.btntextTime)
//        btn.setOnClickListener {
//            getDateTimeCalendar()
//
//            DatePickerDialog(this, this, year, month, day).show()
//        }
//    }

//    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//        savedDay = dayOfMonth
//        savedMonth = month+1
//        savedYear = year
//
//        getDateTimeCalendar()
//        TimePickerDialog(this, this, hour, minute, true).show()
//    }


//    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//        savedHour = hourOfDay
//        savedMinute = minute
//
//        val bt = findViewById<Button>(R.id.btntextTime)
//        bt.text = "$savedDay-$savedMonth-$savedYear, $savedHour:$savedMinute"
//        textDateTime = bt.text.toString()
//
//
//        calendar=Calendar.getInstance()
//        calendar[Calendar.HOUR_OF_DAY]=savedHour
//        calendar[Calendar.YEAR]=savedYear
//        calendar[Calendar.MONTH]=savedMonth
//        calendar[Calendar.MINUTE]=savedMinute
//        calendar[Calendar.DAY_OF_MONTH]=savedDay
//
//        alarmManager=getSystemService(ALARM_SERVICE) as AlarmManager
//        val intent=Intent(this,EndTimeReceiver::class.java)
//
//        pendingIntent=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_IMMUTABLE)
//
//        alarmManager.setRepeating(
//
//            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
//            AlarmManager.INTERVAL_DAY,pendingIntent
//        )
//        Toast.makeText(this, "End Time Set Successfully ", Toast.LENGTH_SHORT)
//            .show()
//
//    }

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