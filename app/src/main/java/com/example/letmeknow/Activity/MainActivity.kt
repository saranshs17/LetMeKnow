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
    private lateinit var bottomNavView : BottomNavigationView
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
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
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
            R.id.search ->{
                Toast.makeText(this,"You clicked Search", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.about ->{
                Toast.makeText(this,"You clicked About", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.settings ->{
                Toast.makeText(this,"You clicked Settings", Toast.LENGTH_LONG).show()
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