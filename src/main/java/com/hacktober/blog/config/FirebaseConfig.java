package com.hacktober.blog.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
	private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

	@Value("${firebase.credentials.path:${FIREBASE_ADMIN_SA_PATH:}}")
	private String credentialsPath;

	@Value("${firebase.project-id:${FIREBASE_PROJECT_ID:}}")
	private String projectId;

	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		if (!FirebaseApp.getApps().isEmpty()) return FirebaseApp.getInstance();

		FirebaseOptions.Builder builder = FirebaseOptions.builder();

		if (credentialsPath != null && !credentialsPath.isBlank()) {
			try (FileInputStream in = new FileInputStream(credentialsPath)) {
				builder.setCredentials(GoogleCredentials.fromStream(in));
				log.info("Firebase: using service account from {}", credentialsPath);
			}
		} else {
			builder.setCredentials(GoogleCredentials.getApplicationDefault());
			log.info("Firebase: using application default credentials");
		}

		if (projectId != null && !projectId.isBlank()) builder.setProjectId(projectId);

		FirebaseApp app = FirebaseApp.initializeApp(builder.build());
		log.info("Firebase initialized (projectId={})", projectId);
		return app;
	}

	@Bean
	public Firestore firestore(FirebaseApp app) {
		FirestoreOptions.Builder fo = FirestoreOptions.newBuilder();
		if (projectId != null && !projectId.isBlank()) fo.setProjectId(projectId);
		return fo.build().getService();
	}
}
