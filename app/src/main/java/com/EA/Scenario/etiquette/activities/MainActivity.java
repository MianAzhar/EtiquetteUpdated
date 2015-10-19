package com.EA.Scenario.etiquette.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.adapters.EtiquetteListAdapter;
import com.EA.Scenario.etiquette.fragments.AddScenarioFragment;
import com.EA.Scenario.etiquette.fragments.EditProfileFragment;
import com.EA.Scenario.etiquette.fragments.IntroductionFragment;
import com.EA.Scenario.etiquette.fragments.LatestFragment;
import com.EA.Scenario.etiquette.fragments.PopularFragment;
import com.EA.Scenario.etiquette.fragments.ProfileFragment;
import com.EA.Scenario.etiquette.fragments.SearchFragment;
import com.EA.Scenario.etiquette.fragments.SignInFragment;
import com.EA.Scenario.etiquette.fragments.SignUpFragment;
import com.EA.Scenario.etiquette.services.GPSTracker;
import com.EA.Scenario.etiquette.utils.Constants;
import com.EA.Scenario.etiquette.utils.Etiquette;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static ArrayList<Etiquette> etiquetteList;
    public static EtiquetteListAdapter adapter;

    public static boolean askGps = true;

    public static GPSTracker gps;

    public static String userName = "";

    public static double latitude;
    public static double longitude;

    public static boolean showDialog = true;

    public static Location location;

    public static RequestQueue networkQueue;

    public DrawerLayout drawerLayout;
    NavigationView navigationView;

    Handler h1;
    Runnable r1;

    EditText search;

    public static GoogleApiClient mGoogleApiClient;

    public static LocationRequest mLocationRequest;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().hide();


        networkQueue = Volley.newRequestQueue(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(30 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        gps = new GPSTracker(this);



        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        MainActivity.etiquetteList = new ArrayList<>();

        adapter = new EtiquetteListAdapter(this, MainActivity.etiquetteList);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ImageView closeDrawer = (ImageView)findViewById(R.id.cancelMenu);
        closeDrawer.setOnClickListener(this);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        search = (EditText)findViewById(R.id.searchBox);
        search.setOnClickListener(this);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String name = search.getText().toString();
                    if (name.length() < 1) {
                        Toast.makeText(MainActivity.this, "Enter something", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    View v = getCurrentFocus();
                    if (v != null) {
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    Bundle args = new Bundle();
                    args.putString("text", name);

                    drawerLayout.closeDrawers();
                    SearchFragment newFrag = new SearchFragment();
                    newFrag.setArguments(args);
                    android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                    //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    trans.addToBackStack(null);
                    trans.replace(R.id.fragment_container, newFrag, Constants.SearchFragmentTag).commit();
                    return true;
                }
                return false;
            }
        });

        TextView signout = (TextView)findViewById(R.id.sign_out_button);
        signout.setOnClickListener(this);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {

            }

            @Override
            public void onDrawerOpened(View view) {

            }

            @Override
            public void onDrawerClosed(View view) {
                View v = getCurrentFocus();
                if (v != null) {
                    InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                EditText search = (EditText)findViewById(R.id.searchBox);
                search.setText("");
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state


              //  if (menuItem.isChecked()) {
                //    menuItem.setChecked(false);
                //}
                //else
                //{
                   // menuItem.setChecked(true);
                //}
                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    // For rest of the options we just show a toast on click

                    case R.id.discover:
                        PopularFragment newFrag = new PopularFragment();
                        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        trans.replace(R.id.fragment_container, newFrag, Constants.PopularFragmentTag).commit();
                        return true;
                    case R.id.addScenario:
                        AddScenarioFragment newFrag2 = new AddScenarioFragment();
                        android.support.v4.app.FragmentTransaction trans2 = getSupportFragmentManager().beginTransaction();
                        //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        trans2.addToBackStack(null);
                        trans2.replace(R.id.fragment_container, newFrag2, Constants.AddScenarioFragmentTag).commit();
                        return true;
                    case R.id.profile:
                        ProfileFragment newFrag1 = new ProfileFragment();
                        android.support.v4.app.FragmentTransaction trans1 = getSupportFragmentManager().beginTransaction();
                        //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        trans1.addToBackStack(null);
                        trans1.replace(R.id.fragment_container, newFrag1, Constants.ProfileFragmentTag).commit();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        h1 = new Handler();

        r1 = new Runnable() {

            @Override
            public void run() {
                location = gps.getLocation();
                h1.postDelayed(r1, 120000);

            }
        };

        SharedPreferences pref = getSharedPreferences(Constants.EtiquettePreferences, Context.MODE_PRIVATE);

        String user = pref.getString("userName", "");

        if(user.length() < 1) {
            IntroductionFragment newFrag = new IntroductionFragment();
            android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, Constants.IntroductionFragmentTag).commit();
        }
        else {
            pref = this.getSharedPreferences(Constants.EtiquettePreferences, Context.MODE_PRIVATE);
            String picture = pref.getString("Picture", "");
            String uname = pref.getString("Name" , "");
            ImageView imgview = (ImageView) this.findViewById(R.id.userpicture);
            TextView tv = (TextView) this.findViewById(R.id.unametv);
            tv.setText(uname);
            if(picture.length() > 0)
                Picasso.with(this).load(picture).into(imgview);


            userName = user;
            LatestFragment newFrag = new LatestFragment();
            android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, Constants.LatestFragmentTag).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        showDialog = true;
        networkQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
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

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.cancelMenu)
            drawerLayout.closeDrawers();
        else if(v.getId() == R.id.searchBox)
        {
            EditText searchText = (EditText)findViewById(R.id.searchBox);
            String str = searchText.getText().toString();

            if(str.length() < 1)
            {
                Toast.makeText(this, "Enter something", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle args = new Bundle();
            args.putString("text", str);

            drawerLayout.closeDrawers();
            SearchFragment newFrag = new SearchFragment();
            newFrag.setArguments(args);
            android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.addToBackStack(null);
            trans.replace(R.id.fragment_container, newFrag, Constants.SearchFragmentTag).commit();
        }
        else if(v.getId() == R.id.sign_out_button){
            SharedPreferences pref = getSharedPreferences(Constants.EtiquettePreferences, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.clear();
            editor.commit();

            if(drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawers();
            }

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            SignInFragment newFrag = new SignInFragment();

            android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, Constants.SignInFragmentTag);
            trans.commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.TAKE_PICTURE_SIGN_UP || requestCode == Constants.SELECT_PICTURE_SIGN_UP || requestCode == Constants.CROP_IMAGE_SIGN_UP)
        {
            SignUpFragment frag = (SignUpFragment)getSupportFragmentManager().findFragmentByTag(Constants.SignUpFragmentTag);
            if(frag != null && frag.isVisible())
            {
                frag.onActivityResult(requestCode, resultCode, data);
            }
        }
        else if(requestCode == Constants.TAKE_PICTURE_ADD_SCENARIO || requestCode == Constants.SELECT_PICTURE_ADD_SCENARIO || requestCode == Constants.CROP_IMAGE_ADD_SCENARIO)
        {
            AddScenarioFragment frag = (AddScenarioFragment)getSupportFragmentManager().findFragmentByTag(Constants.AddScenarioFragmentTag);
            if(frag != null && frag.isVisible())
            {
                frag.onActivityResult(requestCode, resultCode, data);
            }
        }
        else if(requestCode == Constants.TAKE_PICTURE_EDIT_PROFILE || requestCode == Constants.SELECT_PICTURE_EDIT_PROFILE || requestCode == Constants.CROP_IMAGE_EDIT_PROFILE)
        {
            EditProfileFragment frag = (EditProfileFragment)getSupportFragmentManager().findFragmentByTag(Constants.EditProfileFragmentTag);
            if(frag != null && frag.isVisible())
            {
                frag.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void onBackPressed() {


        if(drawerLayout.isDrawerOpen(navigationView)) {
            View v = getCurrentFocus();
            if (v != null) {
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            drawerLayout.closeDrawers();
            return;
        }

        Fragment popularFragment = getSupportFragmentManager().findFragmentByTag(Constants.PopularFragmentTag);
        Fragment latestFragment = getSupportFragmentManager().findFragmentByTag(Constants.LatestFragmentTag);
        Fragment categoriesFragment = getSupportFragmentManager().findFragmentByTag(Constants.CategoriesFragmentTag);
        Fragment choiceFragment = getSupportFragmentManager().findFragmentByTag(Constants.ChoiceFragmentTag);
        Fragment noChoiceFragment = getSupportFragmentManager().findFragmentByTag(Constants.NoChoiceFragmentTag);

        if ((popularFragment != null && popularFragment.isVisible())
                || (latestFragment != null && latestFragment.isVisible())
                || (categoriesFragment != null && categoriesFragment.isVisible())) {
            if (doubleBackToExitPressedOnce) {

                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
        else if((choiceFragment != null && choiceFragment.isVisible())
                || (noChoiceFragment != null && noChoiceFragment.isVisible())){
            LatestFragment newFrag = new LatestFragment();
            android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            trans.replace(R.id.fragment_container, newFrag, Constants.LatestFragmentTag).commit();
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            //handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location loc) {
        location = loc;
    }
}
