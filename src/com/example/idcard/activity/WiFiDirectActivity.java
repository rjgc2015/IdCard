package com.example.idcard.activity;

import com.example.idcard.R;
import com.idcard.broadcast.WiFiDirectBroadcastReceiver;
import com.idcard.fragment.DeviceDetailFragment;
import com.idcard.fragment.DeviceListFragment;
import com.idcard.fragment.DeviceListFragment.DeviceActionListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



/**
 * An activity that uses WiFi Direct APIs to discover and connect with available
 * devices. WiFi Direct APIs are asynchronous and rely on callback mechanism
 * using interfaces to notify the application of operation success or failure.
 * The application should also register a BroadcastReceiver for notification of
 * WiFi state related events.
 */
public class WiFiDirectActivity extends Activity implements ChannelListener, DeviceActionListener {

    public static final String TAG = "wifidirectdemo";
    private WifiP2pManager manager;
    private boolean isWifiP2pEnabled = false;
    private boolean retryChannel = false;
    

    private final IntentFilter intentFilter = new IntentFilter();
    private Channel channel;
    private BroadcastReceiver receiver = null;
    
    private Button mBtnDiscovery;
    private Button mBtnCreate;
    
    private WifiManager mWifiManager;
    private boolean isWifiEnabled=false;

    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i(TAG,"建立布局");
        mWifiManager=(WifiManager) getSystemService(Context.WIFI_SERVICE);
        
        //若原先wifi可用，则设置标志位为true，若不可用，则启用
        if(!mWifiManager.isWifiEnabled())
        	mWifiManager.setWifiEnabled(true);
        else isWifiEnabled=true;
        
        // 增加对应的意图

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        
        mBtnDiscovery=(Button) findViewById(R.id.btn_discovery);
        mBtnDiscovery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 if (!isWifiP2pEnabled) {
	                    Toast.makeText(WiFiDirectActivity.this, R.string.p2p_off_warning,
	                            Toast.LENGTH_SHORT).show();
	                }
	                final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
	                        .findFragmentById(R.id.frag_list);
	                fragment.onInitiateDiscovery();
	                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {

	                    @Override
	                    public void onSuccess() {
	                        Toast.makeText(WiFiDirectActivity.this, "开始搜索",
	                                Toast.LENGTH_SHORT).show();
	                    }

	                    @Override
	                    public void onFailure(int reasonCode) {
	                        Toast.makeText(WiFiDirectActivity.this, "Discovery Failed : " + reasonCode,
	                                Toast.LENGTH_SHORT).show();
	                    }
	                });
				
			}
		});
        mBtnCreate=(Button) findViewById(R.id.btn_group);
        mBtnCreate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			creategroup();
				
			}
		});
       
        
    }

    /** 注册广播，接收对应的intent */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /**
     * 在广播接收到wifi状态变化的时候，重置数据
     */
    public void resetData() {
        DeviceListFragment fragmentList = (DeviceListFragment) getFragmentManager()
                .findFragmentById(R.id.frag_list);
        DeviceDetailFragment fragmentDetails = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        if (fragmentList != null) {
        	//清空列表
            fragmentList.clearPeers();
        }
        if (fragmentDetails != null) {
        	//重置视图
            fragmentDetails.resetViews();
        }
    }

    /**
     * 显示设备详细信息
     */
    @Override
    public void showDetails(WifiP2pDevice device) {
        DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.showDetails(device);

    }

    /**
     * 连接设备
     */
    @Override
    public void connect(WifiP2pConfig config) {
  
    	
        manager.connect(channel, config, new ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(WiFiDirectActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnect() {
        final DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.resetViews();
        manager.removeGroup(channel, new ActionListener() {

            @Override
            public void onFailure(int reasonCode) {
                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);

            }

            @Override
            public void onSuccess() {
                fragment.getView().setVisibility(View.GONE);
            }

        });
    }

    @Override
    public void onChannelDisconnected() {
        // we will try once more
        if (manager != null && !retryChannel) {
            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            resetData();
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);
        } else {
            Toast.makeText(this,
                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void cancelDisconnect() {

        /*
         * A cancel abort request by user. Disconnect i.e. removeGroup if
         * already connected. Else, request WifiP2pManager to abort the ongoing
         * request
         */
        if (manager != null) {
            final DeviceListFragment fragment = (DeviceListFragment) getFragmentManager()
                    .findFragmentById(R.id.frag_list);
            if (fragment.getDevice() == null
                    || fragment.getDevice().status == WifiP2pDevice.CONNECTED) {
                disconnect();
            } else if (fragment.getDevice().status == WifiP2pDevice.AVAILABLE
                    || fragment.getDevice().status == WifiP2pDevice.INVITED) {

                manager.cancelConnect(channel, new ActionListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(WiFiDirectActivity.this, "Aborting connection",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(WiFiDirectActivity.this,
                                "Connect abort request failed. Reason Code: " + reasonCode,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

	@Override
	public void creategroup() {
	  	manager.createGroup(channel, new ActionListener() {
			
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFailure(int reason) {
					// TODO Auto-generated method stub
					Toast.makeText(WiFiDirectActivity.this,
                            "创建失败" + reason,
                            Toast.LENGTH_SHORT).show();
					
				}
			});
		
	}

	@Override
	public void removegroup() {
		manager.removeGroup(channel, new ActionListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(int reason) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}

