package com.hacktober.blog.login;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.hacktober.blog.user.User;
import com.hacktober.blog.utils.Utils;

@Service
public class LoginService {

    public boolean login(String username, String password) throws InterruptedException, ExecutionException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot doc = db.collection("users").document(username).get().get();

        if (!doc.exists()) {
            return false; // user not found
        }

        User user = doc.toObject(User.class);
        if (user == null) {
            return false;
        }

        // Encrypt the incoming password and compare
        return Utils.match(password, user.getPassword());
    }
}
