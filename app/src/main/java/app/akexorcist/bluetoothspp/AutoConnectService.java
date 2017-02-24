/*
 * Copyright 2014 Akexorcist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.akexorcist.bluetoothspp;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class AutoConnectService extends Service {
    BluetoothSPP bt;

    /* The following are the set of AT commands that must be run when a connection is completed
    at this point we dont have
    * auto detect old type
    AT$OBDS=0
    $OK
    //time and distance, 180 secs, 10000 meters, 15 degrees, 0 min speed, send all packets i.e. 0, send data to bluetooth (SET to FFFF for sending to everything including GSM server), 0 schedule, 0 off time, last param left out
    AT$TRAC=131,60,10000,15,0,0,16,0,0 
    $OK
    //set idle switch on
    //: Either engine or ACC (Ignition) Status - 0
    //speed < 5
    //duration > 5 minutes
    //enable = 1
    AT$IDLE=0,5,5,1
    $OK
    //over riming 4500 for 5 secs
    AT$RPME=4500,5,2500,10
    $OK

    user defined report action - send to bluetooth
    AT$RACT=1,1,16
    $OK
    //speed above 100 for 10 seconds, or below 90 for 10 seconds
    AT$SPED=100,10,90,10
    $OK

    //setup harsh braking using G sensor, 250 mg for 0.5 second
    AT$HBKE=2,250,5
    $OK
    //setup harsh acceleration same as above
    AT$HACE=2,250,5
    $OK

    //setupt impact event - 2G debounce (i.e. silence) for 2 seconds
    AT$IMPD=2000,2
    $OK

    ACC on - off,
    —send via RACT1 defined above
    AT$REPT=101,1,”%IN0”,”1”,0,1 //iginition 
    $OK
    AT$REPT=102,1,”%DL”,”1”,0,1 //idle  
    $OK
    AT$REPT=103,1,”%RP”,”1”,0,1 //rpme  
    $OK
    AT$REPT=104,1,”%SD”,”1”,0,1 //sd 
    $OK
    AT$REPT=105,1,”%HA”,”1”,0,1 //Harsh Accel
    $OK
    AT$REPT=106,1,”%HB”,”1”,0,1 //Harsh Braking
    $OK
    AT$REPT=107,1,”%IP”,”1”,0,1 //Impact Sensor
    $OK

    AT$FORM=0,@p,0,”%DT%RP%DL%SD%SM%PS%TR%FC%FL%ET%GV%GN%HA%HB%IP”
    $OK


    *
    * */

    public static String[] configurationQueue = new String[]{
            "AT$PMGR=0,2,2,1,1,1",//disable power manager
            "AT$OBDS=0",
            "AT$TRAC=131,60,1000,15,0,0,16",
            "AT$IDLE=0,5,5",
            "AT$RPME=4500,5,2500,10",
            "AT$RACT=1,3,16", //1,3,16 -> define wehere the report goes
            "AT$SPED=100,10,90,10",
            "AT$HBKE=2,250,5",
            "AT$HACE=2,250,5",
            "AT$IMPD=2000,2",
            "AT$REPT=101,1,\"%IN0\",\"1\",0,1", //iginition 
            "AT$REPT=102,1,\"%DL\",\"1\",0,1",//idle  
            "AT$REPT=103,1,\"%RP\",\"1\",0,1", //rpme  
            "AT$REPT=104,1,\"%SD\",\"1\",0,1", //sd 
            "AT$REPT=105,1,\"%HA\",\"1\",0,1", //Harsh Accel
            "AT$REPT=106,1,\"%HB\",\"1\",0,1", //Harsh Braking
            "AT$REPT=107,1,\"%IP\",\"1\",0,1", //Impact Sensor
            "AT$FORM=0,@p,0,\"%DT%RP%DL%SD%SM%PS%TR%FC%FL%ET%GV%GN%HA%HB%IP\""

    };
    LinkedList<String> configurationStatus = new LinkedList<String>();
//    public class LocalBinder extends Binder {
//        AutoConnectService getService() {
//            return AutoConnectService.this;
//        }
//    }
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mBinder;
//    }
//
//    // This is the object that receives interactions from clients.  See
//    // RemoteService for a more complete example.
//    private final IBinder mBinder = new LocalBinder();



    @Override
    public void onCreate() {
        //

//        runner = new Thread(){
//            @Override
//            public void run(){
//                Looper.prepare();
                bt = new BluetoothSPP(getApplicationContext());
                if(!bt.isBluetoothAvailable()) {
                    Toast.makeText(getApplicationContext()
                            , "Bluetooth is not available"
                            , Toast.LENGTH_SHORT).show();
                    //finish();
                    //return Service.START_STICKY;
                }

                bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
                    public void onDeviceConnected(String name, String address) {
                        Toast.makeText(getApplicationContext()
                                , "AutoConnect says: Connected to " + name
                                , Toast.LENGTH_SHORT).show();

                        //configure device
                        for (String param : AutoConnectService.configurationQueue){
                            bt.send(param,true);
                            Log.d("Configuration Setup", param);
                            Toast.makeText(getApplicationContext()
                                    , "sent config" + param
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }

                    public void onDeviceDisconnected() {
                        Toast.makeText(getApplicationContext()
                                , "Connection lost"
                                , Toast.LENGTH_SHORT).show();
//                        bt.stopAutoConnect();
//                        bt.autoConnect("AX7_BT40");
                    }

                    public void onDeviceConnectionFailed() {
//                        bt.stopAutoConnect();
//                        bt.autoConnect("AX7_BT40");
                        //TELL FleetRover they can

                    }
                });

                bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
                    public void onDataReceived(byte[] data, String message) {
                        //if returned packet is cmd packet, then write to blocking queue
                        //if returned packet is async data broadcast
                        if(message == "$0K"){
                            configurationStatus.add(message);
                        }
                        Log.d("On Data Received", message);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(PositionPacket.class.toString(),(new PositionPacket(message)));
                        //Toast.makeText(AutoConnectService.this, "CONFIGURATION MODE:"+message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EventDispatcher.Filter.ON_DATA);
                        intent.putExtra("data",bundle);
                        LocalBroadcastManager.getInstance(AutoConnectService.this).sendBroadcast(intent);
                    }
                });

                bt.setAutoConnectionListener(new BluetoothSPP.AutoConnectionListener() {
                    public void onNewConnection(String name, String address) {
                        Log.i("Check", "New Connection - " + name + " - " + address);
                    }

                    public void onAutoConnectionStarted() {
                        Log.i("Check", "Auto menu_connection started");
                    }
                });
                onStart();
//            }
//        };
//
//        runner.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
    //protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_autoconnect);

        super.onStartCommand(intent, startId, startId);
        //runner.start();
        return Service.START_STICKY;
    }

    public void scanMode(){
        if(bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
        } else {
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivity(intent);//, BluetoothState.REQUEST_CONNECT_DEVICE);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        //runner.interrupt();
        //bt.stopService();
    }

    public void onStart() {
        //super.onStart();

        //re-initialise configuration queue commands

        if(!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }

    public void setup() {
        //Button btnSend = (Button)findViewById(R.id.btnSend);
//        btnSend.setOnClickListener(new OnClickListener(){
//            public void onClick(View v){
//                bt.send("Text", true);
//            }
//        });

        bt.autoConnect("AX7_BT40");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null

        return null;
    }




}
