package com.shwangce.nt10g.client.wifitest;

public class WifiTestHelper {
    public enum SecurityModeEnum {
        WPA,
        WPA2,
        WPA2_WPA,
        WPA3_WPA2,
        WEP,
        OPN,
        NULL,
        Unknown
    }

    public enum EncryptionTypeEnum {
        PSK,
        PSK2,
        NONE,
        NULL,
        UNSUPPORTED
    }

    public static EncryptionTypeEnum getEncryptionTypeBySecurityMode(SecurityModeEnum mode) {
        switch (mode) {
            case WPA:
            case WPA2_WPA:
                return EncryptionTypeEnum.PSK;

            case WPA2:
            case WPA3_WPA2:
                return EncryptionTypeEnum.PSK2;

            case OPN:
                return EncryptionTypeEnum.NONE;

            case NULL:
                return EncryptionTypeEnum.NULL;

            default:
                return EncryptionTypeEnum.UNSUPPORTED;
        }
    }

    public static String getEncryptionTypeString(EncryptionTypeEnum type) {
        switch (type) {
            case PSK:
                return "psk";
            case PSK2:
                return "psk2";
            case NONE:
                return "none";
            case NULL:
                return "null";
            default:
                return "";
        }
    }
}
