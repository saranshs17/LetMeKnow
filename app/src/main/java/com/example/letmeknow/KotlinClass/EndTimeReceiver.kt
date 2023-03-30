package com.example.letmeknow.KotlinClass

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.Activity.MainActivity
import com.example.letmeknow.Activity.SignInActivity
import com.example.letmeknow.Activity.SignUpActivity
import com.example.letmeknow.Fragment.ResultsFragment
import com.example.letmeknow.R
import com.example.letmeknow.adapter.MyAdapter
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EndTimeReceiver() :BroadcastReceiver() {
    private var db = Firebase.firestore
    override fun onReceive(context: Context?, intent: Intent?) {
        val docId = intent?.getStringExtra("docId")

        val i=Intent(context,SignInActivity::class.java)
        intent!!.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent= PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_IMMUTABLE)

        val builder= NotificationCompat.Builder(context!!,"saransh")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Poll is Ended")
            .setContentText("Click to View Results")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(123,builder.build())


        if (docId != null) {
            db.collection("Global").document(docId).delete()
            db.collection("Enddata").document(docId).delete()
            db.collection("Display").document(docId).update("display", FieldValue.increment(1))
        }
    }


}

