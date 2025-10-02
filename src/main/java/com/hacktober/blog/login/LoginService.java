package com.hacktober.blog.login;

import java.util.concurrent.ExecutionException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.hacktober.blog.user.User;
import com.hacktober.blog.utils.Utils;

@Service
public class LoginService {

    private final PasswordEncoder passwordEncoder;

    public LoginService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(String username, String rawPassword) throws InterruptedException, ExecutionException {
        DocumentReference docRef = FirestoreClient.getFirestore()
                .collection("users")
                .document(username);

        DocumentSnapshot doc = docRef.get().get();
        if (!doc.exists()) return false;

        User user = doc.toObject(User.class);
        if (user == null || user.getPassword() == null) return false;

        String stored = user.getPassword();

        // 1) If it's BCrypt, verify with PasswordEncoder
        if (isBcrypt(stored)) {
            return passwordEncoder.matches(rawPassword, stored);
        }

        // 2) Legacy path (Base64 via Utils) — if it matches, rehash to BCrypt now
        boolean legacyOk = Utils.match(rawPassword, stored);
        if (legacyOk) {
            try {
                String bcrypt = passwordEncoder.encode(rawPassword);
                docRef.update("password", bcrypt).get(); // silent upgrade
            } catch (Exception ignored) {

            }
        }
        return legacyOk;
    }

    private boolean isBcrypt(String value) {
        if (value == null) return false;
        // BCrypt hashes commonly start with $2a$, $2b$, or $2y$
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }
}
