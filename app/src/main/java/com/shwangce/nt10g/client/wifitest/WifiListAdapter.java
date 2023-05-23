package com.shwangce.nt10g.client.wifitest;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shwangce.nt10g.client.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class WifiListAdapter extends BaseAdapter  {
    Context context;
    List<WifiBean> wifiBeans = new ArrayList<>();
    LayoutInflater inflater;

    public WifiListAdapter(Context context, ArrayList list){
        this.context = context;
        this.wifiBeans = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return wifiBeans.size();
    }

    @Override
    public WifiBean getItem(int position) {
        return wifiBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        WifiListAdapter.ViewHolder holder ;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.wifi_info, null);
            holder = new WifiListAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (WifiListAdapter.ViewHolder) convertView.getTag();
        }
        WifiBean wifiBean = wifiBeans.get(position);
        holder.tv_ssid.setText(wifiBean.getEssid());
        String security = wifiBean.getSecurityModeString();
        //String signalLevel = wifiBean.getSignalLevel() + "";
        String signalLevel = wifiBean.getSignalLevel() + "dBm";
        holder.tv_security.setText(security);
        holder.tv_signallevel.setText(signalLevel);
        //holder.tv_level.setText(level);
        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.tv_wifi_ssid) TextView tv_ssid;
        @BindView(R.id.tv_wifi_security) TextView tv_security;
        @BindView(R.id.tv_wifi_signallevel) TextView tv_signallevel;
        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
