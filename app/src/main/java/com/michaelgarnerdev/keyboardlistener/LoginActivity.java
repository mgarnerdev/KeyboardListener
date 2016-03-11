package com.michaelgarnerdev.keyboardlistener;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements RelativeKeyboardListenerLayout.SoftKeyboardVisibilityChangeListener, OnClickListener {

    private LinearLayout mLoginFormView;
    private RelativeKeyboardListenerLayout mRlParentLayout;
    private float mPixelDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            if (BuildConfig.FLAVOR.equals("workingFlavor")) {
                getSupportActionBar().setTitle("WORKING - KeyboardListener");
            } else {
                getSupportActionBar().setTitle("BROKEN - KeyboardListener");
            }
        }
        mPixelDensity = getResources().getDisplayMetrics().density;

        //Setup KeyboardListener
        mRlParentLayout = (RelativeKeyboardListenerLayout) findViewById(R.id.parent_layout);
        mRlParentLayout.setOnSoftKeyboardVisibilityChangeListener(this);

        mLoginFormView = (LinearLayout) findViewById(R.id.login_form);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                closeKeyboard();
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        closeKeyboard();
    }

    private void closeKeyboard() {
        if (mRlParentLayout != null && BuildConfig.FLAVOR.equals("workingFlavor")) {
            mRlParentLayout.setKeyboardClosed(this);
        }
    }

    @Override
    public void onSoftKeyboardShow() {
        if (BuildConfig.FLAVOR.equals("workingFlavor")) {
            handleKeyboardShown();
        }
    }

    @Override
    public void onSoftKeyboardHide() {
        if (BuildConfig.FLAVOR.equals("workingFlavor")) {
            handleKeyboardHidden();
        }
    }

    private void handleKeyboardShown() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoginFormView.getLayoutParams();
        final int targetTopMargin = dpToPx(20);
        final ValueAnimator marginTopValueAnimator = ValueAnimator.ofInt(layoutParams.topMargin, targetTopMargin);
        marginTopValueAnimator.setDuration(300);
        marginTopValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoginFormView.getLayoutParams();
                layoutParams.topMargin = (Integer) animation.getAnimatedValue();
                mLoginFormView.setLayoutParams(layoutParams);
            }
        });
        marginTopValueAnimator.start();
    }

    private void handleKeyboardHidden() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoginFormView.getLayoutParams();
        final int targetMargin = dpToPx(200);
        ValueAnimator marginTopValueAnimator = ValueAnimator.ofInt(layoutParams.topMargin, targetMargin);
        marginTopValueAnimator.setDuration(300);
        marginTopValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoginFormView.getLayoutParams();
                layoutParams.topMargin = (Integer) animation.getAnimatedValue();
                mLoginFormView.setLayoutParams(layoutParams);
            }
        });
        marginTopValueAnimator.start();
    }

    public int dpToPx(int dp) {
        return Math.round((float) dp * mPixelDensity);
    }
}

