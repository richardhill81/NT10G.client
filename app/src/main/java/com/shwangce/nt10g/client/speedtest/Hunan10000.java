package com.shwangce.nt10g.client.speedtest;

public class Hunan10000 {
    public enum HunanTestStep {
        DOWNLOAD,
        UPLOAD
    }

    public static final String[] DOWNLOAD_URLS_test = new String[] {
        "http://222.243.192.22:8088/netspeet/data/speet.dat",
                "http://222.243.109.207:8088/netspeet/data/speet.dat",
                "http://218.76.78.113:8088/netspeet/data/speet.dat",
                "http://218.75.128.45:8088/netspeet/data/speet.dat"
    };

    public static final String[] DOWNLOAD_URLS_1000M = new String[] {
            "http://218.76.39.234:8088/netspeet/data/speet.dat",
            "http://222.243.109.205:8088/netspeet/data/speet.dat",
            "http://218.76.39.226:8088/netspeet/data/speet.dat",
            "http://61.187.187.114:80/netspeet/data/speet.dat"
    };

    public static final String[] DOWNLOAD_URLS = new String[] {
            "http://222.243.154.22:80/netspeet/data/speet.dat",
            "http://124.228.8.146:8088/netspeet/data/speet.dat",
            "http://220.168.141.110:8088/netspeet/data/speet.dat",
            "http://220.168.141.114:8088/netspeet/data/speet.dat"
    };

    public static final String[] UPLOAD_URLS_test = new String[] {
            "http://220.168.141.114:21356",
            "http://222.243.109.203:21356",
            "http://218.76.78.112:21356",
            "http://118.254.0.20:21356"
    };

    public static final String[] UPLOAD_URLS = new String[] {
            "http://61.187.187.114:21356",
            "http://222.243.154.26:21356",
            "http://222.243.154.22:21356",
            "http://218.76.39.234:21356"
    };

    public static String getDownloadsString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<DOWNLOAD_URLS.length;i++) {
            sb.append(DOWNLOAD_URLS[i]);
            if(i != DOWNLOAD_URLS.length -1)
                sb.append("|");
        }
        return sb.toString();
    }

    public static String getDownloadUrlsInfo() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<DOWNLOAD_URLS.length;i++) {
            sb.append(DOWNLOAD_URLS[i]);
            if(i != DOWNLOAD_URLS.length -1)
                sb.append("\n");
        }
        return sb.toString();
    }

    public static String getUploadsString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<UPLOAD_URLS.length;i++) {
            sb.append(UPLOAD_URLS[i]);
            if(i != UPLOAD_URLS.length -1)
                sb.append("|");
        }
        return sb.toString();
    }

    public static String getUploadUrlsInfo() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<UPLOAD_URLS.length;i++) {
            sb.append(UPLOAD_URLS[i]);
            if(i != UPLOAD_URLS.length -1)
                sb.append("\n");
        }
        return sb.toString();
    }
}
