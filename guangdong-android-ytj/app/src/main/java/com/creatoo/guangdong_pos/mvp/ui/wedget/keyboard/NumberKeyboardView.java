package com.creatoo.guangdong_pos.mvp.ui.wedget.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import com.creatoo.guangdong_pos.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by yijiangtao on 2017/7/21.
 */

public class NumberKeyboardView extends KeyboardView {

    private Context mContext;

    public NumberKeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumberKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            // 数字键盘的处理
            if (key.codes[0] == -5 || key.codes[0] == 127) {
                //drawKeyBackground(R.drawable.bg_keyboardview_yes, canvas, key);
                //drawKeyBackground(canvas,key);
                //drawText(canvas, key);
            }
            // 数字键盘的处理
            if (key.codes[0] == -4) {
                drawKeyBackground(R.drawable.bg_keyboardview_yes, canvas, key);
                drawText(canvas, key, Color.WHITE);
            }
        }
    }


    private void drawKeyBackground(Canvas canvas, Keyboard.Key key) {
        Field field;
        Drawable npd = null;
        try {
            field = KeyboardView.class.getDeclaredField("mKeyBackground");
            field.setAccessible(true);
            npd = (Drawable) field.get(this);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(null != npd){
            int[] drawableState = key.getCurrentDrawableState();
            if (key.codes[0] != 0) {
                npd.setState(drawableState);
            }
            /*npd.setBounds(key.x + horizontalGap, key.y + verticalGap, key.x + key.width - horizontalGap, key.y
                    + key.height - verticalGap);*/
            npd.setBounds(key.x + key.gap, key.y + key.gap, key.x + key.width - key.gap, key.y
                    + key.height - key.gap);
            npd.draw(canvas);
        }
    }


    private void drawKeyBackground(int drawableId, Canvas canvas, Keyboard.Key key) {
        Drawable npd = mContext.getResources().getDrawable(
                drawableId);
        int[] drawableState = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            npd.setState(drawableState);
        }
        npd.setBounds(key.x, key.y, key.x + key.width, key.y
                + key.height);
        npd.draw(canvas);
    }

    private void drawText(Canvas canvas, Keyboard.Key key) {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);


        paint.setAntiAlias(true);

        paint.setColor(Color.WHITE);
        if (key.label != null) {
            String label = key.label.toString();

            Field field;

            if (label.length() > 1 && key.codes.length < 2) {
                int labelTextSize = 0;
                int labelTextColor = Color.WHITE;
                try {
                    field = KeyboardView.class.getDeclaredField("mLabelTextSize");
                    field.setAccessible(true);
                    labelTextSize = (int) field.get(this);

                    field = KeyboardView.class.getDeclaredField("mKeyTextColor");
                    field.setAccessible(true);
                    labelTextColor = (int) field.get(this);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                paint.setColor(labelTextColor);
                paint.setTextSize(labelTextSize);
                paint.setTypeface(Typeface.DEFAULT);
            } else {
                int keyTextSize = 0;
                int keyTextColor = Color.WHITE;
                try {
                    field = KeyboardView.class.getDeclaredField("mKeyTextSize");
                    field.setAccessible(true);
                    keyTextSize = (int) field.get(this);

                    field = KeyboardView.class.getDeclaredField("mKeyTextColor");
                    field.setAccessible(true);
                    keyTextColor = (int) field.get(this);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                paint.setColor(keyTextColor);
                paint.setTextSize(keyTextSize);
                paint.setTypeface(Typeface.DEFAULT);
            }

            paint.getTextBounds(key.label.toString(), 0, key.label.toString()
                    .length(), bounds);
            canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                    (key.y + key.height / 2) + bounds.height() / 2, paint);
        } else if (key.icon != null) {
            key.icon.setBounds(key.x + (key.width - key.icon.getIntrinsicWidth()) / 2, key.y + (key.height - key.icon.getIntrinsicHeight()) / 2,
                    key.x + (key.width - key.icon.getIntrinsicWidth()) / 2 + key.icon.getIntrinsicWidth(), key.y + (key.height - key.icon.getIntrinsicHeight()) / 2 + key.icon.getIntrinsicHeight());
            key.icon.draw(canvas);
        }
    }


    private void drawText(Canvas canvas, Keyboard.Key key, int color) {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);

        paint.setAntiAlias(true);

        paint.setColor(color);
        if (key.label != null) {
            String label = key.label.toString();

            Field field;

            if (label.length() > 1 && key.codes.length < 2) {
                int labelTextSize = 0;
                try {
                    field = KeyboardView.class.getDeclaredField("mLabelTextSize");
                    field.setAccessible(true);
                    labelTextSize = (int) field.get(this);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                paint.setTextSize(labelTextSize);
                paint.setTypeface(Typeface.DEFAULT);
            } else {
                int keyTextSize = 0;
                try {
                    field = KeyboardView.class.getDeclaredField("mKeyTextSize");
                    field.setAccessible(true);
                    keyTextSize = (int) field.get(this);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                paint.setTextSize(keyTextSize);
                paint.setTypeface(Typeface.DEFAULT);
            }

            paint.getTextBounds(key.label.toString(), 0, key.label.toString()
                    .length(), bounds);
            canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                    (key.y + key.height / 2) + bounds.height() / 2, paint);
        } else if (key.icon != null) {
            key.icon.setBounds(key.x + (key.width - key.icon.getIntrinsicWidth()) / 2, key.y + (key.height - key.icon.getIntrinsicHeight()) / 2,
                    key.x + (key.width - key.icon.getIntrinsicWidth()) / 2 + key.icon.getIntrinsicWidth(), key.y + (key.height - key.icon.getIntrinsicHeight()) / 2 + key.icon.getIntrinsicHeight());
            key.icon.draw(canvas);
        }
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
