package com.hacktober.blog.config;

import java.util.concurrent.ExecutionException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.hacktober.blog.user.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentSnapshot doc = db.collection("users").document(username).get().get();

            if (!doc.exists()) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            User user = doc.toObject(User.class);
            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            return UserPrincipal.create(user);
        } catch (InterruptedException | ExecutionException e) {
            throw new UsernameNotFoundException("Error loading user: " + username, e);
        }
    }
}
