package com.example.mycamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.view.MotionEvent;
import android.widget.Toast;

public class FingerView extends ImageView {

    private Paint currentPaint;
    public boolean drawRect = true;
    public float left;
    public float top;
    public float right;
    public float bottom;
    public static String action;

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint, paintText;
    //initial color
    private int paintColor = Color.CYAN;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;


    public FingerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        currentPaint = new Paint();
        currentPaint.setDither(true);
        currentPaint.setColor(0xFF00CC00);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeJoin(Paint.Join.ROUND);
        currentPaint.setStrokeCap(Paint.Cap.ROUND);
        currentPaint.setStrokeWidth(2);

        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);


        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.GREEN);
        paintText.setTextSize(50);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    if(action == "text"){
                        drawPath.reset();
                        Toast.makeText(this.getContext(), R.string.textualModel, Toast.LENGTH_SHORT).show();
                        drawCanvas.drawText(MainActivity.inputText, touchX, touchY, paintText);
                    }
                    else if(action == "sketch"){
                        Toast.makeText(this.getContext(), R.string.sketchModel, Toast.LENGTH_SHORT).show();
                        drawCanvas.drawPath(drawPath, drawPaint);
                        drawPath.reset();
                    }
                    break;
                default:
                    return false;
            }

        invalidate();
        return true;
    }

    public void clear() {
        drawPath.reset();
        canvasBitmap.eraseColor(Color.TRANSPARENT);
        drawCanvas.drawBitmap(canvasBitmap, 0, 0,canvasPaint);
        invalidate();
    }

}