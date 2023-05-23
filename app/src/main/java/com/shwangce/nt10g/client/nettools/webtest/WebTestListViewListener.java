package com.shwangce.nt10g.client.nettools.webtest;

/**
 * Created by Administrator on 2018/12/3.
 */

public interface WebTestListViewListener {
    void onTextChanged(int pos, String webName);
    void onClickDelete(int pos);
}
