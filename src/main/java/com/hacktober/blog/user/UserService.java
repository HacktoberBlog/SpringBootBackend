package com.hacktober.blog.user;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.hacktober.blog.login.entity.UserLoginEntity;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.hacktober.blog.email.EmailService;
import com.hacktober.blog.utils.Utils;

@Service
public class UserService implements UserDetailsService {

	private static final String COLLECTION_NAME = "users";
	private static final String USERNAMES_DOC = "usernames";
	private final EmailService emailService;

	// Inject EmailService through constructor
	public UserService(EmailService emailService) {
		this.emailService = emailService;
	}

	/** Create User + Send Email */
	public String create(User user) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		user.setPassword(Utils.encode(user.getPassword())); // Encrypt password

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
		/*Firestore db = FirestoreClient.getFirestore();
		DocumentReference docRef = db.collection(COLLECTION_NAME).document(username);
		DocumentSnapshot snapshot = docRef.get().get();
		return snapshot.exists() ? snapshot.toObject(User.class) : null;*/

		return  new User("Biswarup","biswa123","biswa@gmail","$2a$12$AQmmEx7ngHxqXs8hH8DaOO4TL/89IrAfeFU/trddzXnrOKzPCVjXK",List.of());
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
			user.setPassword(Utils.encode(user.getPassword()));
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
	        DocumentReference docRef = db.collection(COLLECTION_NAME).document(USERNAMES_DOC);
	        DocumentSnapshot snapshot = docRef.get().get();

	        if (snapshot.exists() && snapshot.contains("usernames")) {
	        	System.out.println(snapshot.get("usernames"));
	            return (List<String>) snapshot.get("usernames");
	        }
	        return new ArrayList<>();
	    }

	    /** Add or remove username from usernames array */
	    public String updateUsernames(String username, boolean add) throws InterruptedException, ExecutionException {
	        Firestore db = FirestoreClient.getFirestore();
	        DocumentReference docRef = db.collection("usernames").document("usernames");

	        List<String> currentUsernames = getAllUsernames();
	        if (add) {
	            if (!currentUsernames.contains(username)) {
	                currentUsernames.add(username);
	            }
	        } else {
	            currentUsernames.remove(username);
	        }

	        ApiFuture<WriteResult> result = docRef.update("usernames", currentUsernames);
	        return result.get().getUpdateTime().toString();
	    }

	    /** Helper: add a username */
	    private void addUsername(String username) throws InterruptedException, ExecutionException {
	        updateUsernames(username, true);
	    }

	    /** Helper: remove a username */
	    public void removeUsername(String username) throws InterruptedException, ExecutionException {
	        updateUsernames(username, false);
	    }

	@SneakyThrows
    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = getByUsername(username);
		if(user == null) throw  new UsernameNotFoundException("User name not found for email: "+username);
		return new UserLoginEntity(user.getUsername(),user.getPassword());
	}
}
