
package com.example.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private var mFirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registration_btn = findViewById<TextView>(R.id.registrationBtn)

        /**
         *  Capturing the details from user which are later changed to strings
        */
        registration_btn.setOnClickListener {
            val name = findViewById<EditText>(R.id.editTextName).text.toString()
            val phone = findViewById<EditText>(R.id.editTextPhone).text.toString()
            val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val password = findViewById<EditText>(R.id.editTextpassword).text.toString()

            /**
             * creating a user on firebase
             */
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener { registrationProcess ->
                        if (registrationProcess.isSuccessful){
                            /**
                             * If the user is create then we can store all the user
                             * details on the firebase database using a data class called user.kt
                             * The users will be stored under a collection called users
                             * This is implemented using the function below
                             * When the user is registered, gets a unique id and this will be stored too
                             */
                            val currentUserId = mFirebaseAuth.currentUser!!.uid
                            val userDetailsToStore = User( currentUserId, name, phone, email )

                            // Passing the userDetailsToStore object into the registerUser() function
                            registerUser(userDetailsToStore)
                        }
                    }
                )

        }
    }

    fun registerUser(userData: User){
        firebaseStore.collection("users")
            .document()
            // Using the fields created in the  class with help of UserInfo
            .set(userData).addOnSuccessListener {
                // Displaying a success message to indicate that registration is successful

                Toast.makeText(this,"Registered Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
                // If the registration fails, Display a message
            .addOnFailureListener { e ->
                Log.e( javaClass.simpleName,
                    "Error while registering.",
                    e
                )
            }
    }

}