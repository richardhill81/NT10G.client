package com.shwangce.nt10g.client.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shwangce.nt10g.client.setaccess.PPPoEInfoBean;
import com.shwangce.nt10g.client.setaccess.StaticInfoBean;
import com.shwangce.nt10g.client.speedtest.HxBoxBean;
import com.shwangce.nt10g.client.speedtest.SpeedTestKind;

/**
 * Created by Administrator on 2016/8/24 0024.
 */

public class DatabaseUtil {

    private final Context context;
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "/mnt/sdcard/Shwangce/nt10g";
    private static final String TABLE_NAME = "nt10g";
    private static final String COLUMNNAME_HXBOX_USERNAME = "username";
    private static final String COLUMNNAME_HXBOX_PASSWORD = "password";
    private static final String COLUMNNAME_HXBOX_USERID = "userid";
    private static final String COLUMNNAME_TESTTYPE = "testtype";
    private static final String COLUMNNAME_STATIC_IP = "static_ip";
    private static final String COLUMNNAME_STATIC_NETMASK = "static_netmask";
    private static final String COLUMNNAME_STATIC_GATEWAY = "static_gateway";
    private static final String COLUMNNAME_STATIC_DNS = "static_dns";
    private static final String COLUMNNAME_PPPOE_ACCOUNT = "pppoe_account";
    private static final String COLUMNNAME_PPPOE_PASSWORD = "pppoe_password";
    private static final String COLUMNNAME_DOWNLOAD_URL = "download_url";
    private static final String COLUMNNAME_UPLOAD_URL = "upload_url";
    private static final String[] Columns = new String[]
                  { COLUMNNAME_HXBOX_USERNAME,COLUMNNAME_HXBOX_PASSWORD,COLUMNNAME_HXBOX_USERID,COLUMNNAME_TESTTYPE,COLUMNNAME_STATIC_IP,COLUMNNAME_STATIC_NETMASK,COLUMNNAME_STATIC_GATEWAY,COLUMNNAME_STATIC_DNS,
                    COLUMNNAME_PPPOE_ACCOUNT,COLUMNNAME_PPPOE_PASSWORD ,
                    COLUMNNAME_DOWNLOAD_URL,COLUMNNAME_UPLOAD_URL };

    private static final String TABLE_CREATE = "create table " + TABLE_NAME  +
            "(" +
            COLUMNNAME_HXBOX_USERNAME + " varchar(20)" + "," +
            COLUMNNAME_HXBOX_PASSWORD + " varchar(20)" + "," +
            COLUMNNAME_HXBOX_USERID + " varchar(20)" + "," +
            COLUMNNAME_TESTTYPE + " varchar(20)" + "," +
            COLUMNNAME_STATIC_IP + " varchar(20)" + "," +
            COLUMNNAME_STATIC_NETMASK + " varchar(20)" + "," +
            COLUMNNAME_STATIC_GATEWAY + " varchar(20)" + "," +
            COLUMNNAME_STATIC_DNS + " varchar(20)" + "," +
            COLUMNNAME_PPPOE_ACCOUNT + " varchar(20)" + "," +
            COLUMNNAME_PPPOE_PASSWORD + " varchar(20)" + "," +
            COLUMNNAME_DOWNLOAD_URL + " varchar(100)" + "," +
            COLUMNNAME_UPLOAD_URL + " varchar(100)" +
            ");";

    public DatabaseUtil(Context context) {
        this.context = context;
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        try {
            database.execSQL(TABLE_CREATE);
        } catch (Exception e) {
            Log.d("DatabaseUtil",e.getMessage());
        }
    }

