package demo.pay.com.smartpat.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.LinearLayout;

import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import demo.pay.com.smartpat.R;
import demo.pay.com.smartpat.activity.MainActivity;
import demo.pay.com.smartpat.activity.TransactionRecordActivity;
import demo.pay.com.smartpat.utility.Utility;


public class MainFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private final String TAG = MainFragment.class.getSimpleName();
    private MainActivity mActivity;
    TextView btnViewAllTransaction;


    private OnFragmentInteractionListener mListener;
    // client's name that's visible to other devices when connecting
    public   String CLIENT_NAME = "MERCHANT:Teacher";
    /**
     * service id. discoverer and advertiser can use this id to
     * verify each other before connecting
     */
    public static final String SERVICE_ID = "demo.pay.com.smartpat";
    public ProgressBar progressBar;


    public static final Strategy STRATEGY = Strategy.P2P_STAR;
    /* The connection strategy we'll use.
         * P2P_STAR denotes there is 1 advertiser and N discoverers
         */


    private static final String[] REQUIRED_PERMISSIONS =
            new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    private Button mSendView = null;
    private Button mReceiveView = null;
    private LinearLayout mMerchantLayout = null; // main layout
    private LinearLayout mMerchantListLayout = null;
    private LinearLayout mClientLayout = null; // main Layout
    private LinearLayout mClientListLayout = null;
    private LayoutInflater mLayoutInflater = null;
    private LinearLayout mPayLayout = null;
    private LinearLayout mReceiveLayout = null;
    private TextView mReceiveAmountLayout = null;

    private boolean isAdvertising = false;
    private boolean isDiscovery = false;

    HashMap<String, String> mNearMerchantDevicesList = new HashMap<>();
    HashMap<String, String> mNearClientDevicesList = new HashMap<>();


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        CLIENT_NAME = Utility.getUserProfile(mActivity)+":"+Utility.getEmail(mActivity);

        mSendView = (Button) view.findViewById(R.id.send);
        mReceiveView = (Button) view.findViewById(R.id.receive);
        mPayLayout = (LinearLayout) view.findViewById(R.id.pay_layout);
        mMerchantLayout = (LinearLayout) view.findViewById(R.id.merchant_layout);
        mMerchantListLayout = (LinearLayout) view.findViewById(R.id.merchant_list_layout);
        mReceiveLayout = (LinearLayout) view.findViewById(R.id.receive_layout);
        mReceiveAmountLayout = (TextView) view.findViewById(R.id.receive_ammount);
        mClientLayout = (LinearLayout) view.findViewById(R.id.client_layout);
        mClientListLayout = (LinearLayout) view.findViewById(R.id.client_list_layout); // receive_ammount


        mSendView = (Button)view.findViewById(R.id.send);
        mReceiveView = (Button)view.findViewById(R.id.receive);
        //mMessageView = (TextView)view.findViewById(R.id.status_msg);
        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        btnViewAllTransaction = view.findViewById(R.id.see_transaction);
        btnViewAllTransaction.setOnClickListener(this);
        FirebaseUser firebaseUser = mActivity.auth.getCurrentUser();
        if(firebaseUser !=null){

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        mSendView.setOnClickListener(this);
        mReceiveView.setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Nearby.getConnectionsClient(mActivity).stopAdvertising();
        Nearby.getConnectionsClient(mActivity).stopDiscovery();
        Nearby.getConnectionsClient(mActivity).stopAllEndpoints();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;

        this.mLayoutInflater = ((MainActivity) context).getLayoutInflater();

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (MainFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void startAdvertising() {
        isAdvertising = true;
        Nearby.getConnectionsClient((Context) mActivity).startAdvertising(
                CLIENT_NAME,
                SERVICE_ID,
                mConnectionLifecycleCallback,
                new AdvertisingOptions(STRATEGY))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mActivity, "Adv onSuccess", Toast.LENGTH_SHORT).show();
                        toogleProgressBar();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mActivity, "Adv onFailure", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
    }

    /*
     *    These callbacks are made when other devices:
     *    1. tries to initiate a connection
     *    2. completes a connection attempt
     *    3. disconnects from the connection
     */
    private final ConnectionLifecycleCallback mConnectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(final String endpointId, ConnectionInfo connectionInfo) {
                    Log.i(TAG, endpointId + " connection initiated");
                    if (isAdvertising) {
                        String endpointName = connectionInfo.getEndpointName();
                        String[] str = endpointName.split(":");
                        if (str.length == 2) {
                            String identifier = str[0];
                            if ("MERCHANT".equalsIgnoreCase(identifier)) {
                                mNearMerchantDevicesList.put(endpointId, str[1]);
                            } else if ("CLIENT".equalsIgnoreCase(identifier)) {
                                mNearMerchantDevicesList.put(endpointId, str[1]);
                            }
                        }
                    } else {
                        mNearClientDevicesList.clear();
                        mNearMerchantDevicesList.clear();
                    }
                    acceptConnection(endpointId);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    Log.i(TAG, endpointId + " connection result");
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            if (isAdvertising) {
                                if (mPayLayout.getVisibility() != View.VISIBLE)
                                    mPayLayout.setVisibility(View.VISIBLE);
                                if (mNearMerchantDevicesList.size() == 0) {
                                    mMerchantLayout.setVisibility(View.GONE);
                                } else if (mNearMerchantDevicesList.containsKey(endpointId)) {
                                    inflateMerchantLayout(endpointId);
                                }

                                if (mNearClientDevicesList.size() == 0) {
                                    mClientLayout.setVisibility(View.GONE);
                                } else if (mNearClientDevicesList.containsKey(endpointId)) {
                                    inflateClientLayout(endpointId);
                                }
                            } else {
                                if (mReceiveLayout.getVisibility() != View.VISIBLE)
                                    mReceiveLayout.setVisibility(View.VISIBLE);
                            }
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            if (mNearMerchantDevicesList.containsKey(endpointId)) {
                                mNearMerchantDevicesList.remove(endpointId);
                            }
                            if (mNearClientDevicesList.containsKey(endpointId)) {
                                mNearClientDevicesList.remove(endpointId);
                            }
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            if (mNearMerchantDevicesList.containsKey(endpointId)) {
                                mNearMerchantDevicesList.remove(endpointId);
                            }
                            if (mNearClientDevicesList.containsKey(endpointId)) {
                                mNearClientDevicesList.remove(endpointId);
                            }
                            break;
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    Log.i(TAG, endpointId + " disconnected");
                    if (isAdvertising) {
                        removeView(endpointId);
                    }
                }
            };

    private void removeView(String endpointId) {
        View view = mMerchantListLayout.getRootView().findViewWithTag(endpointId);
        if (view != null) {
            mMerchantListLayout.removeView(view);
        }

        view = mClientListLayout.getRootView().findViewWithTag(endpointId);
        if (view != null) {
            mClientListLayout.removeView(view);
        }

    }

    private void inflateClientLayout(String endpointId) {
        LinearLayout childView = (LinearLayout) mLayoutInflater.inflate(R.layout.layout, null);
        childView.setTag(endpointId);
        TextView tv = (TextView) childView.findViewById(R.id.device_item);
        tv.setText(mNearClientDevicesList.get(endpointId));
        childView.setOnClickListener(this);
        mClientListLayout.addView(childView);
    }

    private void inflateMerchantLayout(String endpointId) {
        LinearLayout childView = (LinearLayout) mLayoutInflater.inflate(R.layout.layout, null);
        childView.setTag(endpointId);
        TextView tv = (TextView) childView.findViewById(R.id.device_item);
        tv.setText(mNearMerchantDevicesList.get(endpointId));
        childView.setOnClickListener(this);
        mMerchantListLayout.addView(childView);
    }

    private void acceptConnection(String endpointId) {
        Nearby.getConnectionsClient((Context) mActivity).acceptConnection(endpointId, mPayloadCallback);
    }


    PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String s, Payload payload) {
            Toast.makeText(mActivity, s + "", Toast.LENGTH_SHORT).show();

            byte[] data = payload.asBytes();
            String amount = new String(data);
            mReceiveAmountLayout.setText(amount);
        }

        @Override
        public void onPayloadTransferUpdate(String s, PayloadTransferUpdate payloadTransferUpdate) {
            Toast.makeText(mActivity, s +"----> onPayloadTransferUpdate", Toast.LENGTH_SHORT).show();
        }
    };

    private void startDiscovery() {
        isDiscovery = true;
        Nearby.getConnectionsClient(mActivity).startDiscovery(
                SERVICE_ID,
                mEndpointDiscoveryCallback,
                new DiscoveryOptions(STRATEGY))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /**
     * These callbacks are made when :
     * 1. an endpoint that we can connect to is found
     * 2. completes a connection attempt
     */
    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(
                        String endpointId, DiscoveredEndpointInfo dei) {
                    Log.i(TAG, endpointId + "-->" + dei.getEndpointName() + " endpoint Found");
                    requestConnection(endpointId);
                }

                @Override
                public void onEndpointLost(String endpointId) {
                    Log.i(TAG, endpointId + " endpoint lost");
                }
            };

    private void requestConnection(String endpointId) {
        Nearby.getConnectionsClient((Context) mActivity).requestConnection(
                CLIENT_NAME,
                endpointId,
                mConnectionLifecycleCallback
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "Request Success");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Request Failure");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                toogleProgressBar();
                startAdvertising();
                break;
            case R.id.receive:
                startDiscovery();
            case R.id.device_child_layout:
                SendMoneyFragment sendMoneyFragment = SendMoneyFragment.newInstance((String) v.getTag());
                mActivity.pushFragments(SendMoneyFragment.class.getSimpleName(),sendMoneyFragment,R.id.main_layout);
                break;
            case R.id.see_transaction:
                Intent intent = new Intent(mActivity, TransactionRecordActivity.class);
                mActivity.startActivity(intent);
                break;

        }
    }

    public void toogleProgressBar(){
        if(progressBar.getVisibility()==View.VISIBLE){
            progressBar.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void pay(String endpointId, String ammount) {
        byte[] data = ammount.getBytes();
        Nearby.getConnectionsClient(mActivity).sendPayload(endpointId, Payload.fromBytes(data));
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
