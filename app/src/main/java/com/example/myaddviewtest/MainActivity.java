package com.example.myaddviewtest;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

public class MainActivity extends Activity {

    private ViewGroup mParent;
    private View mChild;
    public static final String TAG = "MainActivity";
    private  MyLayoutTransition mLayoutTransition;
    private Field mTransitioningViewsField;
    private Field mVisibilityChangingChildrenField;
    private Field mDisappearingChildrenField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mParent = findViewById(R.id.parent);
        mChild = findViewById(R.id.child);

        mLayoutTransition = new MyLayoutTransition();
        // 先添加我们自己的监听，保证我们自己的先于系统的执行。
        mLayoutTransition.addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                try {
                    Log.i(TAG, "startTransition: mTransitioningViewsField = " + mTransitioningViewsField.get(mParent));
                    Log.i(TAG, "startTransition: mDisappearingChildrenField = " + mDisappearingChildrenField.get(mParent));
                    Log.i(TAG, "startTransition: mVisibilityChangingChildrenField = " + mVisibilityChangingChildrenField.get(mParent));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "startTransition: parent = " + mParent + ", child = " + mChild + ". child.getParent =" + mChild.getParent() + ", transitionType =" + transitionType);
            }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                try {
                    Log.i(TAG, "endTransition: mTransitioningViewsField = " + mTransitioningViewsField.get(mParent));
                    Log.i(TAG, "endTransition: mDisappearingChildrenField = " + mDisappearingChildrenField.get(mParent));
                    Log.i(TAG, "endTransition: mVisibilityChangingChildrenField = " + mVisibilityChangingChildrenField.get(mParent));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "endTransition: parent = " + mParent + ", child = " + mChild + ". child.getParent =" + mChild.getParent() + ", transitionType =" + transitionType );
            }
        });
        mParent.setLayoutTransition(mLayoutTransition);
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

    public void remove1(View view) {
        Log.i(TAG, "remove 1: view.getParent() = " + mChild.getParent());
        Log.i(TAG, "remove 1: parent.getChildAt(0) = " + mParent.getChildAt(0));
        mParent.removeView(mChild);
        Log.i(TAG, "remove 2: view.getParent() = " + mChild.getParent());
        Log.i(TAG, "remove 2: parent.getChildAt(0) = " + mParent.getChildAt(0));
        mParent.addView(mChild, 0);
    }


}
