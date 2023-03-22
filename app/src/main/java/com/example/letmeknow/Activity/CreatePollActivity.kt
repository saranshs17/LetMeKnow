package com.example.letmeknow.Activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.letmeknow.R
import com.example.letmeknow.model.InputData
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
    private lateinit var img: ImageView
    private lateinit var btnCreate: Button
    private lateinit var btnSelect: Button
    private lateinit var btnUpload: Button
    private lateinit var textDateTime: String
    private var optionlist = mutableListOf<String>()
    private var db = Firebase.firestore
    private lateinit var storageRef: StorageReference
    private var ImageUri: Uri? = null
    private lateinit var firebaseAuth: FirebaseAuth


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
                "List" to sdocData,
                "picURL" to ""
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
                                }
                                Toast.makeText(this, "Successfully Uploaded", Toast.LENGTH_SHORT)
                                    .show()
                                if (prog.isShowing) prog.dismiss()
                            }
                            .addOnFailureListener {

                                if (prog.isShowing) prog.dismiss()
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

                            }
                    }

                    Toast.makeText(this, "Successfully Created ", Toast.LENGTH_SHORT).show()
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
