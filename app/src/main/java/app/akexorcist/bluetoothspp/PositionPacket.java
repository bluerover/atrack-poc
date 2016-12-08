package app.akexorcist.bluetoothspp;

import java.io.Serializable;

/**
 * Created by amitshah on 2016-12-08.
 */

public class PositionPacket implements Serializable{
    String _rawMessage;
    public PositionPacket(String message){
        _rawMessage = message;
        String[] data = message.split(",");
        //TODO unpack string buffer
    }
}
