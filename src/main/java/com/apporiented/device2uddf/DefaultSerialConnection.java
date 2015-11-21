package com.apporiented.device2uddf;

import com.apporiented.device2uddf.utils.Utils;
import com.google.common.primitives.Bytes;
import jssc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by lars on 20.11.15.
 */
public class DefaultSerialConnection implements SerialConnection, SerialPortEventListener {

    private static Logger log = LoggerFactory.getLogger(DefaultSerialConnection.class);

    private SerialPort serialPort;

    private static final int TIMEOUT = 10000;

    private Integer expectedResponseLength;
    private Boolean checkCRC;
    private Boolean trimEcho;

    private int baudRate = 2400;
    private int dataBits = SerialPort.DATABITS_8;
    private int stopBits = SerialPort.STOPBITS_1;
    private int parity =  SerialPort.PARITY_ODD;

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public DefaultSerialConnection(String portName) {
        this.serialPort = new SerialPort(portName);
    }


    public String[] findPortNames() {
        String[] portNames = SerialPortList.getPortNames();
        for (String portName : portNames) {
            log.info("Found port {}", portName);
        }
        return portNames;
    }

    @Override
    public void open() {
        try {
            if (!serialPort.isOpened()) {
                log.debug("Opening serial port port {}", serialPort.getPortName());
                serialPort.openPort();
                serialPort.setParams(baudRate, dataBits, stopBits, parity, false, false);
                //int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask

                int mask = SerialPort.MASK_RXCHAR
                        | SerialPort.MASK_RXFLAG
                        | SerialPort.MASK_TXEMPTY
                        | SerialPort.MASK_CTS
                        | SerialPort.MASK_DSR
                        | SerialPort.MASK_RLSD
                        | SerialPort.MASK_BREAK
                        | SerialPort.MASK_ERR
                        | SerialPort.MASK_RING;

                serialPort.setEventsMask(mask);//Set mask
                serialPort.addEventListener(this);
            }
        } catch (Exception e) {
            throw new CommunicationException("Cannot open port " + serialPort.getPortName());
        }

    }

    @Override
    public void close() {
        if (serialPort.isOpened()) {
            try {
                log.debug("Closing serial port port {}", serialPort.getPortName());
                serialPort.removeEventListener();
                serialPort.closePort();
            } catch (Exception e) {
                throw new CommunicationException("Cannot close port " + serialPort.getPortName());
            }
        }
    }

    /*


    public void readsync() throws Exception {
		serialPort = new SerialPort("/dev/cu.usbserial-00002014");
		try {
			serialPort.openPort();
			try {
				serialPort.setParams(2400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_ODD);
				serialPort.setDTR(true);
				serialPort.setRTS(false);


    int[] res = readSuuntoMemory(0x24, 1);
    if(res[0]==MODEL_MOSQUITO) {
        System.out.println("Model: Mosquito");
    }
    else if(res[0]==MODEL_D3) {
        System.out.println("Model: D3");
    }
    else {
        System.out.println("Unknown model: " + res[0]);
    }

    res = readSuuntoMemory(0x26, 4);
    NumberFormat nf = new DecimalFormat("00");
    System.out.println("Serial number: " + nf.format(res[0]*256+res[1])+nf.format(res[2])+nf.format(res[3]));

    res = readSuuntoMemory(0x2c, 30);
    byte[] b = new byte[res.length];
    for(int i = 0; i<res.length; i++)
    b[i] = (byte) res[i];
    String s = new String(b);
    System.out.println("Personal info: " + s);


    res = readSuuntoMemory(0x5c, 4);
    System.out.println("Max depth (freediving) [m]:  " + feetToMeters(res[0]*256+res[1]));
    System.out.println("Max time (freediving) [s]:  " + res[2]*256+res[3]);

    res = readSuuntoMemory(0x65, 5);
    System.out.println("Time alarm: " + res[1]*256+res[2]);
    System.out.println("Depth alarm [m]:  " + feetToMeters(res[3]*256+res[4]));

    System.out.println("*********");

} finally {

        serialPort.closePort();




        }






        } catch (final SerialPortException ex) {
        System.out.println(ex);
        }
        }

private static int[] readSuuntoMemory(int addr, int len) throws Exception {
        byte[] cmd = new byte[5];
        int[] res = new int[len];
        int b;

        cmd[0] = 0x05;
        cmd[1] = (byte) (addr/256);
        cmd[2] = (byte) (addr%256);
        cmd[3] = (byte) len;
        cmd[4] = (byte) (cmd[0]^cmd[1]^cmd[2]^cmd[3]);
        sleep(1500);
        serialPort.setRTS(true);
        sleep(1500);
        serialPort.writeBytes(cmd);
        sleep(1500);
        serialPort.setRTS(false);
        sleep(1500);

        int[] buf = new int[4];
        for(int i = 0; i<buf.length; i++) {
        buf[i] = read(1000);
        if(buf[i]!=cmd[i])
        throw new RuntimeException("Reading memory: got "+buf[i]+" while waiting for "+cmd[i]);
        }

        byte crc = cmd[4];
        for(int i = 0; i<len; i++) {
        b = read(1000);
        if(b==-1) throw new RuntimeException("Timeout reading memory");
        res[i] = b;
        crc ^= b;
        }
        crc ^= read(1000);
        if(crc!=0) throw new RuntimeException("CRC error reading memory");
        return res;
        }

private static int read(int timeout) throws Exception {
        int timeelapsed = 0;
        if (serialPort.getInputBufferBytesCount()>0) {
        return serialPort.readBytes(1)[0];
        }
        while (timeelapsed<timeout) {
        sleep(100);
        timeelapsed += 100;
        if (serialPort.getInputBufferBytesCount()>0) {
        return serialPort.readBytes(1)[0];
        }
        }
        return -1;
        }


     */


