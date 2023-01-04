/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doanlaptrinhmang;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.io.IOException;

public class FirebaseConnection {

    private final FirebaseService fbs;

    public FirebaseConnection() throws IOException {
        fbs = new FirebaseService();

    }

    public LapTrinhMang getData() {
        LapTrinhMang lapTrinhMang;
        do {
            DatabaseReference ref = fbs.getDb().getReference("/");
            StringBuffer sb = new StringBuffer();
            Gson g = new Gson();
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    sb.append(dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.out.print("Error: " + error.getMessage());
                }
            });
            lapTrinhMang = g.fromJson(sb.toString(), LapTrinhMang.class);
        } while (lapTrinhMang == null);
        return lapTrinhMang;
    }
}
