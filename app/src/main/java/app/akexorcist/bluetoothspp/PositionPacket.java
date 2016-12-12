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
}
