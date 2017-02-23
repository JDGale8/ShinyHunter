package dallasapps.shine;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Jake on 1/8/2017.
 */
public class SlidingContainer extends LinearLayout {

    private View menu;      // The sliding menu
    private View content;   // The content

    protected static final int menuMargin = 300;



    public enum menuState{
        CLOSED, OPEN, CLOSING, OPENING
    };


    // PROTECTED INFORMATION
    protected int currentContentOffset = 0;
    protected menuState menuCurrentState = menuState.CLOSED;

    // ANIMATION INFORMATION
    protected Scroller menuAnimationScroller = new Scroller(this.getContext(), new smoothInterpolator());
    protected Runnable menuAnimationRunnable = new AnimationRunnable();
    protected Handler menuAnimationHandler = new Handler();

    private static final int menuAnimationDuration = 250;
    private static final int menuAnimationPollingInterval = 4;

    // extensions from Linear Layout
    public SlidingContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public SlidingContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SlidingContainer(Context context) {
        super(context);
    }

    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();

        this.menu = this.getChildAt(0);
        this.content = this.getChildAt(1);

        this.menu.setVisibility(View.GONE);
    }



    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom){
        if(changed){
            this.calculateChildDimensions();
        }
        this.menu.layout(left, top, right - menuMargin, bottom);

        this.content.layout(left + this.currentContentOffset, top, right + this.currentContentOffset, bottom);
    }



    public void toggleMenu(){
        switch (this.menuCurrentState){
            case CLOSED:
                this.menuCurrentState = menuState.OPENING;
                this.menu.setVisibility(View.VISIBLE);
                this.currentContentOffset = this.getMenuWidth();
                this.menuAnimationScroller.startScroll(0, 0,    // current position
                        this.getMenuWidth(),                    // x translation
                        0,                                      // y translation
                        menuAnimationDuration);                 // duration
                break;
            case OPEN:
                this.menuCurrentState = menuState.CLOSING;
                this.menuAnimationScroller.startScroll(this.currentContentOffset, 0,    // current position
                        -this.currentContentOffset,                    // x translation
                        0,                                      // y translation
                        menuAnimationDuration);                 // duration
                break;
            default:
                return;
        }
        this.menuAnimationHandler.postDelayed(this.menuAnimationRunnable,
                menuAnimationPollingInterval);
    }


    public int getMenuWidth() {
        return this.menu.getLayoutParams().width;
    }

    private void calculateChildDimensions() {
        this.content.getLayoutParams().height = this.getHeight();
        this.content.getLayoutParams().width = this.getWidth();

        this.menu.getLayoutParams().height = this.getHeight();
        this.menu.getLayoutParams().width = this.getWidth() - menuMargin;


    }

    protected class AnimationRunnable implements Runnable {

        @Override
        public void run() {
            SlidingContainer.this
                    .adjustContentPosition(SlidingContainer.this.menuAnimationScroller
                            .computeScrollOffset());
        }

    }

    private void adjustContentPosition(boolean isAnimationOngoing) {
        int scrollerOffset = this.menuAnimationScroller.getCurrX();

        this.content.offsetLeftAndRight(scrollerOffset - this.currentContentOffset);

        this.currentContentOffset = scrollerOffset;

        this.invalidate();

        if(isAnimationOngoing){
            this.menuAnimationHandler.postDelayed(this.menuAnimationRunnable, menuAnimationPollingInterval);
        }
        else
            this.onMenuTransitionComplete();

    }

    private void onMenuTransitionComplete() {
        switch (this.menuCurrentState){
            case OPENING:
                this.menuCurrentState = menuState.OPEN;
                break;
            case CLOSING:
                this.menuCurrentState = menuState.CLOSED;
                this.menu.setVisibility(View.GONE);
                break;
            default:
                return;
        }
    }

    protected class smoothInterpolator implements Interpolator{

        @Override
        public float getInterpolation(float t) {
            return (float) Math.pow(t-1,5) + 1;
        }
    }

}
