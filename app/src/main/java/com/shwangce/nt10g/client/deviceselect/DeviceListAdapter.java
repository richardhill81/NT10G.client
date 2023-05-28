package com.shwangce.nt10g.client.deviceselect;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class DeviceListAdapter extends BaseAdapter  {
    Context context;
    List<DeviceBean> deviceList = new ArrayList<>();
    LayoutInflater inflater;

    public DeviceListAdapter(Context context, ArrayList list){
        this.context = context;
        this.deviceList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public DeviceBean getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.device_name, parent,false);
            holder = new ViewHolder();
            holder.tv_devicename = convertView.findViewById(R.id.tv_bluetooth_devicename);
            holder.tv_deviceaddress = convertView.findViewById(R.id.tv_bluetooth_deviceaddress);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        DeviceBean deviceBean = deviceList.get(position);
        String devicename = deviceBean.getDeviceName();
        String deviceaddress = deviceBean.getDeviceMac();
        holder.tv_devicename.setText(devicename);
        holder.tv_deviceaddress.setText(deviceaddress);
        return convertView;
    }

    static class ViewHolder{
        TextView tv_devicename;
        TextView tv_deviceaddress;
    }
}
