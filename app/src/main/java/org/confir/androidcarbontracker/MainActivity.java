package org.confir.androidcarbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseApp firebaseApp;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment addActivity = new AddActivityFragment();

        setFragment(addActivity);
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

        double[] carbonPerMile = {0, 0, 1, 0.5};

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
            Toast.makeText(view.getContext(),
                    "Added click handler",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(final View view) {
            Spinner s = (Spinner) view.getRootView().findViewById(R.id.transportationSpinner);
            if(s==null)
                return;
            int transitMeansType = s.getSelectedItemPosition();
            EditText textbox=(EditText) view.getRootView().findViewById(R.id.distanceEditText);
            try{
                final double distance=Double.parseDouble(textbox.getText().toString());
                final DatabaseReference myHome=FirebaseDatabase.getInstance(firebaseApp).getReference()
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
                        .child(codeNames[transitMeansType])
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Number totalDistanceAsNum=(Number)dataSnapshot.getValue();
                                double totalDistance;
                                if(totalDistanceAsNum == null)
                                    totalDistance = 0.0;
                                else
                                    totalDistance=totalDistanceAsNum.doubleValue();
                                totalDistance+=distance;
                                myHome.child("distance-stats")
                                        .child(dataSnapshot.getKey()).setValue(totalDistance);

                                Toast.makeText(view.getContext(),
                                        "Added trip",
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            } catch(NumberFormatException e) {

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


            String[] UserArray = {"User1", "User1", "User1", "User1",
                    "User1", "User1", "User1", "User1"};


            ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, UserArray);


            ListView listView = (ListView) view.findViewById(R.id.LeaderView);
            listView.setAdapter(listViewAdapter);
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
    public static class ProfileFragment extends Fragment {
        //To set the layout for the fragment, we must override 2 methods
        //onCreateView() and onViewCreated()
        //onCreateView() returns the view for the fragment.

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_profile, null);
        }

        //onViewCreated() allows for activity methods to be created
        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            EditText nameEditText = (EditText) view.findViewById(R.id.nameEditText);
            nameEditText.setText("Name");//set the text in edit text

            EditText emailEditText = (EditText) view.findViewById(R.id.emailEditText);
            emailEditText.setText("Email");//set the text in edit text

            EditText collegeEditText = (EditText) view.findViewById(R.id.collegeEditText);
            collegeEditText.setText("College");//set the text in edit text
        }
    }

    public void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
