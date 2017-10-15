package com.lyw.zhiji;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyw.zhiji.utils.DialogUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 0013 10-13.
 */

public abstract class BaseFragment extends Fragment {
    private ProgressDialog mDialog;
    protected Context mContext;
    protected View mView;
    protected Bundle mBundle;
    protected LayoutInflater mInflater;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        initBundle(mBundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null)
                parent.removeView(mView);
        } else {
            mView = inflater.inflate(getLayoutId(), container, false);
            mInflater = inflater;
//            this.mInflater = inflater;
//            super.onCreateView(inflater, container, savedInstanceState);
            // Do something
            onBindViewBefore(mView);
            // Get savedInstanceState
            if (savedInstanceState != null)
                onRestartInstance(savedInstanceState);
            // Init
            initView(mView);
            initData();
        }
        return mView;

    }

    protected void onBindViewBefore(View root) {
        // ...
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBundle = null;
    }



    //获取布局id
    protected abstract int getLayoutId();

    protected void initBundle(Bundle bundle) {

    }
    protected abstract void initView(View view) ;

    protected abstract void initData() ;

    protected <T extends Serializable> T getBundleSerializable(String key) {
        if (mBundle == null) {
            return null;
        }
        return (T) mBundle.getSerializable(key);
    }

    /**
     * 获取一个图片加载管理器
     *
     * @return RequestManager
     */


    public boolean onBackPressed() {
        return false;
    }



    protected void onRestartInstance(Bundle bundle) {

    }
    @Override
    public void onStop() {
        super.onStop();
        hideWaitDialog();
    }
    /**
     * show FocusWaitDialog
     *
     * @return progressDialog
     */
    protected ProgressDialog showFocusWaitDialog() {

        String message = getResources().getString(R.string.progress_submit);
        if (mDialog == null) {
            mDialog = DialogUtil.getProgressDialog(getActivity(), message, false);//DialogHelp.getWaitDialog(this, message);
        }
        mDialog.show();

        return mDialog;
    }
    /**
     * hide waitDialog
     */
    protected void hideWaitDialog() {
        ProgressDialog dialog = mDialog;
        if (dialog != null) {
            mDialog = null;
            try {
                dialog.cancel();
                // dialog.dismiss();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
