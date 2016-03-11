package com.michaelgarnerdev.keyboardlistener;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

/**
 * Created by mgarner on 3/8/2016.
 * This class helps with managing the state of the Soft Keyboard in Android.
 * This layout can be used in place of a RelativeLayout as the parent layout for a view.
 * It listens for changes in size and then sends events accordingly which can be listened to in an Activity or Fragment.
 * The activity must specify android:windowSoftInputMode="adjustResize" and should also use "stateHidden" too.
 */
public class RelativeKeyboardListenerLayout extends RelativeLayout {

    private boolean mIsKeyboardShown = false;
    private SoftKeyboardVisibilityChangeListener mKeyboardListener;

    public RelativeKeyboardListenerLayout(Context context) {
        super(context);
    }

    public RelativeKeyboardListenerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeKeyboardListenerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // Keyboard is hidden <<< RIGHT
            if (mIsKeyboardShown) {
                mIsKeyboardShown = false;
                mKeyboardListener.onSoftKeyboardHide();
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int actualHeight = getHeight();
        if (actualHeight > proposedHeight) {
            // Keyboard is shown
            if (!mIsKeyboardShown) {
                mIsKeyboardShown = true;
                if (mKeyboardListener != null) {
                    mKeyboardListener.onSoftKeyboardShow();
                }
            }
        } else if (actualHeight < proposedHeight) {
            //Keyboard might be hidden, not 100% fool proof
            if (mIsKeyboardShown) {
                mIsKeyboardShown = false;
                if (mKeyboardListener != null) {
                    mKeyboardListener.onSoftKeyboardShow();
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnSoftKeyboardVisibilityChangeListener(SoftKeyboardVisibilityChangeListener listener) {
        this.mKeyboardListener = listener;
    }

    public void setKeyboardClosed() {
        if (mIsKeyboardShown) {
            mIsKeyboardShown = false;
        }
        if (mKeyboardListener != null) {
            mKeyboardListener.onSoftKeyboardHide();
        }
    }

    //Can be called from activity or fragment to force the keyboard closed and synchronize the state.
    public void setKeyboardClosed(Activity activity) {
        setKeyboardClosed();
        if (activity != null && !activity.isFinishing()) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public interface SoftKeyboardVisibilityChangeListener {
        void onSoftKeyboardShow();

        void onSoftKeyboardHide();
    }

}