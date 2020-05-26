package com.rpifinal.hitema.partyGo.data.user.model

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import oldFiles.model.User

class UserDAO {
    companion object {
        private const val TAG = "UserHelper"
        // =========================================================================================
        // //////////////////////////////////////// ATTRIBUTES /////////////////////////////////////
        // =========================================================================================
        private const val COLLECTION_NAME = "users"

        private val usersCollection: CollectionReference
            get() = FirebaseFirestore.getInstance().collection(COLLECTION_NAME)

        val connectedUsers: Task<QuerySnapshot>
            get() = usersCollection.whereEqualTo("isConnected", "true").get()
        // =========================================================================================
        // /////////////////////////////////////////////////////////////////////////////////////////
        // =========================================================================================
        fun createUser(uid: String?, email: String?, firstName: String?, lastName: String?,
                       urlPicture: String?, isConnected: String?, token: String?, username: String? = null,  lvl: Int = 1, xp: Int = 0): Task<Void>
        {
            val userToCreate = User(uid, email, username, firstName, lastName, urlPicture, lvl, xp, isConnected, token)
            return usersCollection.document(uid!!).set(userToCreate)
        }

        fun createUser(user: com.rpifinal.hitema.partyGo.data.user.model.User): Task<Void> {
            Log.d(TAG, "Upload user")
            return usersCollection.document(user.uid!!).set(user)
        }
        // =========================================================================================
        // --- READ ---
        fun getUser(uid: String?): Task<DocumentSnapshot> {
            return usersCollection.document(uid!!).get()
        }
        // =========================================================================================
        // --- UPDATE ---
        fun updateUsername(username: String?, uid: String?): Task<Void> {
            return usersCollection.document(uid!!).update("username", username)
        }

        fun updateFirstName(firstName: String?, uid: String?): Task<Void> {
            return usersCollection.document(uid!!).update("firstName", firstName)
        }

        fun updateLastName(lastName: String?, uid: String?): Task<Void> {
            return usersCollection.document(uid!!).update("lastName", lastName)
        }

        fun updateIsConnected(isConnected: String?, uid: String?): Task<Void> {
            return usersCollection.document(uid!!).update("isConnected", isConnected)
        }

        fun updateToken(token: String?, uid: String?): Task<Void> {
            return usersCollection.document(uid!!).update("token", token)
        }
        // =========================================================================================
        // --- DELETE ---
        fun deleteUser(uid: String?): Task<Void> {
            return usersCollection.document(uid!!).delete()
        }
        // =========================================================================================
        // /////////////////////////////////////////////////////////////////////////////////////////
        // =========================================================================================
    }
}