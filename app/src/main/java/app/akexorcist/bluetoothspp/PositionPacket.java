package app.akexorcist.bluetoothspp;

import java.io.Serializable;

/**
 * Created by amitshah on 2016-12-08.
 */

public class PositionPacket implements Serializable{
    String _rawMessage;
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
        String[] data = message.split(",");
        //TODO unpack string buffer


        /*

        @p, EC79, 151, 0, 359739070114723,
        Prefix,CRC, Length, SeqId, UnitId, [POSITION DATA]

        1481211972, gpsTimesamp
        1481231214, rtcTimestamp
        1481231214, positionSendDateTime
        -80550329,longitude
        43358366,latitude
        0,heading
        2,reportID
        55677, odometer
        990, gpsHDOP
        1, inputStatus (8 bit status, input 1 = ignition)
        35, vehicleSpeed
        0,outputStatus
        0,analogInput
        ,driverID
        2000,temperatureSensor1
        2000,temperatureSensor2
        ,textMessage
        0,realTimeReport (0 - realtime, 1 log) DT
        1296,rpmMax
        1,vehicleIdleEventStatus (1 - true, 0 - false)
        0,speedingEventStatus
        35,speedMax
        0,mainPowerLoseEventStatus
        0,throttlePositionMax (%)
        195291,fuelUser (0.1 litre)
        0,fuelLevel(%)
        0,engineCoolantTemperature (C)
        080C00800040,gForceHarshEventMax
        ,gSensorData
        ,harshAccelerationEventStatus
        ,harshBrakingEventStatus
        ,impactEventStatus
         */
    }

    public boolean performCRC(){
        //TODO
        return true;
    }
}
