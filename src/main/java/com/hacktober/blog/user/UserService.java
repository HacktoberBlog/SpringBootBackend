package com.hacktober.blog.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.hacktober.blog.email.EmailService;
import com.hacktober.blog.utils.Utils;

@Service
public class UserService {

	private static final String COLLECTION_NAME = "users";
	private static final String USERNAMES_DOC = "usernames";
	private final EmailService emailService;

	private final PasswordEncoder passwordEncoder;

	// Inject EmailService through constructor
	public UserService(EmailService emailService, PasswordEncoder passwordEncoder) {
		this.emailService = emailService;
		this.passwordEncoder = passwordEncoder;
	}

	/** Create User + Send Email */
	public String create(User user) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Save user to Firestore
		ApiFuture<WriteResult> result = db.collection(COLLECTION_NAME).document(user.getUsername()).set(user);

		String updateTime = result.get().getUpdateTime().toString();
		
		addUsername(user.getUsername());

		// ✅ Send email after successful Firestore write
		emailService.sendEmail(user.getEmail(), "Welcome to Hacktober Blog",
				"Hello " + user.getName() + ",\n\n" + "Your account has been successfully created.\n" + "Username: "
						+ user.getUsername() + "\n\n" + "Happy blogging!\nHacktober Blog Team");

		return updateTime;
	}

	/** Read/Get user by username */
	public User getByUsername(String username) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		DocumentReference docRef = db.collection(COLLECTION_NAME).document(username);
		DocumentSnapshot snapshot = docRef.get().get();
		return snapshot.exists() ? snapshot.toObject(User.class) : null;
	}

	/** Get all users */
	public List<User> getAll() throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME).get();
		List<QueryDocumentSnapshot> documents = query.get().getDocuments();
		List<User> users = new ArrayList<>();
		for (QueryDocumentSnapshot doc : documents) {
			users.add(doc.toObject(User.class));
		}
		return users;
	}

	/** Update User (updates fields, re-encrypts password if changed) */
	public String update(User user) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		if (user.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		ApiFuture<WriteResult> result = db.collection(COLLECTION_NAME).document(user.getUsername()).set(user);
		return result.get().getUpdateTime().toString();
	}

	/** Delete User by username */
	public String delete(String username) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> result = db.collection(COLLECTION_NAME).document(username).delete();
		
		removeUsername(username);
		return result.get().getUpdateTime().toString();
	}


	public List<String> getAllUsernames() throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		DocumentReference docRef = db.collection(COLLECTION_NAME).document(USERNAMES_DOC); // users/usernames
		DocumentSnapshot snapshot = docRef.get().get();

		if (snapshot.exists() && snapshot.contains("usernames")) {
			return (List<String>) snapshot.get("usernames");
		}
		return new ArrayList<>();
	}


	/** Add or remove username using atomic array ops; creates doc if missing */
	public String updateUsernames(String username, boolean add) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		DocumentReference docRef = db.collection(COLLECTION_NAME).document(USERNAMES_DOC); // users/usernames

		// Ensure the doc exists without clobbering existing fields
		docRef.set(Collections.singletonMap("usernames", Collections.emptyList()), SetOptions.merge()).get();

		ApiFuture<WriteResult> write = add
				? docRef.update("usernames", FieldValue.arrayUnion(username))
				: docRef.update("usernames", FieldValue.arrayRemove(username));

		return write.get().getUpdateTime().toString();
	}

	/** Helper: add a username */
	private void addUsername(String username) throws InterruptedException, ExecutionException {
		updateUsernames(username, true);
	}

	/** Helper: remove a username */
	public void removeUsername(String username) throws InterruptedException, ExecutionException {
		updateUsernames(username, false);
	}

	/** Reset password by email (returns true if a user was updated) */
	public boolean resetPasswordByEmail(String email, String rawNewPassword) {
		try {
			Firestore db = FirestoreClient.getFirestore();
			// Find the user doc by email (username is the doc id, but we don't know it here)
			ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME)
					.whereEqualTo("email", email)
					.limit(1)
					.get();

			List<QueryDocumentSnapshot> docs = future.get().getDocuments();
			if (docs.isEmpty()) {
				return false;
			}

			DocumentReference docRef = docs.get(0).getReference();
			String hash = passwordEncoder.encode(rawNewPassword);
			docRef.update("password", hash).get();
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Failed to reset password", e);
		}
	}
}
