package com.example.slidingconflict;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class XHorizontalScrollView extends ViewGroup {

    private static final String  TAG="XHorizontalScrollView";
    private int  mChildrenSize;
    private  int  mChildWith;
    private  int  mChildIndex;

    //分别记录上次滑动的坐标
    private int mLastX=0;
    private int mLastY=0;

    private int mLastXIntercept;
    private int mLastYIntercept;

    private Scroller   mScroller;
    private VelocityTracker  mVelocityTracker;

    public XHorizontalScrollView(Context context) {
        super(context);
        init( );
    }

    public XHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init( );
    }

    public XHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init( );
    }

    private void init() {
        if(mScroller==null){
            mScroller=new Scroller(getContext());
            mVelocityTracker=VelocityTracker.obtain();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
         boolean  intercepted=false;
         int x= (int) event.getX();
         int y= (int) event.getY();
         switch (event.getAction()){
             case MotionEvent.ACTION_DOWN:
                 intercepted=false;
                 if(!mScroller.isFinished()){
                     mScroller.abortAnimation();
                     intercepted=true;
                 }
             break;
             case MotionEvent.ACTION_MOVE:
               int detailX=x-mLastXIntercept;
               int detailY=y-mLastYIntercept;
               if(Math.abs(detailX)>Math.abs(detailY)){
                    intercepted=true;
               }else{
                    intercepted=false;
               }
             break;
             case MotionEvent.ACTION_UP:
                 intercepted=false;
              break;
         }
         mLastY=y;
         mLastX=x;
         mLastYIntercept=y;
         mLastXIntercept=x;
         return  intercepted;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x= (int) event.getX();
        int y= (int) event.getY();
        switch (event.getAction()){
            case  MotionEvent.ACTION_DOWN:
              if(!mScroller.isFinished()){
                  mScroller.abortAnimation();
              }
             break;
              case MotionEvent.ACTION_MOVE:
                  int  detailX=x-mLastX;
                  int  detailY=y-mLastY;
                  scrollBy(-detailX,0);
                  break;
            case  MotionEvent.ACTION_UP:
                int  scrollX=getScrollX();
                mVelocityTracker.computeCurrentVelocity(1000);
                float  xVelocity=mVelocityTracker.getXVelocity();
                if(Math.abs(xVelocity)>=50){
                    mChildIndex=xVelocity>0?mChildIndex-1:mChildIndex+1;
                }else{
                    mChildIndex=(scrollX+mChildWith/2)/mChildWith;
                }
                mChildIndex=Math.max(0,Math.min(mChildIndex,mChildrenSize-1));
                int dx=mChildIndex*mChildWith-scrollX;
                smoothScrollBy(dx,0);
                mVelocityTracker.clear();
                break;
        }
        mLastX=x;
        mLastY=y;
        return  true;
    }

    private  void smoothScrollBy(int  dx, int  dy){
        mScroller.startScroll(getScrollX(),0,dx,0,500);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int  measuredWidth=0;
        int  measureHeight=0;
        final  int  childCount=getChildCount();
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        int widthSpaceSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        if(childCount==0){
           setMeasuredDimension(0,0);
        }else if(widthSpecMode==MeasureSpec.AT_MOST&&heightSpecMode==MeasureSpec.AT_MOST){
           final View  childView=getChildAt(0);
           measuredWidth=childView.getMeasuredWidth()*childCount;
           measureHeight=childView.getMeasuredHeight();
           setMeasuredDimension(measuredWidth,measureHeight);
        }else if(heightSpecMode==MeasureSpec.AT_MOST){
            final View childView=getChildAt(0);
            measureHeight=childView.getMeasuredHeight();
            setMeasuredDimension(widthSpaceSize,measureHeight);
        }else if(widthSpecMode==MeasureSpec.AT_MOST){
            final View childView=getChildAt(0);
            measuredWidth=childView.getMeasuredWidth()*childCount;
            setMeasuredDimension(measuredWidth,heightMeasureSpec);
        }
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int  childLeft=0;
        final  int  childCount=getChildCount();
        mChildrenSize=childCount;
        for (int i=0;i<childCount;i++){
            final  View childView=getChildAt(i);
            if(childView.getVisibility()!=GONE){
                final  int childWidth=childView.getMeasuredWidth();
                mChildWith=childWidth;
                childView.layout(childLeft,0,childLeft+childWidth,childView.getMeasuredHeight());
                childLeft+=childWidth;
            }
        }
    }

    @Override
    public void computeScroll() {
       if(mScroller.computeScrollOffset()){
           scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
           postInvalidate();
       }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }
}








