package com.hottechnologies.onewood.view;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hottechnologies.onewood.databinding.FragmentMainBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hottechnologies.onewood.R;
import com.hottechnologies.onewood.CheckInternetConnection;
import com.hottechnologies.onewood.SharedPreference;
import com.hottechnologies.onewood.interfaces.ChangeServer;
import com.hottechnologies.onewood.model.Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;
import de.blinkt.openvpn.core.VpnStatus;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment implements View.OnClickListener, ChangeServer {

    private Server server;
    private CheckInternetConnection connection;

    private OpenVPNThread vpnThread = new OpenVPNThread();
    private OpenVPNService vpnService = new OpenVPNService();
    public static boolean vpnStart = false;
    public static boolean vpnConnecting = false;
    private SharedPreference preference;
    private DrawerLayout drawer;

    private FragmentMainBinding binding;
    RelativeLayout open;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        View view = binding.getRoot();
        initializeAll();
        return view;
    }

    /**
     * Initialize all variable and object
     */

    private void initializeAll() {
        preference = new SharedPreference(getContext());
        server = preference.getServer();

        // Update current selected server icon
        updateCurrentServerIcon(server.getFlagUrl());
        server.getCountry();
        connection = new CheckInternetConnection();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        open = getView().findViewById(R.id.changeServer);
        Toolbar toolbar = getView().findViewById(R.id.toolbar);
        drawer = getView().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
        binding.start.setOnClickListener(this);

        open.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                MainActivity._instance.closeDrawer();
            }
        });

        // Checking is vpn already running or not
        isServiceRunning();
        VpnStatus.initLogCache(getActivity().getCacheDir());
    }

    /**
     * @param v: click listener view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                // Vpn is running, user would like to disconnect current connection.
                if (vpnStart) {
                    confirmDisconnect();
                    binding.animationView.loop(false);
                }else {
                    prepareVpn();
                }
        }
    }

    /**
     * Show show disconnect confirm dialog
     */
    public void confirmDisconnect(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getString(R.string.connection_close_confirm));

        builder.setPositiveButton(getActivity().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                stopVpn();
            }
        });
        builder.setNegativeButton(getActivity().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // User cancelled the dialog
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Prepare for vpn connect with required permission
     */
    private void prepareVpn() {
        if (!vpnStart && !vpnConnecting) {
            if (getInternetStatus()) {
                // Checking permission for network monitor
                Intent intent = VpnService.prepare(getContext());
                if (intent != null) {
                    startActivityForResult(intent, 1);
                } else startVpn();//have already permission
                // Update confection status
                vpnConnecting = true;
                status("connecting");
                binding.animationView.playAnimation();
            } else {
                // No internet connection available
                showToast("you have no internet connection !!");
            }
        } else {
            if (stopVpn()) {
                // VPN is stopped, show a Toast message.
                showToast("Disconnect Successfully");
            }
        }
    }

    /**
     * Stop vpn
     * @return boolean: VPN status
     */
    public boolean stopVpn() {
        try {
            vpnThread.stop();
            status("connect");
            vpnStart = false;
            vpnConnecting = false;
            binding.animationView.loop(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Taking permission for network access
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //Permission granted, start the VPN
            startVpn();
        } else {
            showToast("Permission Deny !! ");
            binding.animationView.loop(false);
        }
    }

    /**
     * Internet connection status.
     */
    public boolean getInternetStatus() {
        return connection.netCheck(getContext());
    }

    /**
     * Get service status
     * @return
     */
    public boolean isServiceRunning() {
        setStatus(vpnService.getStatus());
        return false;
    }

    /**
     * Start the VPN
     */
    private void startVpn() {
        try {
            // .ovpn file
            InputStream conf = getActivity().getAssets().open(server.getOvpn());
            InputStreamReader isr = new InputStreamReader(conf);
            BufferedReader br = new BufferedReader(isr);
            String config = "";
            String line;

            while (true) {
                line = br.readLine();
                if (line == null) break;
                config += line + "\n";
            }

            br.readLine();
            OpenVpnApi.startVpn(getContext(), config, server.getCountry(), server.getOvpnUserName(), server.getOvpnUserPassword());

            // Update log
            binding.connected.setText("Connecting...");
//            binding.animationView.playAnimation();
        } catch (IOException | RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Status change with corresponding vpn connection status
     * @param connectionState
     */
    public void setStatus(String connectionState) {
        if (connectionState!= null)
        switch (connectionState) {
            case "DISCONNECTED":
                status("connect");
                vpnStart = false;
                vpnService.setDefaultStatus();
                binding.logTv.setText("Not Connected");
                binding.start.setBackgroundResource(R.drawable.stop);
                break;
            case "CONNECTED":
                vpnStart = true;// it will use after restart this activity
                status("connected");
                vpnConnecting = false;
                binding.logTv.setText("Safely Connected");
//                binding.animationView.playAnimation();
                binding.start.setBackgroundResource(R.drawable.connection);
                binding.animationView.loop(false);
//                rotateAnimation();
                break;
            case "WAIT":
                binding.logTv.setText("waiting for server connection!!");
                binding.animationView.playAnimation();
                break;
            case "AUTH":
                binding.logTv.setText("server authenticating!!");
                binding.animationView.playAnimation();
                break;
            case "RECONNECTING":
                status("connecting");
                binding.logTv.setText("Reconnecting...");
                binding.animationView.playAnimation();
                break;
            case "NONETWORK":
                binding.logTv.setText("No network connection");
                break;
        }

    }

    /**
     * Change button background color and text
     * @param status: VPN current status
     */
    public void status(String status) {

        if (status.equals("connect")) {
            binding.connected.setText(getContext().getString(R.string.connect));
        } else if (status.equals("connecting.......")) {
            confirmDisconnect();
            binding.connected.setText(getContext().getString(R.string.connecting));
            binding.animationView.playAnimation();
        } else if (status.equals("connected")) {
            binding.connected.setText(getContext().getString(R.string.disconnect));
            binding.start.setBackgroundResource(R.drawable.connection);
//            binding.animationView.loop(false);
//            binding.animationView.playAnimation();
        } else if (status.equals("tryDifferentServer")) {
            binding.connected.setBackgroundResource(R.drawable.button_connected);
            binding.connected.setText("Try Different\nServer");
        } else if (status.equals("loading")) {
            binding.connected.setBackgroundResource(R.drawable.button);
            binding.connected.setText("Loading Server..");
        } else if (status.equals("invalidDevice")) {
            binding.connected.setBackgroundResource(R.drawable.button_connected);
            binding.connected.setText("Invalid Device");
        } else if (status.equals("authenticationCheck")) {
            binding.connected.setBackgroundResource(R.drawable.button_connecting);
            binding.connected.setText("Authentication \n Checking...");
        }
    }

    /**
     * Receive broadcast message
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                setStatus(intent.getStringExtra("state"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                String duration = intent.getStringExtra("duration");
                String lastPacketReceive = intent.getStringExtra("lastPacketReceive");
                String byteIn = intent.getStringExtra("byteIn");
                String byteOut = intent.getStringExtra("byteOut");

//                 Data send Second Activity...
//                Intent intent1 = new Intent(context,SpeedTestActivity.class);
//                intent1.putExtra("byteIn", byteIn);
//                startActivity(intent);


                if (duration == null) duration = "00:00:00";
                if (lastPacketReceive == null) lastPacketReceive = "0";
                if (byteIn == null) byteIn = " 00.00";
                if (byteOut == null) byteOut = " 00.00";
                updateConnectionStatus(duration, lastPacketReceive, byteIn, byteOut);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * Update status UI
     * @param duration: running time
     * @param lastPacketReceive: last packet receive time
     * @param byteIn: incoming data
     * @param byteOut: outgoing data
     */
    public void updateConnectionStatus(String duration, String lastPacketReceive, String byteIn, String byteOut) {
        binding.durationTv.setText("" + duration);
//        binding.lastPacketReceiveTv.setText("Packet Received: " + lastPacketReceive + " second ago");
        binding.byteInTv.setText("" + byteIn);
        binding.byteOutTv.setText("" + byteOut);
    }

    /**
     * Show toast message
     * @param message: toast message
     */
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * VPN server country icon change
     * @param serverIcon: icon URL
     */
    public void updateCurrentServerIcon(String serverIcon) {
        Glide.with(getContext())
                .load(serverIcon)
                .into(binding.selectedServerIcon);
       binding.countryname.setText(server.getCountry());
//       Glide.with(getContext()).load(server).into(binding.signal);

    }

    /**
     * Change server when user select new server
     * @param server ovpn server details
     */
    @Override
    public void newServer(Server server) {
        this.server = server;
        updateCurrentServerIcon(server.getFlagUrl());
        server.getCountry();
        // Stop previous connection
        if (vpnStart) {
            stopVpn();
        }
        prepareVpn();
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
        if (server == null) {
            server = preference.getServer();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    /**
     * Save current selected server on local shared preference
     */
    @Override
    public void onStop() {
        if (server != null) {
            preference.saveServer(server);
        }
        super.onStop();
    }
}
