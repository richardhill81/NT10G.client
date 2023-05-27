package com.shwangce.nt10g.client.library;


import com.shwangce.nt10g.client.util.ProjectUtil;

/**
 * Created by Administrator on 2016/9/29 0029.
 */

public class WorkUtils {

    private final static char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static String toHexString(byte[] d,int s,int n) {
        final char[] ret = new char[n * 2];
        final int e = s + n;

        int x = 0;
        for (int i = s; i < e; ++i) {
            final byte v = d[i];
            ret[x++] = HEX[0x0F & (v >> 4)];
            ret[x++] = HEX[0x0F & v];
        }
        return new String(ret);
    }

    public enum NetToolsType {
        PING,
        TRACEROUTE,
        IPERF,
        PINGANDTRACEROUTE,
        TIMERSCHEDULE,
        WEBSITETEST,
        DNSTEST,
        NULL
    }

    public enum TestSpeedType {
        DownloadTest,
        UploadTest
    }

    public static boolean isValidMac(String macStr) {

        if (macStr == null || macStr.equals("")) {
            return false;
        }
        String macAddressRule = "([A-Fa-f0-9]{2}[:]){5}[A-Fa-f0-9]{2}";
        if (macStr.matches(macAddressRule)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidDeviceName(String deviceName) {

        if (deviceName == null || deviceName.equals("")) {
            return false;
        }
        String deviceNameRule = ProjectUtil.DeviceName + "_([A-Fa-f0-9]{2}[:]){2}[A-Fa-f0-9]{2}";
        if (deviceName.matches(deviceNameRule)) {
            return true;
        } else {
            return false;
        }
    }
}
