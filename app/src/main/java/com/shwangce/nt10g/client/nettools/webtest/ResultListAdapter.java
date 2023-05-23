package com.shwangce.nt10g.client.nettools.webtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shwangce.nt10g.client.R;

import java.util.List;

/**
 * Created by Administrator on 2018/12/10.
 */

public class ResultListAdapter extends BaseAdapter{

    private Context context;
    private List<String> result_lsit;

    public ResultListAdapter(Context context,List<String> result_lsit){
        this.context = context;
        this.result_lsit = result_lsit;
    }

    @Override
    public int getCount() {
        return result_lsit.size();
    }

    @Override
    public Object getItem(int position) {
        return result_lsit.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHodler hodler;
        if(view == null){
            hodler = new ViewHodler();
            view = LayoutInflater.from(context).inflate(R.layout.item_content_webtest_listview_showresult,null);
            hodler.textView = (TextView) view.findViewById(R.id.itemWebShowResult_textView_showResult);
            view.setTag(hodler);
        }else{
            hodler = (ViewHodler) view.getTag();
        }
        hodler.textView.setText(result_lsit.get(position));

        return view;
    }

    private class ViewHodler{
        TextView textView;
    }
}
