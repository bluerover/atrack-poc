package app.akexorcist.bluetoothspp;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by amitshah on 2016-12-08.
 */

public class PositionPacket implements Serializable{
    /* index holds the parse order of packets coming in via the form field output.  the naming is 1:1 with current
    object properties.  If we decide to add new fields, update this index list based on where it appears in the ATrack
    form packet and add the property.  It will be automagically decoded */
    static String[] index= {
        "prefix","crc", "length","seqID", "unitID", "gpsTimesamp",
                "rtcTimestamp",
                "positionSendDateTime", "longitude", "latitude",
                "heading",
                "reportID",
                "odometer",
                "gpsHDOP",
                "inputStatus",
                "vehicleSpeed",
                "outputStatus",
                "analogInput",
                "driverID",
                "temperatureSensor1",
                "temperatureSensor2","textMessage",
                "realTimeReport",
                "rpmMax",
                "vehicleIdleEventStatus",
                "speedingEventStatus",
                "speedMax",
                "mainPowerLoseEventStatus",
                "throttlePositionMax",
                "fuelUser",
                "fuelLevel",
                "engineCoolantTemperature",
                "gForceHarshEventMax",
                "gSensorData",
                "harshAccelerationEventStatus",
                "harshBrakingEventStatus",
                "impactEventStatus"
    };

    String _rawMessage;
    String[] data;
    String prefix;
    int crc;
    int length;
    long seqID;
    String unitID;
    long gpsTimesamp;
    long rtcTimestamp;
    long  positionSendDateTime;
    long longitude;
    long latitude;
    double heading;
    int reportID;
    long odometer;
    int gpsHDOP;
    byte inputStatus;
    double vehicleSpeed;
    int outputStatus;
    int analogInput;
    String driverID;
    double temperatureSensor1;
    double temperatureSensor2;
    String textMessage;
    int realTimeReport;// (0 - realtime, 1 log) DT
    double rpmMax;
    boolean vehicleIdleEventStatus ;//(1 - true, 0 - false)
    boolean speedingEventStatus ; // (1- true, 0 -false);
    double speedMax;
    int mainPowerLoseEventStatus; // (1- off, 0 -on)
    double throttlePositionMax ;//(%)
    double fuelUser;// (0.1 litre)
    double fuelLevel;//(%)
    double engineCoolantTemperature;// (C)
    String gForceHarshEventMax;// parse from 080C00800040
    String gSensorData;
    int harshAccelerationEventStatus;
    int harshBrakingEventStatus;
    int impactEventStatus;

    public PositionPacket(String message){
        _rawMessage = message;
        data = message.split(",");
        //TODO unpack string buffer
        Class<PositionPacket> classDefintion = PositionPacket.class;
        //TODO uncomment this when we are ready for live data;

//        for(int i =0; i < index.length; i++){
//            try{
//                parseDataIndex(classDefintion.getField(index[i]), i);
//            }catch (Exception e){
//
//            }
//        }

    }

    void parseDataIndex(Field property, int index){
        try {
            Type t = property.getType();
            if(property.getType().isAssignableFrom(Integer.TYPE)){
                property.setInt(this, Integer.parseInt(data[index]));
            }
            else if(property.getType().isAssignableFrom(Long.TYPE)){
                property.setLong(this, Long.parseLong(data[index]));
            }
            else if(property.getType().isAssignableFrom(Double.TYPE)){
                property.setDouble(this, Double.parseDouble(data[index]));
            }
            else if(property.getType().isAssignableFrom(String.class)){
                property.set(this, data[index]);
            }
            else{
                throw new Exception("unknnown property unpacking");
            }

        }catch(Exception e){

        }

    }

    public boolean performCRC(){
        //TODO
        return true;
    }
}
