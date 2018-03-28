package org.hopto.atkseegow.utility;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.View;

public class ConstraintLayoutUtility {
    public static View GetFirstView(ConstraintLayout largeConstraintLayout){
        View result = null;
        for (int i = 0; i < largeConstraintLayout.getChildCount(); i ++) {
            View view = largeConstraintLayout.getChildAt(i);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)view.getLayoutParams();
            if(layoutParams.topToBottom == -1 && view.getId() != 1)
            {
                result = view;
                break;
            }
        }
        return result;
    }

    public static View GetLastView(ConstraintLayout largeConstraintLayout) {
        View previousView = ConstraintLayoutUtility.GetFirstView(largeConstraintLayout);
        View lastView = null;

        do{
            View targetTopToBottomView = ConstraintLayoutUtility.GetTargetTopToBottomView(previousView.getId(), largeConstraintLayout);
            if(targetTopToBottomView == null)
                lastView = previousView;
            else
                previousView = targetTopToBottomView;
        }while(lastView == null);

        return lastView;
    }

    public static View GetTargetTopToBottomView(int topToBottomId, ConstraintLayout largeConstraintLayout){
        View result = null;
        for (int i = 0; i < largeConstraintLayout.getChildCount(); i ++) {
            View view = largeConstraintLayout.getChildAt(i);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)view.getLayoutParams();
            if(layoutParams.topToBottom == topToBottomId)
                result = view;
        }
        return result;
    }

    public static int DpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
