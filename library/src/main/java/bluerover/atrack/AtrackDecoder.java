package bluerover.atrack;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;

/**
 * Created by amitshah on 2016-11-25.
 * A factory class that generates ATrack Data Position Packets
 */


public class AtrackDecoder extends BluetoothSPP {
    public AtrackDecoder(Context context){
        super(context);

    }

    public void startAtrackDecoder(){
        if(!this.isBluetoothAvailable()) {
            //TODO return BLUETOOTH_UNAVAILABLE
            //THROW BLUETOOTH_UNAVAILABLE
        }

        this.setOnDataReceivedListener(new OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
               //TODO decode data here and write to eventbus
            }
        });

        this.setBluetoothConnectionListener(new BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Log.d("ATRACK","Connected to " + name + "\n" + address);
            }

            public void onDeviceDisconnected() {
                Log.d("ATRACK","Connection lost");
            }

            public void onDeviceConnectionFailed() {
                Log.d("ATRACK","Unable to connect");
            }
        });


    }
    /*
    the activity that is passed in should handle the activity result as such:
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
			if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
		} else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                atrackDecoder.setupService();
                atrackDecoder.startService(false);
                setup();
            } else {
                //TODO bluetooth not enabled
            }
        }
    }

     */
    public void startScanForResult(ContextWrapper context, Activity activity){
        if(this.getServiceState() == BluetoothState.STATE_CONNECTED) {
            this.disconnect();
        }
        Intent intent = new Intent(context, DeviceList.class);
        activity.startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    public PositionData decode(String line){
        String[] params = line.split(",");
        return new PositionData();
    }


    public class PositionData{
        String deviceId;
        long crc;
        long timestamp;
        int reportId;
        boolean ignitionStatus;
        float latitude;
        float longitude;
        long odometer;
        double speedKPH;
        double rpm;
        double fuelLevel;
        double fuelEconomy;
        boolean isSpeeding;
        boolean isIdling;
        boolean isHardbraking;
        boolean isHardAcceleration;

        public boolean isValidCrc(){
            return false;
        }
        public PositionData(){

        }
    }
}
