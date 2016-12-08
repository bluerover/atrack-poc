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
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class AutoConnectService extends Service {
    BluetoothSPP bt;
    Thread runner;
    public static String[] configurationQueue = new String[]{
            "AT$OBDS=0",
            //Send updates every 10 seconds
            "AT$TRAC=131,10,10000,15,0,0,16,0,0",
            "AT$IDLE=0,5,5,1",
            "AT$RPME=4500,5,2500,10",

            //user defined report action, send to bluetooth
            "AT$RACT=1,1,16",
            //speed above 100 for 10 seconds, or deactivate when below 90 for 10 seconds
            "AT$SPED=100,10,90,10",
            //send via RACT1 defined above
            "AT$REPT=101,1,\"%IN0\",\"1\",0,1", //iginition 
            "AT$REPT=102,1,\"%DL\",\"1\",0,1", //idle  
            "AT$REPT=103,1,\"%RP\",\"1\",0,1", //rpme  
            "AT$REPT=104,1,\"%SD\",\"1\",0,1",//sd
            "AT$REPT=105,1,\"%HA\",\"1\",0,1",//hard accel
            "AT$REPT=106,1,\"%HB\",\"1\",0,1",//hard braking
            "AT$REPT=107,1,\"%HC\",\"1\",0,1",//hard cornering
            //setup output command format
            //DT - realtime report or log
            //RP - max engine rpm between readings
            //DL - idle state
            //SD - speeding event status
            //SM - max speed between reports
            //PS - Main power lose event status (no main voltage for 2 sec)
            //TR - max throttle position between reports
            //FC - fuel used in 0.1 litre increments
            //FL - fuel level (%)
            //ET - Engine Coolant Temperature event status
            //GV - Maximum G-force value during the harsh events (*NOTE below)
              //*NOTE The G-sensor data are displayed in HEX in both of data format
              //X axis = 1100(HEX) = 4352(DEC), Y axis = B400(HEX) = -19456 (DEC), Z axis = BD00(HEX) = -17152(DEC),
              //Binary 40508B06003900020001463868C076A0570DD8EA570DD8E9570DD8E9073EE3A5017EC651001B6500000000000801 00000000000007D007D0001100B400BD00 (look at the last 6 bytes)
            "AT$FORM=0,@p,0,\"%DT%RP%DL%SD%SM%PS%TR%FC%FL%ET%GV\""
    };

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
//                for (String param : AutoConnectService.configurationQueue){
//                    bt.send(param,true);
//                    Log.d("Configuration Setup", "Sent configuration command:"+param);
//                    Toast.makeText(getApplicationContext()
//                            , "sent config" + param
//                            , Toast.LENGTH_SHORT).show();
//                }
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
                        Log.d("On Data Received", message);
                        //Toast.makeText(AutoConnectService.this, "CONFIGURATION MODE:"+message, Toast.LENGTH_SHORT).show();

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
