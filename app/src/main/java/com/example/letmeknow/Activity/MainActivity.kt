package com.example.letmeknow.Activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.letmeknow.Fragment.HomeFragment
import com.example.letmeknow.Fragment.ProfileFragment
import com.example.letmeknow.Fragment.MyPollFragment
import com.example.letmeknow.R
import com.example.letmeknow.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())



        //for bottom navigation bar
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.notifications -> replaceFragment(MyPollFragment())
                R.id.profile ->replaceFragment(ProfileFragment())
            }
            true
        }

        //for creating notification
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        //if condition for android version greater than Oreo i.e.8
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name:CharSequence="Let Me Know Channel"
            val description="Channel for End Time"
            val importance= NotificationManager.IMPORTANCE_HIGH
            val channel= NotificationChannel("saransh",name,importance)
            channel.description=description
            val notificationManager=getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager= supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame,fragment)
        fragmentTransaction.commit()
    }

    //for action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.about ->{
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.logout ->{
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                return true
            }else->{
                super.onOptionsItemSelected(item)
            }
        }
    }
}