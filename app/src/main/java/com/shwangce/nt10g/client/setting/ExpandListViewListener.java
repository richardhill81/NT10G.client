package com.shwangce.nt10g.client.setting;

/**
 * Created by Administrator on 2018/12/14.
 */

public interface ExpandListViewListener {
    void doGroupCheckClick(boolean isChecked, int groupPos);
    void doChildCheckClick(boolean isChecked, int groupPos, int childPos);
}