    private void runAsync(final Block block) {
        Runnable r = () -> {
            try {
                block.execute();
            } catch (Exception e) {
                log.error("Could not execute command ", e);
            }
        };
        new Thread(r).start();
    }


    private Object lck = new Object();
    public void waitForResponse(Integer expectedResponseLength, Boolean checkCRC) {
        synchronized (lck) {
            try {
                this.expectedResponseLength = expectedResponseLength;
                this.checkCRC = checkCRC;
                lck.wait(TIMEOUT);
            } catch (InterruptedException e) {

            }
        }
    }

    public void waitForResponse() {
        waitForResponse(null, null);
    }

    public void signalResponse() {
        synchronized (lck) {
            lck.notifyAll();
        }
    }



    @Override
    public byte[] sendAndReceive(byte[] cmd, int expectedLength, boolean trimEcho,  boolean checkCRC) {
        this.expectedResponseLength = null;
        this.checkCRC = null;
        this.trimEcho = trimEcho;
        this.lastError = null;
        this.baos = null;
        this.lastCmd = cmd;

        if (!serialPort.isOpened()) {
            throw new CommunicationException("Port " + serialPort.getPortName() + " not open.");
        }

        try {
            Utils.logBytes("Command", cmd);

            runAsync(() -> serialPort.setRTS(true));
            log.debug("Waiting for cts");
            waitForResponse();

            runAsync(()->{
                try {
                    serialPort.writeBytes(cmd);
                } catch (SerialPortException e) {
                    log.error("Could not write command.", e);
                }
            });
            runAsync(() -> serialPort.setRTS(false));
            waitForResponse(expectedLength, checkCRC);

            if (lastError != null) {
                throw new CommunicationException(lastError);
            }

            byte[] result = baos.toByteArray();
            Utils.logBytes("Response", result);
            return result;
        } catch (Exception e) {
            throw new CommunicationException("Cannot send data to port " + serialPort.getPortName(), e);
        } finally {

        }
    }



    private ByteArrayOutputStream baos;
    private String lastError;
    private byte[] lastCmd;

    @Override
    public void serialEvent(SerialPortEvent event) {
        if(event.isRXCHAR()){
            if(event.getEventValue() > 0){
                try {
                    byte[] data  = serialPort.readBytes();
                    log.info("Got {} bytes: {}", event.getEventValue(), Utils.toString(data));
                    baos.write(data);

                    if (trimEcho) {
                        if (baos.size() >= lastCmd.length) {
                            int idx = Bytes.indexOf(baos.toByteArray(), lastCmd);
                            if (idx >= 0) {
                                idx += lastCmd.length;
                                byte[] d = baos.toByteArray();
                                baos = new ByteArrayOutputStream();
                                if (idx < d.length-1) {
                                    baos.write(Arrays.copyOfRange(d, idx, baos.size()));
                                }
                            }
                        }
                    }

                    if (expectedResponseLength != null) {
                        if (checkCRC) {
                            if (baos.size() == expectedResponseLength+1) {
                                byte[] completeData = baos.toByteArray();
                                byte targetCRC = completeData[completeData.length-1];
                                byte actualCRC = 0;
                                for (int i = 0; i < completeData.length-1; i++) {
                                    actualCRC ^= completeData[i];
                                }
                                if (actualCRC != targetCRC) {
                                    lastError = "CRC mismatch";
                                }
                                signalResponse();
                            }
                            else if (baos.size() > expectedResponseLength+1) {
                                lastError = "Unexpected response size: " + baos.size() + " (" + expectedResponseLength + ")";
                            }
                        }
                        else {
                            if (baos.size() == expectedResponseLength) {
                                signalResponse();
                            }
                            else if (baos.size() > expectedResponseLength) {
                                lastError = "Unexpected response size: " + baos.size() + " (" + expectedResponseLength + ")";
                            }
                        }
                    }
                }
                catch (IOException ex) {
                    lastError = "Could write response to buffer";

                }
                catch (SerialPortException ex) {
                    lastError = "Could not read response.";
                }
            }
        }
        else if(event.isCTS()){
            if(event.getEventValue() == 1){
                log.info("CTS - ON");
                baos = new ByteArrayOutputStream();
                lastError = null;
                signalResponse();
            }
            else {
                log.info("CTS - OFF");
            }

        }
        else if(event.isDSR()){///If DSR line has changed state
            if(event.getEventValue() == 1){//If line is ON
                log.info("DSR - ON");
            }
            else {
                log.info("DSR - OFF");
            }
        }
    }




    public interface Block {
        void execute() throws Exception;
    }
}
