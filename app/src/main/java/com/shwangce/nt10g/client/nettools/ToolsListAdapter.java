package com.shwangce.nt10g.client.nettools;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.library.WorkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class ToolsListAdapter extends BaseAdapter  {
    Context context;
    List<String> toolsNameList = new ArrayList<>();
    List<WorkUtils.NetToolsType> toolsList = new ArrayList<>();
    LayoutInflater inflater;

    public ToolsListAdapter(Context context, ArrayList list){
        this.context = context;
        this.toolsNameList = list;
        for(int i =0;i<list.size();i++) {
            String name = toolsNameList.get(i);
            if(name.indexOf("PING")>=0) {
                toolsList.add(WorkUtils.NetToolsType.PING);
            } else if(name.indexOf("TRACEROUTE") >=0 ) {
                toolsList.add(WorkUtils.NetToolsType.TRACEROUTE);
            } else if(name.indexOf("对测") >=0) {
                toolsList.add(WorkUtils.NetToolsType.IPERF);
            } else if(name.indexOf("路由跟踪") >=0 ) {
                toolsList.add(WorkUtils.NetToolsType.PINGANDTRACEROUTE);
            } else if(name.indexOf("定时测试") >=0 ) {
                toolsList.add(WorkUtils.NetToolsType.TIMERSCHEDULE);
            } else if(name.indexOf("网站测试") >=0 ){
                toolsList.add(WorkUtils.NetToolsType.WEBSITETEST);
            } else if(name.indexOf("DNS测试") >=0 ){
                toolsList.add(WorkUtils.NetToolsType.DNSTEST);
            } else {
                toolsList.add(WorkUtils.NetToolsType.NULL);
            }
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return toolsNameList.size();
    }

    @Override
    public WorkUtils.NetToolsType getItem(int position) {
        return toolsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.singleitem, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String devicename = toolsNameList.get(position);
        holder.name.setText(devicename);
        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.tv_bluetooth_devicename) TextView name;
        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
