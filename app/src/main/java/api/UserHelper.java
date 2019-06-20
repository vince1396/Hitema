package api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rpifinal.hitema.model.User;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // =============================================================================================
    // --- COLLECTION REFERENCE ---
    public static CollectionReference getUsersCollection(){

        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }
    // =============================================================================================
    // --- CREATE ---
    public static Task<Void> createUser(String uid, String email, String username, String firstName,
                                        String lastName, String urlPicture, int lvl, int xp, boolean isConnected)
    {
        User userToCreate = new User(uid, email, username, firstName, lastName, urlPicture, lvl, xp, isConnected);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }
    // =============================================================================================
    // --- READ ---
    public static Task<DocumentSnapshot> getUser(String uid) {

        return UserHelper.getUsersCollection().document(uid).get();
    }
    // =============================================================================================
    // --- UPDATE ---
    public static Task<Void> updateUsername(String username, String uid) {

        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateFirstName(String firstName, String uid)
    {

        return UserHelper.getUsersCollection().document(uid).update("firstName", firstName);
    }

    public static Task<Void> updateLastName(String lastName, String uid)
    {
        return UserHelper.getUsersCollection().document(uid).update("lastName", lastName);
    }

    public static Task<Void> updateIsConnected(boolean isConnected, String uid)
    {
        return UserHelper.getUsersCollection().document(uid).update("isConnected", isConnected);
    }
    // =============================================================================================
    // --- DELETE ---
    public static Task<Void> deleteUser(String uid) {

        return UserHelper.getUsersCollection().document(uid).delete();
    }
}