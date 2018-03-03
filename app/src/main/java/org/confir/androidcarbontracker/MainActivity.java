package org.confir.androidcarbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseApp firebaseApp;

    private static String displayName;
    public static boolean firstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseApp=FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance(firebaseApp);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set the default nav drawer page
        if(!firstTime) {
            Fragment addActivity = new AddActivityFragment();
            setFragment(addActivity);
        } else {
            Fragment profileFragment = new ProfileFragment();
            setFragment(profileFragment);
            firstTime = false;
        }


        FirebaseDatabase.getInstance(firebaseApp)
                .getReference()
                .child("android")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("profile")
                .child("name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        displayName=dataSnapshot.getValue(String.class);
                        if(displayName == null || displayName.equals(""))
                            displayName="Anonymous";

                        //set nav name
                        TextView navNameTextView = (TextView) findViewById(R.id.navNameTextView);
                        navNameTextView.setText(displayName);

                        //set nav email
                        TextView navEmailTextView = (TextView) findViewById(R.id.navEmailTextView);
                        navEmailTextView.setText(firebaseAuth.getCurrentUser().getEmail());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
        } else if (id == R.id.nav_leaderboard) {
            fragment = new LeaderboardFragment();
        } else if (id == R.id.nav_view_trips) {

        } else if (id == R.id.nav_add_activity) {
            fragment = new AddActivityFragment();
        } else if (id == R.id.nav_legal) {
            fragment = new LegalFragment();
        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            goToLoginActivity();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction().addToBackStack(null);

            //replace from content_main.xml
            ft.replace(R.id.screen_area, fragment);

            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //replace from content_main.xml
        ft.replace(R.id.screen_area, fragment);

        ft.commit();
    }

    // Fragments
    public static class AboutFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_about, null);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }

    public static class AddActivityFragment extends Fragment implements View.OnClickListener {

        String[] arraySpinner = {"Bike", "Walking", "Car", "Bus"};
        String[] codeNames = {"bike", "foot", "car", "bus"};
        //Conversion factors
        double[] scorePerMile = {12, 12, -4, -2};

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_add_activity, null);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Spinner s = (Spinner) view.findViewById(R.id.transportationSpinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            s.setAdapter(adapter);

            Button submitButton=(Button) view.findViewById(R.id.submitBtn);

            submitButton.setOnClickListener(this);
//            Toast.makeText(view.getContext(),
//                    "Added click handler",
//                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(final View view) {
            Spinner s = (Spinner) view.getRootView().findViewById(R.id.transportationSpinner);
            if(s==null)
                return;
            final int transitMeansType = s.getSelectedItemPosition();
            EditText textbox=(EditText) view.getRootView().findViewById(R.id.distanceEditText);
            try{
                final double distance=Double.parseDouble(textbox.getText().toString());
                final DatabaseReference databaseRoot=FirebaseDatabase.getInstance(firebaseApp).getReference()
                        .child("android");
                final DatabaseReference myHome=databaseRoot
                        .child(firebaseAuth.getCurrentUser().getUid());

                Long timestamp = (Long) System.currentTimeMillis()/1000l;

                /**************************************************
                        Add the leg
                 **************************************************/
                DatabaseReference leg=myHome
                        .child("legs")
                        .child(timestamp+"");
                leg.child("start").setValue(timestamp);
                leg.child("means").setValue(codeNames[transitMeansType]);
                leg.child("distance").setValue(distance);
                Log.e("a", leg.toString());

                /**************************************************
                 Add the trip
                 ***************************************************/
                myHome
                        .child("trips")
                        .child("trip-"+timestamp)
                        .setValue("1");

                /*************************************************
                 * Update User Stats
                 *************************************************/

                myHome.child("distance-stats")
                        .addListenerForSingleValueEvent(new ScoreAndUpdateListener(transitMeansType, distance, myHome, view, databaseRoot));


            } catch(NumberFormatException e) {

            }
        }

        private class ScoreAndUpdateListener implements ValueEventListener {
            private final int transitMeansType;
            private final double distance;
            private final DatabaseReference myHome;
            private final View view;
            private final DatabaseReference databaseRoot;

            public ScoreAndUpdateListener(int transitMeansType, double distance, DatabaseReference myHome, View view, DatabaseReference databaseRoot) {
                this.transitMeansType = transitMeansType;
                this.distance = distance;
                this.myHome = myHome;
                this.view = view;
                this.databaseRoot = databaseRoot;
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalCarbon=100;
                for (int i = 0; i < codeNames.length; i++) {
                    Number totalDistanceFromDB = (Number) dataSnapshot.child(codeNames[i]).getValue();

                    double totalDistance;
                    if (totalDistanceFromDB == null)
                        totalDistance = 0.0;
                    else
                        totalDistance = totalDistanceFromDB.doubleValue();

                    Log.d("1", totalDistance + "");

                    if (i == transitMeansType) {
                        totalDistance += distance;
                        myHome.child("distance-stats")
                                .child(codeNames[i])
                                .setValue(totalDistance);
                    }
                    Log.d("2", totalDistance + "");
                    totalCarbon += totalDistance * scorePerMile[i];

                }

                myHome.child("profile")
                        .child("total-carbon").setValue(totalCarbon);

                Toast.makeText(view.getContext(),
                        "Added trip",
                        Toast.LENGTH_SHORT).show();

                /*************************
                 * Update LEaderboard
                 ****************************/

                final double totalScore=totalCarbon;

                databaseRoot.child("leaderboard").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot leaderboardSnapshot) {
                        DatabaseReference leaderboard=databaseRoot.child("leaderboard");
                        //Used when we start shifting users down a place
                        double mostRecentScore = totalScore;
                        String lastUsername=displayName;
                        String lastUid=firebaseAuth.getCurrentUser().getUid();

                        int i;

                        for(i=1; i<=10; i++){
                            //Log.d("foo", "Skipping "+i);
                            DataSnapshot currentPlacer = leaderboardSnapshot.child(Integer.toString(i));
                            double currentScoreD = ((Number)currentPlacer.child("score").getValue()).doubleValue();
                            //Log.d("foo", "totalScore: "+totalScore);
                            //Log.d("foo", "oldScore: "+currentPlacerScore);
                            if(currentScoreD < totalScore){
                                break;
                            }
                        }

                        for(; i<=10; i++){
                            //Log.d("foo", "Moving "+i);
                            DataSnapshot snapshot = leaderboardSnapshot.child(Integer.toString(i));

                            leaderboard.child(Integer.toString(i)).child("score").setValue(mostRecentScore);
                            leaderboard.child(Integer.toString(i)).child("user").setValue(lastUsername);
                            leaderboard.child(Integer.toString(i)).child("uid").setValue(lastUid);

                            mostRecentScore = ((Number)snapshot.child("score").getValue()).doubleValue();
                            lastUsername=snapshot.child("user").getValue(String.class);
                            lastUid=snapshot.child("uid").getValue(String.class);
                            if(lastUid==null)lastUid="";

                            if(lastUid.equals(firebaseAuth.getCurrentUser().getUid()))
                                break;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }
    }


    public static class LeaderboardFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_leader_board, null);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            final String[] UserArray = new String[10];
            for(int i=0; i<UserArray.length; i++){
                UserArray[i]="";
            }


            final ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, UserArray);


            ListView listView = (ListView) view.findViewById(R.id.LeaderView);
            listView.setAdapter(listViewAdapter);

            FirebaseDatabase.getInstance(firebaseApp).getReference()
                    .child("android")
                    .child("leaderboard")
                    .addValueEventListener(new ValueEventListener(){

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(int i=0; i<10; i++){
                                DataSnapshot currentPlacer = dataSnapshot.child(Integer.toString(i+1));

                                double score = ((Number)currentPlacer.child("score").getValue()).doubleValue();
                                String uid=currentPlacer.child("user").getValue(String.class);

                                UserArray[i]=(i+1)+": "+uid+" (score: "+((int)score)+")";
                            }
                            listViewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    public static class LegalFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_legal, null);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }

    //Every fragment needs a layout file
    public static class ProfileFragment extends Fragment implements View.OnClickListener, ValueEventListener {
        private EditText nameEditText;
        private EditText yearEditText;
        private EditText collegeEditText;
        //To set the layout for the fragment, we must override 2 methods
        //onCreateView() and onViewCreated()
        //onCreateView() returns the view for the fragment.

        private int lastChangedByFirebaseCounter=0;
        private int lastChangedByUserCounter=0;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_profile, null);
        }

        //onViewCreated() allows for activity methods to be created
        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            nameEditText = (EditText) view.findViewById(R.id.nameEditText);
            yearEditText = (EditText) view.findViewById(R.id.yearEditText);
            collegeEditText = (EditText) view.findViewById(R.id.collegeEditText);

            Button button=view.findViewById(R.id.profile_save_button);
            button.setOnClickListener(this);

            //User's home node
            DatabaseReference userHome = FirebaseDatabase.getInstance(firebaseApp)
                    .getReference()
                    .child("android")
                    .child(firebaseAuth.getCurrentUser().getUid());

            DatabaseReference profile = userHome.child("profile");

            profile.addValueEventListener(this);
        }

        @Override
        public void onDataChange(DataSnapshot profile) {
           if(profile.hasChild("name")){
               nameEditText.setText(profile.child("name").getValue().toString());
           }
            if(profile.hasChild("year")){
                yearEditText.setText(profile.child("year").getValue().toString());
            }
            if(profile.hasChild("college")){
                collegeEditText.setText(profile.child("college").getValue().toString());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onClick(View view) {
//
//            //Ignore if we've just changed it by pulling from Firebase
//            // If not, this changes Firebase, then Firebase notices a change and changes the text
//            // field, which fires this event and repeat infinitely.
//            if(lastChangedByFirebaseCounter > 0){
//                lastChangedByFirebaseCounter--;
//                return;
//            }
//
//            lastChangedByUserCounter++;

            //User's home node
            DatabaseReference userHome = FirebaseDatabase.getInstance(firebaseApp)
                    .getReference()
                    .child("android")
                    .child(firebaseAuth.getCurrentUser().getUid());

            DatabaseReference profile = userHome.child("profile");

            String name=this.nameEditText.getText().toString();
            profile.child("name").setValue(name);

            String year=this.yearEditText.getText().toString();
            profile.child("year").setValue(year);

            String college=this.collegeEditText.getText().toString();
            profile.child("college").setValue(college);

            String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
            profile.child("email").setValue(email);
        }
    }

    public void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
