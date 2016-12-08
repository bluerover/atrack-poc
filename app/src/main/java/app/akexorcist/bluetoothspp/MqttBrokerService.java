package app.akexorcist.bluetoothspp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.moquette.server.Server;

public class MqttBrokerService extends Service {

    @Override
    public void onCreate() {
        Thread t = new Thread(){
            @Override
            public void run(){
                try {
                    Log.d("MqttBroker", "starting the broker");
                    new Server().startServer();
                }catch (Exception e){
                    Log.e("MqttBroker", "could not start server!Fatal Exception");
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
