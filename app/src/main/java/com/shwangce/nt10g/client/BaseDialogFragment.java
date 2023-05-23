package com.shwangce.nt10g.client;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/12/14 0014.
 */

public abstract class BaseDialogFragment extends DialogFragment {
    public BaseDialogFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(false);

    }
    @Override
    public int show(FragmentTransaction transaction, String tag) {
        try {
            Class clazz = Class.forName("android.support.v4.app.DialogFragment");
            Field mDismissed = clazz.getDeclaredField("mDismissed");
            mDismissed.setAccessible(true);
            mDismissed.setBoolean(this,false);
            Field mShownByMe = clazz.getDeclaredField("mShownByMe");
            mShownByMe.setAccessible(true);
            mShownByMe.setBoolean(this,true);
            Field mViewDestroyed = clazz.getDeclaredField("mViewDestroyed");
            mViewDestroyed.setAccessible(true);
            mViewDestroyed.setBoolean(this,false);
            transaction.add(this, tag);
            return transaction.commitAllowingStateLoss();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @return
     */
    public boolean isShowing() {
        return getDialog() != null && getDialog().isShowing();
    }


    @Override
    public void dismiss() {
        if(isShowing())
            super.dismiss();
    }

    public void dismiss(boolean isResume) {
        if(isResume){
            dismiss();
        }else{
            dismissAllowingStateLoss();
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        if(isShowing()){
            super.dismissAllowingStateLoss();
        }
    }


}
