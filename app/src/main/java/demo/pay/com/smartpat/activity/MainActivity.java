package demo.pay.com.smartpat.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import demo.pay.com.smartpat.R;
import demo.pay.com.smartpat.fragment.MainFragment;
import demo.pay.com.smartpat.fragment.SendMoneyFragment;
import demo.pay.com.smartpat.utility.AppConstant;
import demo.pay.com.smartpat.utility.Utility;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,MainFragment.OnFragmentInteractionListener,SendMoneyFragment.OnFragmentInteractionListener {

    public FirebaseAuth auth;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth= FirebaseAuth.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUserProfileInSlidingMenu();
        setHomeFragment();
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
            MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.class.getSimpleName());
            if(mainFragment !=null){
                mainFragment.toogleProgressBar();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_logout) {
                auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //signup listner
  /*  FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        }
    };*/

    public void setHomeFragment(){
        pushFragments(AppConstant.FRAG_HOME,new MainFragment(),R.id.main_layout);
    }

    public void setUserProfileInSlidingMenu(){
        View headerLayout = navigationView.getHeaderView(0);
        TextView emailView = headerLayout.findViewById(R.id.nav_header_email);
        TextView nameView = headerLayout.findViewById(R.id.nav_header_name);
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser !=null){
            emailView.setText(firebaseUser.getEmail());
            String displayName = firebaseUser.getDisplayName();
            if(displayName != null){
                String[] arr = displayName.split(":");
                if(arr != null && arr.length==2){
                    nameView.setText(arr[0] +" as a :"+arr[1]);
                    SharedPreferences sf = Utility.getSharedPref(this);
                    SharedPreferences.Editor editor = sf.edit();
                    editor.putString("UserProfile",arr[1]);
                    editor.putString("UserEmail",firebaseUser.getEmail());
                    editor.commit();


                }
            }
        }

    }
    @Override
    public void onFragmentInteraction() {

    }

    //Send Money Fragment
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
