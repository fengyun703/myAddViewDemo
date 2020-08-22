package com.example.myaddviewtest;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

public class MainActivity2 extends Activity {

    private ViewGroup mParent;
    private ViewGroup mContent;
    private View mChild;
    public static final String TAG = "MainActivity2";
    private MyLayoutTransition mLayoutTransition;
    private Field mTransitioningViewsField;
    private Field mVisibilityChangingChildrenField;
    private Field mDisappearingChildrenField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mParent = findViewById(R.id.parent);
        mContent = findViewById(R.id.content);
        mChild = findViewById(R.id.child);

        mLayoutTransition = new MyLayoutTransition();
        // 先添加我们自己的监听，保证我们自己的先于系统的执行。
        mLayoutTransition.addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                try {
                    Log.i(TAG, "startTransition: mTransitioningViewsField = " + mTransitioningViewsField.get(mContent));
                    Log.i(TAG, "startTransition: mDisappearingChildrenField = " + mDisappearingChildrenField.get(mContent));
                    Log.i(TAG, "startTransition: mVisibilityChangingChildrenField = " + mVisibilityChangingChildrenField.get(mContent));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "startTransition: mContent = " + mContent + ", child = " + mChild + ". child.getParent =" + mChild.getParent() + ", transitionType =" + transitionType);
            }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                try {
                    Log.i(TAG, "endTransition: mTransitioningViewsField = " + mTransitioningViewsField.get(mContent));
                    Log.i(TAG, "endTransition: mDisappearingChildrenField = " + mDisappearingChildrenField.get(mContent));
                    Log.i(TAG, "endTransition: mVisibilityChangingChildrenField = " + mVisibilityChangingChildrenField.get(mContent));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "endTransition: mContent = " + mContent + ", child = " + mChild + ". child.getParent =" + mChild.getParent() + ", transitionType =" + transitionType );
            }
        });
        mContent.setLayoutTransition(mLayoutTransition);
        try {
            mTransitioningViewsField = ViewGroup.class.getDeclaredField("mTransitioningViews");
            mTransitioningViewsField.setAccessible(true);
            mVisibilityChangingChildrenField = ViewGroup.class.getDeclaredField("mVisibilityChangingChildren");
            mVisibilityChangingChildrenField.setAccessible(true);
            mDisappearingChildrenField = ViewGroup.class.getDeclaredField("mDisappearingChildren");
            mDisappearingChildrenField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class MyLayoutTransition extends LayoutTransition {

        @Override
        public void addChild(ViewGroup parent, View child) {
            Log.i(TAG, "addChild1: parent = " + parent + ", child = " + child + ". child.getParent =" + child.getParent());
            super.addChild(parent, child);
            Log.i(TAG, "addChild2: parent = " + parent + ", child = " + child + ". child.getParent =" + child.getParent());

        }

        @Override
        public void removeChild(ViewGroup parent, View child) {
            Log.i(TAG, "removeChild1: parent = " + parent + ", child = " + child + ". child.getParent =" + child.getParent());
            super.removeChild(parent, child);
            Log.i(TAG, "removeChild2: parent = " + parent + ", child = " + child + ". child.getParent =" + child.getParent());
        }
    }

    /**
     * 崩溃模拟1
     * @param view
     */
    public void remove1(View view) {
        mContent.removeViewAt(0);
        Log.i(TAG, "remove 1: mChild.getParent() = " + mChild.getParent());
        Log.i(TAG, "remove 1: mContent.getChildAt(0) = " + mContent.getChildAt(0));

        View view1 = mParent.getChildAt(0);
        mParent.removeViewAt(0);
        FrameLayout fl = new FrameLayout(this);
        fl.addView(view1);
        mParent.addView(fl, 0);

        Log.i(TAG, "remove 2: mChild.getParent() = " + mChild.getParent());
        Log.i(TAG, "remove 2: mContent.getChildAt(0) = " + mContent.getChildAt(0));

        mContent.addView(mChild, 0);
        Log.i(TAG, "remove 3: mChild.getParent() = " + mChild.getParent());
        Log.i(TAG, "remove 3: mContent.getChildAt(0) = " + mContent.getChildAt(0));

    }

    /**
     * 崩溃模拟2
     * @param view
     */
    public void remove2(View view) {
        View child = mContent.getChildAt(0);
        mContent.removeViewAt(0);
        Log.i(TAG, "remove 4: mChild.getParent() = " + mChild.getParent());
        Log.i(TAG, "remove 4: mContent.getChildAt(0) = " + mContent.getChildAt(0));
        mParent.addView(child);
    }
}
