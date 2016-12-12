package app.akexorcist.bluetoothspp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by amitshah on 2016-12-09.
 */

public abstract class EventDispatcher {
    BroadcastReceiver _broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras().getBundle("data");
            //unpack the position packetp0
            PositionPacket pp = (PositionPacket)bundle.get(PositionPacket.class.toString());
            handlePacket(pp);
            Log.d(EventDispatcher.class.toString(), pp._rawMessage);
        }
    };

    Context _context;

    //pass in application Context
    public EventDispatcher(Context context){
        _context = context;
    }

    public void subscribe(){
        LocalBroadcastManager.getInstance(_context).registerReceiver(_broadcastReceiver
                ,new IntentFilter(Filter.ON_DATA));
    }
    public void unsubscribe(){
        LocalBroadcastManager.getInstance(_context).unregisterReceiver(_broadcastReceiver);
    }

    public abstract void handlePacket(PositionPacket pp);


    public class Filter{
        // Constants that indicate the current connection state
        public static final String ON_DATA= "OnData";       	// we're doing nothing
        public static final String ON_COMMAND= "OnCommand";

    }

}
