package com.lyw.zhiji;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.lyw.zhiji.manager.ActivityManager;
import com.lyw.zhiji.utils.DialogUtil;

/**
 * Created by Administrator on 0013 10-13.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mDialog;
    private boolean mIsDestroy;
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        if (initBundle(getIntent().getExtras())) {
            setContentView(getContentView());

            initWindow();
            initWidget();
            initData();
        } else {
            finish();
        }


    }

    protected void addFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
        }
    }

    protected void replaceFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(frameLayoutId, fragment);
            transaction.commit();
        }
    }


    protected abstract int getContentView();

    protected boolean initBundle(Bundle bundle) {
        return true;
    }

    protected abstract void initWindow() ;

    protected abstract void initWidget() ;

    protected abstract void initData() ;

    protected void init(Bundle savedInstanceState) {
    }

    /**
     * show WaitDialog
     *
     * @return progressDialog
     */
    protected ProgressDialog showWaitDialog(@StringRes int messageId) {
        if (mDialog == null) {
            if (messageId <= 0) {
                mDialog = DialogUtil.getProgressDialog(this, true);
            } else {
                String message = getResources().getString(messageId);
                mDialog = DialogUtil.getProgressDialog(this, message, true);
            }
        }
        mDialog.show();

        return mDialog;
    }

    /**
     * show FocusWaitDialog
     *
     * @return progressDialog
     */
    protected ProgressDialog showFocusWaitDialog() {

        String message = getResources().getString(R.string.progress_submit);
        if (mDialog == null) {
            mDialog = DialogUtil.getProgressDialog(this, message, false);//DialogHelp.getWaitDialog(this, message);
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
    @Override
    protected void onDestroy() {
        mIsDestroy = true;
        super.onDestroy();
    }

    public boolean isDestroy() {
        return mIsDestroy;
    }
}
