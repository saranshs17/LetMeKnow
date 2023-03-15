package com.example.letmeknow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
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
                R.id.home-> replaceFragment(HomeFragment())
                R.id.notifications-> replaceFragment(NotificationsFragment())
                R.id.profile->replaceFragment(ProfileFragment())
            }
            true
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
            R.id.search->{
                Toast.makeText(this,"You clicked Search", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.about->{
                Toast.makeText(this,"You clicked About", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.settings->{
                Toast.makeText(this,"You clicked Settings", Toast.LENGTH_LONG).show()
                return true
            }else->{
                super.onOptionsItemSelected(item)
            }
        }
    }
}