package com.example.letmeknow.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.letmeknow.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //To hide Action and status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()
        binding.directsignin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val pass = binding.signupPass.text.toString()
            val repass = binding.signupRepass.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()&& repass.isNotEmpty()) {
                if (pass == repass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields are not allowed!!", Toast.LENGTH_SHORT).show()
            }
        }
        // Get the root view of the current activity
        val rootView: View = window.decorView.findViewById(android.R.id.content)

        rootView.setOnTouchListener { _, _ ->
            // Hide the soft keyboard
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(rootView.windowToken, 0)
            // Return false to indicate that the touch event has not been consumed
            false
        }

    }
}