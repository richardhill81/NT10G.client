package com.shwangce.nt10g.client.nettools.webtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shwangce.nt10g.client.R;

import java.util.List;

/**
 * Created by Administrator on 2018/12/3.
 */

public class WebNameListAdapter extends BaseAdapter {

    private Context context;

    private List<Integer> list_numbers;

    private List<String> list_webNames;

    private WebTestListViewListener listener;

    private boolean isEnable;

    public WebNameListAdapter(Context context,List list_numbers,List list_webNames,boolean isEnable,WebTestListViewListener listener){
        this.context =context;
        this.list_numbers = list_numbers;
        this.list_webNames = list_webNames;
        this.listener = listener;
        this.isEnable = isEnable;
    }

    @Override
    public int getCount() {
        return list_numbers.size();
    }

    @Override
    public Object getItem(int position) {
        return list_numbers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return isEnable;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHodler hodler;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_content_webtest_listview_webname,null);
            hodler = new ViewHodler();
            hodler.editText = (EditText) view.findViewById(R.id.itemListViewWebName_editText_webName);
            hodler.imageButton = (ImageButton)view.findViewById(R.id.itemListViewWebName_imageButton_delete);
            view.setTag(hodler);
        }else{
            hodler = (ViewHodler) view.getTag();
        }
        hodler.editText.setText(list_webNames.get(position));

        hodler.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("域名");

                final EditText editText = new EditText(context);
                editText.setText(list_webNames.get(position));

                builder.setView(editText);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String webName = editText.getText().toString();
                        listener.onTextChanged(position,webName);
                    }
                });
                builder.setNegativeButton("取消",null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        hodler.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEnable){
                    return;
                }
                listener.onClickDelete(position);
            }
        });

        return view;
    }

    public void setEnable(boolean isEnable){
        this.isEnable = isEnable;
    }

    private class ViewHodler{
        TextView textView;
        EditText editText;
        ImageButton imageButton;
    }
}
