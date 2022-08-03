package com.hottechnologies.onewood.view;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.hottechnologies.onewood.R;
import com.hottechnologies.onewood.adapter.ServerListRVAdapter;
import com.hottechnologies.onewood.interfaces.ChangeServer;
import com.hottechnologies.onewood.interfaces.NavItemClickListener;
import com.hottechnologies.onewood.model.Server;

import java.util.ArrayList;

import com.hottechnologies.onewood.Utils;

public class MainActivity extends AppCompatActivity implements NavItemClickListener {

    public static MainActivity _instance;
    private FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    private Fragment fragment;
    private RecyclerView serverListRv;
    private ArrayList<Server> serverLists;
    ArrayList<Server> filterList = new ArrayList<>();
    private ServerListRVAdapter serverListRVAdapter;
    private DrawerLayout drawer;
    private ChangeServer changeServer;
    ImageView bbacktomain;
    EditText searchServer;

    LinearLayout account,rateus, feedback,setting,referfriend,privacypolicy,updatepremium,SpeedTest;
    public static final String TAG = "One Wood Vpn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all variable
        initializeAll();

        ImageButton menuRight = findViewById(R.id.navbar_right);
        ImageButton menuLeft = findViewById(R.id.navbar_left);

//        Left Navigation Categories.....
        account = findViewById(R.id.account);
        rateus = findViewById(R.id.rateus);
        referfriend = findViewById(R.id.tellfriend);
        feedback = findViewById(R.id.feedback);
        setting = findViewById(R.id.setting);
        privacypolicy = findViewById(R.id.privacypolicy);
        updatepremium = findViewById(R.id.updatepremium);
        SpeedTest = findViewById(R.id.speed);

        searchServer = findViewById(R.id.searchServer);

        LinearLayout btn = findViewById(R.id.gotoPremium);

        bbacktomain = findViewById(R.id.backtomain);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        searchServer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    filter(editable.toString());
            }
        });

        updatepremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Account Activity", Toast.LENGTH_SHORT).show();
                    }
                });

        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Feedback Activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });


        referfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"Download the App and Free Vpn Use \n\n https://play.google.com/store/apps/details?id="+getPackageName());
                startActivity(Intent.createChooser(intent,"Choose One"));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Feedback Activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
                startActivity(intent);
            }
        });

        SpeedTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SpeedTestActivity.class);
                startActivity(intent);
            }
        });
        menuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        });

        menuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, go_to_premium.class);
                startActivity(intent);
                finish();
            }
        });

        bbacktomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
            }
        });
        transaction.add(R.id.container, fragment);
        transaction.commit();

        // Server List recycler view initialize
        if (serverLists != null) {
            serverListRVAdapter = new ServerListRVAdapter(serverLists, this);
            serverListRv.setAdapter(serverListRVAdapter);
        }

    }

    /**
     * Initialize all object, listener etc
     */
    private void initializeAll() {
        drawer = findViewById(R.id.drawer_layout);

        fragment = new MainFragment();
        serverListRv = findViewById(R.id.serverListRv);
        serverListRv.setHasFixedSize(true);

        serverListRv.setLayoutManager(new LinearLayoutManager(this));

        serverLists = getServerList();
        changeServer = (ChangeServer) fragment;

    }

    /**
     * Close navigation drawer
     */
    public void closeDrawer() {

        if(MainFragment.vpnStart || MainFragment.vpnConnecting)
        {
            Toast.makeText(MainActivity.this, "Please Disconnect the Server", Toast.LENGTH_SHORT).show();
            return;
        }
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            drawer.openDrawer(GravityCompat.END);
        }

    }

    /**`
     * Generate server array list
     */

    /**
     * On navigation item click, close drawer and change server
     *
     * @param index: server index
     */
    @Override
    public void clickedItem(int index) {
        closeDrawer();
        changeServer.newServer(serverLists.get(index));
    }

    private void filter(String text) {
        filterList = new ArrayList<>();

        for (Server item : serverLists) {
            if (item.getCountry().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }
        }

        if(filterList.size() == 0)
        {
            Toast.makeText(getApplicationContext(), "No Country Found", Toast.LENGTH_LONG).show();
        }
        serverListRVAdapter.filterList(filterList);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}
