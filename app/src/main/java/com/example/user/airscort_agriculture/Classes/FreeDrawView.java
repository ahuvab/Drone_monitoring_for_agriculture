package com.example.user.airscort_agriculture.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.user.airscort_agriculture.Interfaces.FreeDrawInterface;
import com.example.user.airscort_agriculture.R;

import java.util.ArrayList;

/*
This class allows to user free hand draw on map
 */
public class FreeDrawView extends View {
    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    private ArrayList<PointInFloat> drawPath;
    private ArrayList<PointInFloat> straightPath;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    private static final int CHANGE_RANGE = 16;
    private FreeDrawInterface mFreeDrawInterface;
    private Context context;

    public FreeDrawView(Context c) {
        super(c);
        context=c;
        init(null, 0);
    }

    public FreeDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(attrs, 0);
    }

    public FreeDrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
        init(attrs, defStyle);
    }

    public void init(AttributeSet attrs, int defStyle) {
        drawPath=new ArrayList<PointInFloat>();
        straightPath=new ArrayList<PointInFloat>();
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(7);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w-(getPaddingLeft()+getPaddingRight());
        height=h-(getPaddingTop()+getPaddingBottom());
        if(width > 0 && height > 0) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath( mPath,  mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    private void touch_start(float x, float y) {
        mPath.moveTo(x, y);
        drawPath.add(new PointInFloat(x, y));
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        drawPath.add(new PointInFloat(x, y));
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        convertToStraightLines();

        if(straightPath.size()>2) {
            mFreeDrawInterface.convertPixelToLatLng(straightPath);     //convert to map
            mFreeDrawInterface.drawOnMapFragment();
        }
        else{
            Toast.makeText(context, context.getString(R.string.define_close_area), Toast.LENGTH_SHORT).show();
        }
        cleanCanvas();
    }

    public void cleanCanvas(){
        mPath.reset();
        drawPath.clear();
        straightPath.clear();
        mBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }

    /* convert the draw path to straight lines by change slope  realtive to angle with the x axis */
    public void convertToStraightLines(){
        straightPath.add(drawPath.get(0));
        PointInFloat p=drawPath.get(0);
        for(int i=2; i<drawPath.size(); i++){
            float deltaY1=drawPath.get(i-1).getY()-p.getY();
            float deltaX1=drawPath.get(i-1).getX()-p.getX();
            double degree1= Math.toDegrees(Math.atan2(deltaY1, deltaX1));
            float deltaY2=drawPath.get(i).getY()-drawPath.get(i-1).getY();
            float deltaX2=drawPath.get(i).getX()-drawPath.get(i-1).getX();
            double degree2= Math.toDegrees(Math.atan2(deltaY2, deltaX2));
            if(Math.abs(degree1-degree2)>CHANGE_RANGE){
                straightPath.add(drawPath.get(i-1));
                p = drawPath.get(i-1);
            }
        }
        if(straightPath.get(straightPath.size()-1).getXDistance(straightPath.get(0))<70 && straightPath.get(straightPath.size()-1).getYDistance(straightPath.get(0))<70 ){
            straightPath.remove(straightPath.size() - 1);
        }
    }

    public void setFreeDrawInterface( FreeDrawInterface m ) {
        mFreeDrawInterface=m;
    }

}
