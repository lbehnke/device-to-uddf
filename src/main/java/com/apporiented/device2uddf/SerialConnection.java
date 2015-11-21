package com.apporiented.device2uddf;


public interface SerialConnection {

    void open();

    void close();

    byte[] sendAndReceive(byte[] cmd, int expectedResponseLength, boolean echo, boolean checkCRC);
}
