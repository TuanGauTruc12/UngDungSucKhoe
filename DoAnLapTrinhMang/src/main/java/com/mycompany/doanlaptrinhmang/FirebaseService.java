package com.mycompany.doanlaptrinhmang;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseService {

    private final FirebaseDatabase db;

    public FirebaseService() throws IOException {
        final String fileJson = System.getProperty("user.dir") + "\\src\\main\\java\\com\\mycompany\\lib\\laptrinhmangmaytinh.json";
        FileInputStream serviceAccount = new FileInputStream(fileJson);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://lt-mang-622c4-default-rtdb.firebaseio.com/")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
        db = FirebaseDatabase.getInstance();
    }

    public FirebaseDatabase getDb() {
        return db;
    }
}
