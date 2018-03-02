package org.confir.androidcarbontracker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by planetguy on 2/25/18.
 */

public class FirebaseUtil {

    static FirebaseDatabase database;

    static{
        database=FirebaseDatabase.getInstance();
    }

    public static void getTripCount(){
        DatabaseReference myTrips=database.getReference("myTrips");

        myTrips.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post = dataSnapshot.getValue(String.class);
                System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


}
