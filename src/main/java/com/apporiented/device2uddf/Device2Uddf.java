package com.apporiented.device2uddf;

import com.apporiented.device2uddf.devices.SuuntoMosquito;
import com.apporiented.device2uddf.model.GlobalData;
import jssc.SerialPortList;

/**
 * Created by lars on 20.11.15.
 */
public class Device2Uddf {


    public static void main(String[] args) {
        String portName;
        if (args.length == 0) {
            String[] portNames = SerialPortList.getPortNames();
            System.out.println("Found serial ports:");
            for (String pn : portNames) {

                System.out.println("  " + pn);
            }
            return;
        }
        else {
            portName = args[0];
        }



        SerialConnection conn = new DefaultSerialConnection(portName);
        try {

            conn.open();

            SuuntoMosquito device = new SuuntoMosquito();
            device.init(conn);
            GlobalData globalData = device.retrieveGlobalData();



            System.out.println(globalData);


        } finally {
            conn.close();

        }



    }

}
