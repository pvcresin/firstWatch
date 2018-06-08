package com.resin.firstwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    int r = 30, fontS = 20, touchMax = 2;
    int[] x, y;

    public MySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);        // コールバックの設定
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);        // コールバックの設定
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);        // コールバックの設定
    }

    // Surface が作成されたとき(実装必須)
    public void surfaceCreated(SurfaceHolder holder) {
        x = new int [touchMax];
        y = new int [touchMax];

        for (int i = 0; i < touchMax; i++) {
            x[i] = -999;
            y[i] = -999;
        }

        thread = new Thread(this);
        thread.start();
    }
    Thread thread;
    int xx = 10, yy = 10;

    @Override
    public void run() {
        while(thread != null) {
            xx++;
            yy++;
            xx %= 200;
            yy %= 200;

            MotionEvent ev = MotionEvent.obtain(SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis() + 100, MotionEvent.ACTION_DOWN, xx, yy, 0);
            this.onTouchEvent(ev);
            draw();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int count = ev.getPointerCount();

        for (int i = 0; i < touchMax; i++) {
            if (i < count) {
                x[i] = (int)ev.getX(i);
                y[i] = (int)ev.getY(i);
            } else {
                x[i] = -999;
                y[i] = -999;
            }
        }

        draw() ; // 描画
        return true;
    }

    // 描画処理
    public void draw() {
        Canvas canvas = getHolder().lockCanvas();

        if (canvas != null) {
            canvas.drawColor(Color.DKGRAY);

            // 円を描画
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.RED);

            for (int i = 0; i < touchMax; i++) canvas.drawCircle(x[i], y[i], r, paint);

            // テキストを描画
            paint.setTextSize(fontS);
            paint.setColor(Color.RED);
            for (int i = 0; i < touchMax; i++) {
                canvas.drawText("x = " + x[i] + ", y = " + y[i], x[i] - 2 * r, y[i] - r, paint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    // Surface が変更されたとき(実装必須)
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {    }

    // Surface を破棄したとき(実装必須)
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;
    }
}