package com.shwangce.nt10g.client.setaccess;

import com.shwangce.nt10g.client.BasePresenter;
import com.shwangce.nt10g.client.BaseView;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public interface SetAccessContract {
    interface View extends BaseView<Presenter> {
        void doShowAccessFail(String failreason);
        void doAccessSuccess();
    }

    interface Presenter extends BasePresenter {
        void doDhcp();
        void doStatic(String ipaddress,String netmask,String gateway,String dns);
        void doPPPoe(String username,String password);
        void doBack();
    }
}
