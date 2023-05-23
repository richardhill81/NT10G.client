package com.shwangce.nt10g.client.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwangce.nt10g.client.R;

import java.util.List;

/**
 * Created by hzj on 2018/12/13.
 */

public class ShowFilesExpandableListViewApdapter extends BaseExpandableListAdapter{

    private List<List<String>>  list_files;
    private List<List<Boolean>> list_isFileChecked;

    private List<String> list_file_types;
    private List<Boolean> list_isFileTypeAllChecked;
    private Context context;

    private ExpandListViewListener listener;

    public ShowFilesExpandableListViewApdapter(Context context,
                                               List<String> list_file_types,
                                               List<Boolean> list_isFileTypeAllChecked,
                                               List<List<String>> list_files,
                                               List<List<Boolean>> list_isFileChecked,
                                               ExpandListViewListener listener){
        this.context = context;
        this.list_file_types = list_file_types;
        this.list_isFileTypeAllChecked = list_isFileTypeAllChecked;

        this.list_files = list_files;
        this.list_isFileChecked = list_isFileChecked;
        this.listener = listener;
    }

    @Override
    public int getGroupCount() {
        return list_files.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list_files.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list_files.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list_files.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        MyHolderGroup holderGroup;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_filemanager_showfile,null);
            holderGroup = new MyHolderGroup();
//            holderGroup.imageView_group = (ImageView) view.findViewById(R.id.fileManager_imageView_group);
            holderGroup.textView_group = (TextView) view.findViewById(R.id.fileManager_textView_group);
            holderGroup.checkBox_group = (CheckBox) view.findViewById(R.id.fileManager_checkBox_group);
            view.setTag(holderGroup);
        }else{
            holderGroup = (MyHolderGroup) view.getTag();
        }

        holderGroup.checkBox_group.setChecked(list_isFileTypeAllChecked.get(groupPosition));

        holderGroup.checkBox_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.doGroupCheckClick(!list_isFileTypeAllChecked.get(groupPosition),groupPosition);
            }
        });

        holderGroup.textView_group.setText(list_file_types.get(groupPosition));

        return view;
    }

    @Override
    public View getChildView(final int groupPosition,final int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        MyHolderChild holderChild;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_filemanager_showfile_child,null);
            holderChild = new MyHolderChild();

            holderChild.textView_child = (TextView) view.findViewById(R.id.fileManager_textView_child);
            holderChild.checkBox_child = (CheckBox) view.findViewById(R.id.fileManager_checkBox_child);

            view.setTag(holderChild);
        }else{
            holderChild = (MyHolderChild) view.getTag();
        }

        holderChild.checkBox_child.setChecked(list_isFileChecked.get(groupPosition).get(childPosition));


        holderChild.textView_child.setText(list_files.get(groupPosition).get(childPosition));

        holderChild.checkBox_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.doChildCheckClick(!list_isFileChecked.get(groupPosition).get(childPosition),groupPosition,childPosition);
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class MyHolderGroup{
        ImageView imageView_group;
        TextView textView_group;
        CheckBox checkBox_group;
    }

    class MyHolderChild{
        TextView textView_child;
        CheckBox checkBox_child;
    }
}
