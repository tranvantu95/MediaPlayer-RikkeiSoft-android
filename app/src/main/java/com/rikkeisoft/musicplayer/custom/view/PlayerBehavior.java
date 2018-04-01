package com.rikkeisoft.musicplayer.custom.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.rikkeisoft.musicplayer.R;

public class PlayerBehavior extends AppBarLayout.ScrollingViewBehavior {

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public PlayerBehavior() {
    }

    public PlayerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
//        android.util.Log.d("debug", "child " + child.getTop());
//        android.util.Log.d("debug", "dependency " + dependency.getTop());
        if(callback != null) callback.onTopChange(child.getTop() - dependency.findViewById(R.id.toolbar).getHeight());
        return super.onDependentViewChanged(parent, child, dependency);
    }

    public interface Callback {
        void onTopChange(int top);
    }

}
