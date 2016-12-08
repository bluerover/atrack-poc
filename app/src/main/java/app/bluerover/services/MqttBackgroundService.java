//package app.bluerover.services;
//
//import android.app.Service;
//import android.content.Intent;
//import android.content.Context;
//
//import org.eclipse.moquette.server.Server;
//
//import static android.R.attr.id;
//
///**
// * An {@link IntentService} subclass for handling asynchronous task requests in
// * a service on a separate handler thread.
// * <p>
// * TODO: Customize class - update intent actions, extra parameters and static
// * helper methods.
// */
//public class MqttBackgroundService extends Service {
//    // TODO: Rename actions, choose action names that describe tasks that this
//    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
//    private static final String BROKER = "app.bluerover.services.action.broker";
//
//    // TODO: Rename parameters
//    private static final String EXTRA_PARAM1 = "app.bluerover.services.extra.PARAM1";
//
//    public MqttBackgroundService() {
//        super("BluetoothBackgroundService");
//    }
//
//    /**
//     * Starts this service to perform action Foo with the given parameters. If
//     * the service is already performing a task this action will be queued.
//     *
//     * @see IntentService
//     */
//    // TODO: Customize helper method
//    public static void startActionBroker(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, MqttBackgroundService.class);
//        intent.setAction(BROKER);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        context.startService(intent);
//    }
//
//    @Override
//    protected void onStartCommand(Intent intent,flags,id) {
//        super.onStartCommand(intent,flags,id);
//        if (intent != null) {
//            final String action = intent.getAction();
//            if (BROKER.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                handleActionBrokerStart(param1);
//            }
//        }
//    }
//
//    /**
//     * Handle action Foo in the provided background thread with the provided
//     * parameters.
//     */
//    private void handleActionBrokerStart(String param1) {
//        try {
//            new Server().startServer();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//}
