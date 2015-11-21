package com.apporiented.device2uddf.devices;

import com.apporiented.device2uddf.SerialConnection;
import com.apporiented.device2uddf.model.DiveData;
import com.apporiented.device2uddf.model.GlobalData;
import com.apporiented.device2uddf.model.Progress;
import com.apporiented.device2uddf.utils.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by lars on 20.11.15.
 */
public class SuuntoMosquito implements Device {

    private SerialConnection connection;

    @Override
    public void init(SerialConnection connection) {
        this.connection = connection;
    }

    @Override
    public GlobalData retrieveGlobalData() {

        GlobalData data = new GlobalData();
        byte[] res;

        /* Serial number */
        res = readMem(0x26, 4);
        NumberFormat nf = new DecimalFormat("00");
        data.setSerialNumber(nf.format(res[0]*256+res[1])+nf.format(res[2])+nf.format(res[3]));

        /* Read personal info: 0x2C-0x49 */
        res = readMem(0x2c, 30);
        data.setOwnerName(new String(res));

        /* Free diving history: 0x5c-0x5f */
        res = readMem(0x5c, 4);
        data.setMaxFreeDiveDepth(Utils.feetToMeters(res[0] * 256 + res[1]));
        data.setMaxFreeDiveTime(res[2] * 256 + res[3]);

        /* Alarms */
        res = readMem(0x65, 5);
        data.setTimeAlarm(res[1] * 256 + res[2]);
        data.setDepthAlarm(Utils.feetToMeters(res[3] * 256 + res[4]));

        return data;
    }

    private byte[] readMem(int address, int size) {
        byte[] cmd = createReadMemoryCommand(address, size, true);
        byte[] res = connection.sendAndReceive(cmd, 4, true, true);
        byte[] mem = Arrays.copyOfRange(res, cmd.length, res.length-1);
        Utils.logBytes("Memory", mem);
        return mem;
    }


    @Override
    public List<DiveData> retrieveDiveData(Consumer<Progress> progressCallback) {
        return null;
    }

    protected byte[] createReadMemoryCommand(int address, int size, boolean addCRC) {
        byte[] cmd = new byte[addCRC ? 5 : 4];
        cmd[0] = 0x05;
        cmd[1] = (byte) (address/256);
        cmd[2] = (byte) (address%256);
        cmd[3] = (byte) size;
        if (addCRC) {
            setCheckDigit(cmd);
        }
        return cmd;
    }

    protected void setCheckDigit (byte[] data) {
        byte cs = 0;
        for (int i = 0; i < data.length - 1; i++) {
            cs ^= data[i];
        }
        data[data.length-1] = cs;
    }

}
