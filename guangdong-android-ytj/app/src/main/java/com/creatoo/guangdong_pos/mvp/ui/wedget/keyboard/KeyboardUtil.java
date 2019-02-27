package com.creatoo.guangdong_pos.mvp.ui.wedget.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.force.librarybase.utils.logger.Logger;

import java.lang.reflect.Method;

/**
 * Created by yijiangtao on 2017/7/24.
 */

public class KeyboardUtil {
    private static final String TAG = "KeyboardUtil";

    private Context mCtx;

    private Activity mActivity;

    private NumberKeyboardView mKeyboardView;

    private Keyboard mKeyboardNumber;//数字键盘

    private EditText mEditText;

    private OnClickDoneListener mOnClickDoneListener;

    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (null == mEditText) {
                Logger.w(TAG + " mEditText is null");
                return;
            }

            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == 127) { // 清空
                if (editable != null && editable.length() > 0) {
                    editable.clear();
                }
            } else if (primaryCode == -3) {//00
                if (editable != null && editable.length() > 0) {
                    editable.append("00");
                }
            } else if (primaryCode == Keyboard.KEYCODE_DONE) {//完成
                if (null != mOnClickDoneListener) {
                    mOnClickDoneListener.onClickDone();
                }
            } else {
                try {
                    if (!TextUtils.isEmpty(editable.toString()) && Double.parseDouble(editable.toString()) == 0d) {//如果初始从0开始则覆盖
//                        editable.clear();
                        editable.append(Character.toString((char) primaryCode));
                    } else {
                        if (editable.toString().equals("") && ".".equals(Character.toString((char) primaryCode)))
                            return;
                        editable.insert(start, Character.toString((char) primaryCode));
                    }
                } catch (NumberFormatException e) {
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    /**
     * @param ctx
     * @param parent              包含KeyboardView的ViewGroup
     * @param keyboardViewId      KeyboardView的Id
     * @param xmlKeyboardLayoutId Keyboard的样式id
     */
    public KeyboardUtil(Context ctx, View parent, int keyboardViewId, int xmlKeyboardLayoutId) {
        this.mCtx = ctx;
        //此处可替换键盘xml
        mKeyboardNumber = new Keyboard(ctx, xmlKeyboardLayoutId);
        mKeyboardView = (NumberKeyboardView) parent.findViewById(keyboardViewId);
        mKeyboardView.setContext(ctx);
        mKeyboardView.setKeyboard(mKeyboardNumber);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(true);
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
    }

    /**
     * @param activity
     * @param keyboardViewId      KeyboardView的Id
     * @param xmlKeyboardLayoutId Keyboard的样式id
     */
    public KeyboardUtil(Activity activity, int keyboardViewId, int xmlKeyboardLayoutId) {
        this.mCtx = activity.getApplicationContext();
        //此处可替换键盘xml
        mKeyboardNumber = new Keyboard(mCtx, xmlKeyboardLayoutId);
        mKeyboardView = (NumberKeyboardView) activity.findViewById(keyboardViewId);
        mKeyboardView.setContext(mCtx);
        mKeyboardView.setKeyboard(mKeyboardNumber);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(true);
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
    }

    /**
     * edittext绑定自定义键盘
     *
     * @param editText 需要绑定自定义键盘的edittext
     */
    public void attachTo(EditText editText) {
        this.mEditText = editText;
        hideSystemSofeKeyboard(mCtx, mEditText);
        //showSoftKeyboard();
    }

    /**
     * 隐藏系统键盘
     *
     * @param editText
     */
    public static void hideSystemSofeKeyboard(Context context, EditText editText) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 11) {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);

            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            editText.setInputType(InputType.TYPE_NULL);
        }
        // 如果软键盘已经显示，则隐藏
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void setOnClickDoneListener(OnClickDoneListener onClickDoneListener) {
        this.mOnClickDoneListener = onClickDoneListener;
    }

    public interface OnClickDoneListener {
        void onClickDone();
    }
}
