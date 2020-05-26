package com.rpifinal.hitema.partyGo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.rpifinal.hitema.partyGo.data.user.model.User
import com.rpifinal.hitema.partyGo.data.user.model.UserDAO
import javax.inject.Inject

class MainActivity @Inject constructor(): BaseActivity() {
    // =============================================================================================
    // ////////////////////////////////////// ATTRIBUTES ///////////////////////////////////////////
    // =============================================================================================
    private val TAG = "MainActivity"

    private var RC_SIGN_IN = 123 // Random code for authentication

    // Choose authentication providers
    private val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
    )
    // =============================================================================================
    // //////////////////////////////////////// METHODS ////////////////////////////////////////////
    // =============================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startLogin()
    }
    // =============================================================================================
    // Start Firebase authentication process. Calls onActivityResult() as callback
    private fun startLogin() {
        Log.d(TAG, "Starting login")
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }
    // =============================================================================================
    // Callback function for startActivityForResult()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult")
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "Getting response")
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                Log.d(TAG, "Successfully signed in")
                createUserInFirestore()
            } else {
                if (response == null) {
                    Log.w(TAG, "Login Canceled")
                } else {
                    Log.w(TAG, "Error login")
                    response.error?.errorCode
                }
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    // =============================================================================================
    private fun createUserInFirestore() {
        fun create() {
            Log.d(TAG, "Creating User")

            val user = User(
                    uid = getCurrentUser()!!.uid,
                    email = getCurrentUser()?.email!!,
                    phoneNumber = getCurrentUser()!!.phoneNumber,
                    photoUrl = getCurrentUser()!!.photoUrl.toString(),
                    firstName = null,
                    lastName = null)

            UserDAO.createUser(user)
        }
        val db = FirebaseFirestore.getInstance()

        // Fetch in the database a user with current user's uid
        val docRef = db.collection("users").document(getCurrentUser()!!.uid)

        // We try to get the user previously fetched
        docRef.get()
                .addOnSuccessListener { document -> // On succes
                    val intent = Intent(this, PocActivity::class.java)
                    if (document.exists()) { // If current user already exists in DB
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        startActivity(intent)
                    } else { // If current user doesn't exist in DB
                        Log.d(TAG, getCurrentUser()!!.uid)
                        Log.d(TAG, "No such document")
                        create()
                        startActivity(intent)
                    }
                }
                .addOnFailureListener { exception -> // On failure
                    Log.d(TAG, "get failed with ", exception)
                }
    }
    // =============================================================================================
    // /////////////////////////////////////////// END /////////////////////////////////////////////
    // =============================================================================================
}