    public boolean updateTestType(SpeedTestKind testtype, String downloadurl) {
        if(database.isOpen()) {
            Cursor cursor = null;
            boolean r = false;
            String mytype = "";
            switch (testtype) {
                case HTTP_DOWNLOAD:
                    mytype = "1";
                    break;
                case HXBOX:
                    mytype = "2";
                    break;
                case TCP_SPEEDTEST:
                    mytype = "3";
                    break;
                case GD10000:
                    mytype = "4";
                    break;
                case HUNAN10000:
                    mytype = "5";
                    break;
            }
            try {
                cursor = database.query(TABLE_NAME, Columns, null, null, null, null, null, null);
                ContentValues cv = new ContentValues();
                cv.put(COLUMNNAME_TESTTYPE,mytype);
                cv.put(COLUMNNAME_DOWNLOAD_URL,downloadurl);
                if (cursor.getCount() == 0) {
                    if (database.insert(TABLE_NAME, "", cv) != -1)
                        r = true;
                    else
                        r = false;
                } else {
                    if (database.update(TABLE_NAME, cv, null, null) != -1)
                        r = true;
                    else
                        r = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                r = false;
            } finally {
                if(cursor != null)
                    cursor.close();
                return r;
            }
        } else
            return false;
    }

    public boolean updateUserInfo(HxBoxBean bean) {
        if(database.isOpen()) {
            Cursor cursor = null;
            boolean r = false;
            try {
                cursor = database.query(TABLE_NAME, Columns, null, null, null, null, null, null);
                ContentValues cv = new ContentValues();
                cv.put(COLUMNNAME_HXBOX_USERNAME, bean.getUserName());
                cv.put(COLUMNNAME_HXBOX_PASSWORD, bean.getPwd());
                cv.put(COLUMNNAME_HXBOX_USERID, bean.getUserid());
                if (cursor.getCount() == 0) {
                    if (database.insert(TABLE_NAME, "", cv) != -1)
                        r = true;
                    else
                        r = false;
                } else {
                    if (database.update(TABLE_NAME, cv, null, null) != -1)
                        r = true;
                    else
                        r = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                r = false;
            } finally {
                if(cursor != null)
                    cursor.close();
                return r;
            }
        } else
            return false;
    }

    public boolean updateStaticInfo(StaticInfoBean info) {
        if(database.isOpen()) {
            Cursor cursor = null;
            boolean r = false;
            try {
                cursor =  database.query(TABLE_NAME,Columns,null,null,null,null,null,null);
                ContentValues cv = new ContentValues();
                cv.put(COLUMNNAME_STATIC_IP,info.getIpString());
                cv.put(COLUMNNAME_STATIC_NETMASK,info.getNetMask());
                cv.put(COLUMNNAME_STATIC_GATEWAY,info.getGateWay());
                cv.put(COLUMNNAME_STATIC_DNS,info.getDNS());
                if(cursor.getCount() == 0) {
                    if(database.insert(TABLE_NAME,"",cv) != -1)
                        r = true;
                    else
                        r = false;
                } else {
                    if(database.update(TABLE_NAME,cv,null,null) != -1)
                        r = true;
                    else
                        r = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                r = false;
            } finally {
                if(cursor != null)
                    cursor.close();
                return r;
            }
        } else
            return false;
    }

    public boolean updatePPPoEInfo(PPPoEInfoBean info) {
        if(database.isOpen()) {
            Cursor cursor = null;
            boolean r = false;
            try {
                cursor =  database.query(TABLE_NAME,Columns,null,null,null,null,null,null);
                ContentValues cv = new ContentValues();
                cv.put(COLUMNNAME_PPPOE_ACCOUNT,info.getPPPoEAccount());
                cv.put(COLUMNNAME_PPPOE_PASSWORD,info.getPPPoEPassword());
                if(cursor.getCount() == 0) {
                    if(database.insert(TABLE_NAME,"",cv) != -1)
                        r = true;
                    else
                        r = false;
                } else {
                    if(database.update(TABLE_NAME,cv,null,null) != -1)
                        r = true;
                    else
                        r = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                r = false;
            } finally {
                if(cursor != null)
                    cursor.close();
                return r;
            }
        } else
            return false;
    }

    public SpeedTestKind getTestType() {
        if(database.isOpen()) {
            Cursor cursor = null;
            SpeedTestKind testtype = SpeedTestKind.HTTP_DOWNLOAD;
            try {
                cursor = database.query(TABLE_NAME, Columns, null, null, null, null, null, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(COLUMNNAME_TESTTYPE);
                    switch (cursor.getString(columnIndex)) {
                        case "1":
                            testtype = SpeedTestKind.HTTP_DOWNLOAD;
                            break;
                        case "2":
                            testtype = SpeedTestKind.HXBOX;
                            break;
                        case "3":
                            testtype = SpeedTestKind.TCP_SPEEDTEST;
                            break;
                        case "4" :
                            testtype = SpeedTestKind.GD10000;
                            break;
                        case "5":
                            testtype = SpeedTestKind.HUNAN10000;
                            break;
                    }
                } else {
                    return SpeedTestKind.HTTP_DOWNLOAD;
                }
            } catch (Exception e) {
                testtype = SpeedTestKind.HTTP_DOWNLOAD;
            } finally {
                if(cursor != null)
                    cursor.close();
                return testtype;
            }
        } else
            return SpeedTestKind.HTTP_DOWNLOAD;
    }

    public String getDownloadUrl() {
        if(database.isOpen()) {
            Cursor cursor = null;
            String url = "";
            try {
                cursor = database.query(TABLE_NAME, Columns, null, null, null, null, null, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(COLUMNNAME_DOWNLOAD_URL);
                    url = cursor.getString(columnIndex);
                } else {
                    url = "";
                }
            } catch (Exception e) {
                url = "";
            } finally {
                if(cursor != null)
                    cursor.close();
                return url;
            }
        } else
            return "";
    }

    public HxBoxBean getHxTestInfo() {
        if(database.isOpen()) {
            Cursor cursor = null;
            HxBoxBean info = null;
            try {
                cursor = database.query(TABLE_NAME, Columns, null, null, null, null, null, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int usernameColumnIndex = cursor.getColumnIndex(COLUMNNAME_HXBOX_USERNAME);
                    int userpwdColumnIndex = cursor.getColumnIndex(COLUMNNAME_HXBOX_PASSWORD);
                    int useridColumnIndex = cursor.getColumnIndex(COLUMNNAME_HXBOX_USERID);
                    info = new HxBoxBean();
                    info.setUserName(cursor.getString(usernameColumnIndex));
                    info.setPwd(cursor.getString(userpwdColumnIndex));
                    info.setUserid(cursor.getString(useridColumnIndex));
                } else {
                    return null;
                }
            } catch (Exception e) {
                info = null;
            } finally {
                if(cursor != null)
                    cursor.close();
                return info;
            }
        } else
            return null;
    }

    public StaticInfoBean getStaticInfo() {
        if(database.isOpen()) {
            Cursor cursor = null;
            StaticInfoBean info = null;
            try {
                cursor = database.query(TABLE_NAME, Columns, null, null, null, null, null, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int ipColumnIndex = cursor.getColumnIndex(COLUMNNAME_STATIC_IP);
                    int maskColumnIndex = cursor.getColumnIndex(COLUMNNAME_STATIC_NETMASK);
                    int gwColumnIndex = cursor.getColumnIndex(COLUMNNAME_STATIC_GATEWAY);
                    int dnsColumnIndex = cursor.getColumnIndex(COLUMNNAME_STATIC_DNS);
                    info = new StaticInfoBean();
                    info.setIpString(cursor.getString(ipColumnIndex));
                    info.setNetMask(cursor.getString(maskColumnIndex));
                    info.setGateWay(cursor.getString(gwColumnIndex));
                    info.setDNS(cursor.getString(dnsColumnIndex));
                } else {
                    return null;
                }
            } catch (Exception e) {
                info = null;
            } finally {
                if(cursor != null)
                    cursor.close();
                return info;
            }
        } else
            return null;
    }

    public PPPoEInfoBean getPPPoEInfo() {
        if(database.isOpen()) {
            Cursor cursor = null;
            PPPoEInfoBean info = null;
            try {
                cursor = database.query(TABLE_NAME, Columns, null, null, null, null, null, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int userColumnIndex = cursor.getColumnIndex(COLUMNNAME_PPPOE_ACCOUNT);
                    int passColumnIndex = cursor.getColumnIndex(COLUMNNAME_PPPOE_PASSWORD);
                    info = new PPPoEInfoBean();
                    info.setPPPoEAccount(cursor.getString(userColumnIndex));
                    info.setPPPoEPassword(cursor.getString(passColumnIndex));
                }
            } catch (Exception ex) {
                info = null;
            }
            finally {
                if(cursor != null)
                    cursor.close();
                return info;
            }
        } else
            return null;
    }
}
