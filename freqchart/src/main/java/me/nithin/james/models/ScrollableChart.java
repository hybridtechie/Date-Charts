package me.nithin.james.models;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Scroller;

import me.nithin.james.utils.BundleSavedState;

public abstract class ScrollableChart extends View
        implements GestureDetector.OnGestureListener,
        ValueAnimator.AnimatorUpdateListener {

    private int dataOffset;

    private int scrollerBucketSize = 1;

    private int direction = 1;

    private GestureDetector detector;

    private Scroller scroller;

    private ValueAnimator scrollAnimator;

    private ScrollController scrollController;

    private int maxDataOffset = 10000;

    public ScrollableChart(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        detector = new GestureDetector(context, this);
        scroller = new Scroller(context, null, true);
        scrollAnimator = ValueAnimator.ofFloat(0, 1);
        scrollAnimator.addUpdateListener(this);
        scrollController = new ScrollController() {
        };
    }

    public ScrollableChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public int getDataOffset() {
        return dataOffset;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (!scroller.isFinished()) {
            scroller.computeScrollOffset();
            updateDataOffset();
        } else {
            scrollAnimator.cancel();
        }
    }

    private void updateDataOffset() {
        int newDataOffset = scroller.getCurrX() / scrollerBucketSize;
        newDataOffset = Math.max(0, newDataOffset);
        newDataOffset = Math.min(maxDataOffset, newDataOffset);

        if (newDataOffset != dataOffset) {
            dataOffset = newDataOffset;
            //  scrollController.onDataOffsetChanged(dataOffset);
            postInvalidate();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
        if (scrollerBucketSize == 0) return false;

        if (Math.abs(dx) > Math.abs(dy)) {
            ViewParent parent = getParent();
            if (parent != null) parent.requestDisallowInterceptTouchEvent(true);
        }

        dx = -direction * dx;
        dx = Math.min(dx, getMaxX() - scroller.getCurrX());
        scroller.startScroll(scroller.getCurrX(), scroller.getCurrY(), (int) dx,
                (int) dy, 0);

        scroller.computeScrollOffset();
        updateDataOffset();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1,
                           MotionEvent e2,
                           float velocityX,
                           float velocityY) {
        scroller.fling(scroller.getCurrX(), scroller.getCurrY(),
                direction * ((int) velocityX) / 2, 0, 0, getMaxX(), 0, 0);
        invalidate();

        scrollAnimator.setDuration(scroller.getDuration());
        scrollAnimator.start();
        return false;
    }

    private int getMaxX() {
        return maxDataOffset * scrollerBucketSize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putInt("x", scroller.getCurrX());
        bundle.putInt("y", scroller.getCurrY());
        bundle.putInt("dataOffset", dataOffset);
        bundle.putInt("direction", direction);
        bundle.putInt("maxDataOffset", maxDataOffset);
        return new BundleSavedState(superState, bundle);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof BundleSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        BundleSavedState bss = (BundleSavedState) state;
        int x = bss.bundle.getInt("x");
        int y = bss.bundle.getInt("y");
        direction = bss.bundle.getInt("direction");
        dataOffset = bss.bundle.getInt("dataOffset");
        maxDataOffset = bss.bundle.getInt("maxDataOffset");
        scroller.startScroll(0, 0, x, y, 0);
        scroller.computeScrollOffset();
        super.onRestoreInstanceState(bss.getSuperState());
    }

    public void setScrollDirection(int direction) {
        if (direction != 1 && direction != -1)
            throw new IllegalArgumentException();
        this.direction = direction;
    }

    public void setMaxDataOffset(int maxDataOffset) {
        this.maxDataOffset = maxDataOffset;
        this.dataOffset = Math.min(dataOffset, maxDataOffset);
        //scrollController.onDataOffsetChanged(this.dataOffset);
        postInvalidate();
    }

    public void setScrollController(ScrollController scrollController) {
        this.scrollController = scrollController;
    }

    public void setScrollerBucketSize(int scrollerBucketSize) {
        this.scrollerBucketSize = scrollerBucketSize;
    }

    public interface ScrollController {
        // default void onDataOffsetChanged(int newDataOffset) {}
    }
}
