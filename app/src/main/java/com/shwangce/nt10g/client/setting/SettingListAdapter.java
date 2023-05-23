package com.shwangce.nt10g.client.setting;


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
public class SettingListAdapter extends BaseAdapter  {
    Context context;
    List<String> settingNameList = new ArrayList<>();

    LayoutInflater inflater;

    public SettingListAdapter(Context context, ArrayList list){
        this.context = context;
        this.settingNameList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return settingNameList.size();
    }

    @Override
    public String getItem(int position) {
        return settingNameList.get(position);
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
        String devicename = settingNameList.get(position);
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
