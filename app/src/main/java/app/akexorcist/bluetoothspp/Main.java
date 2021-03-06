package app.akexorcist.bluetoothspp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Main extends Activity implements OnClickListener {
    BroadcastReceiver myBroadCastReceviver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras().getBundle("data");
            PositionPacket pp = (PositionPacket)bundle.get(PositionPacket.class.toString());
            Log.d("Main Activity", pp._rawMessage);
        }
    };

    EventDispatcher ed = new EventDispatcher(Main.this) {
        @Override
        public void handlePacket(PositionPacket pp) {
            Log.d(Main.class.toString(), pp._rawMessage);
        }
    };
    protected void onStart(){
        //lets start the process in the backgrounda
        super.onStart();
        ed.subscribe();

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        ed.unsubscribe();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadCastReceviver);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnSimple = (Button) findViewById(R.id.btnSimple);
        btnSimple.setOnClickListener(this);

        Button btnListener = (Button) findViewById(R.id.btnListener);
        btnListener.setOnClickListener(this);

        Button btnAutoConnect = (Button) findViewById(R.id.btnAutoConnect);
        btnAutoConnect.setOnClickListener(this);

        Button btnDeviceList = (Button) findViewById(R.id.btnDeviceList);
        btnDeviceList.setOnClickListener(this);

        Button btnTerminal = (Button) findViewById(R.id.btnTerminal);
        btnTerminal.setOnClickListener(this);
//        Thread t = new Thread(){
//            public void run(){
//                Intent autoConnect= new Intent(getApplicationContext(),AutoConnectService.class );
//                startService(autoConnect);
//            }
//        };
//        t.start();
//        Intent autoConnect= new Intent(getApplicationContext(),AutoConnectService.class );
//        startService(autoConnect);

//        Intent broker = new Intent(getApplicationContext(), MqttBrokerService.class);
//        startService(broker);
        Intent autoConnect= new Intent(getApplicationContext(),AutoConnectService.class );
        startService(autoConnect);

//        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadCastReceviver
//                ,new IntentFilter(EventDispatcher.Filter.ON_DATA));

//        ServiceConnection autoConnectConnection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName componentName) {
//
//            }
//        };
//        bindService(autoConnect,autoConnectConnection, Context.BIND_AUTO_CREATE);
    }


    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id) {
            case R.id.btnSimple:
                intent = new Intent(getApplicationContext(), SimpleActivity.class);
                startActivity(intent);
                break;
            case R.id.btnListener:
                intent = new Intent(getApplicationContext(), ListenerActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAutoConnect:
                intent = new Intent(getApplicationContext(), AutoConnectActivity.class);
                startActivity(intent);
                break;
            case R.id.btnDeviceList:
                intent = new Intent(getApplicationContext(), DeviceListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTerminal:
                intent = new Intent(getApplicationContext(), TerminalActivity.class);
                startActivity(intent);
                break;
        }
    }
}
