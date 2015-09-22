package com.EA.Scenario.etiquette.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.EA.Scenario.etiquette.R;
import com.EA.Scenario.etiquette.adapters.EtiquetteListAdapter;
import com.EA.Scenario.etiquette.fragments.AddScenarioFragment;
import com.EA.Scenario.etiquette.fragments.IntroductionFragment;
import com.EA.Scenario.etiquette.fragments.ProfileFragment;
import com.EA.Scenario.etiquette.fragments.SearchFragment;
import com.EA.Scenario.etiquette.utils.Etiquette;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<Etiquette> etiquetteList;
    public static EtiquetteListAdapter adapter;
    public DrawerLayout drawerLayout;
    NavigationView navigationView;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().hide();

        MainActivity.etiquetteList = new ArrayList<Etiquette>();
        adapter = new EtiquetteListAdapter(this, MainActivity.etiquetteList);

        MainActivity.etiquetteList.add(new Etiquette("It is often crowded in economy class in plane rides. We understand everyone wants to get as much leg room as possible.", "TRAVEL", 1));
        MainActivity.etiquetteList.add(new Etiquette("It is often crowded in economy class in plane rides. We understand everyone wants to get as much leg room as possible.", "TOILET", 1));
        MainActivity.etiquetteList.add(new Etiquette("It is often crowded in economy class in plane rides. We understand everyone wants to get as much leg room as possible.", "TRAVEL", 1));
        MainActivity.etiquetteList.add(new Etiquette("It is often crowded in economy class in plane rides. We understand everyone wants to get as much leg room as possible.", "TRAVEL", 1));
        MainActivity.etiquetteList.add(new Etiquette("It is often crowded in economy class in plane rides. We understand everyone wants to get as much leg room as possible.", "TRAVEL", 1));

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ImageView closeDrawer = (ImageView)findViewById(R.id.cancelMenu);
        closeDrawer.setOnClickListener(this);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    // For rest of the options we just show a toast on click

                    case R.id.discover:
                        SearchFragment newFrag = new SearchFragment();
                        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                        //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        trans.addToBackStack(null);
                        trans.replace(R.id.fragment_container, newFrag, "SearchFragment").commit();
                        return true;
                    case R.id.addScenario:
                        AddScenarioFragment newFrag2 = new AddScenarioFragment();
                        android.support.v4.app.FragmentTransaction trans2 = getSupportFragmentManager().beginTransaction();
                        //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        trans2.addToBackStack(null);
                        trans2.replace(R.id.fragment_container, newFrag2, "Add ScenarioFragment").commit();
                        return true;
                    case R.id.profile:
                        ProfileFragment newFrag1 = new ProfileFragment();
                        android.support.v4.app.FragmentTransaction trans1 = getSupportFragmentManager().beginTransaction();
                        //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        trans1.addToBackStack(null);
                        trans1.replace(R.id.fragment_container, newFrag1, "ProfileFragment").commit();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        IntroductionFragment newFrag = new IntroductionFragment();
        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        trans.replace(R.id.fragment_container, newFrag, "IntroductionFragment").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.cancelMenu)
            drawerLayout.closeDrawers();
    }

    public void onBackPressed() {


        if(drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();
            return;
        }

        Fragment popularFragment = (Fragment) getSupportFragmentManager().findFragmentByTag("PopularFragment");
        Fragment latestFragment = getSupportFragmentManager().findFragmentByTag("LatestFragment");
        Fragment categoriesFragment = getSupportFragmentManager().findFragmentByTag("CategoriesFragment");

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
        } else {
            super.onBackPressed();
            return;
        }

    }

}
