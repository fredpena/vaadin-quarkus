package dev.fredpena.app.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import dev.fredpena.app.data.User;
import dev.fredpena.app.exception.FirebaseException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author me@fredpena.dev
 * @created 22/01/2024  - 11:57
 */
@ApplicationScoped
@Slf4j
public class FirebaseService {

    private static final String CREDENTIALS_FILE_PATH = "/google-credentials.json";

    @PostConstruct
    void postConstruct() throws IOException {
        final InputStream in = getClass().getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(in))
                .build();

        boolean hasBeenInitialized = false;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        for (FirebaseApp app : firebaseApps) {
            if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                hasBeenInitialized = true;
            }
        }
        if (!hasBeenInitialized) {
            FirebaseApp.initializeApp(options);
        }

    }

    public void login(String email, String password) throws FirebaseException {

    }


    public void createUser(User user) throws FirebaseException {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(user.getEmail())
                    .setEmailVerified(user.isEmailVerified())
                    .setPassword(user.getPassword())
                    .setPhoneNumber(user.getPhone())
                    .setDisplayName(user.getDisplayName())
                    .setPhotoUrl(user.getPhotoUrl())
                    .setDisabled(user.isDisabled());

            FirebaseAuth.getInstance().createUser(request);
        } catch (IllegalArgumentException | FirebaseAuthException e) {
            throw new FirebaseException(e);
        }
    }

    public Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public UserRecord findUserByEmail(String username) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().getUserByEmail(username);
    }


}
