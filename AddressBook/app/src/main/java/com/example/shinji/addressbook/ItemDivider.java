package com.example.shinji.addressbook;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shinji on 2017/08/16.
 */
class ItemDivider extends RecyclerView.ItemDecoration {
    private final Drawable divider;

    // constructor loads built-in Android list item divider
    public ItemDivider(Context context) {
        int[] attrs = {android.R.attr.listDivider};
        divider = context.obtainStyledAttributes(attrs).getDrawable(0);
    }

    // draws the list item dividers onto the RecyclerView
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent,
                           RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        // calculate left/right x-coordinates for all dividers
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        // for every item but the last, draw a line below it
        for (int i = 0; i < parent.getChildCount() - 1; ++i) {
            View item = parent.getChildAt(i); // get ith list item

            // calculate top/bottom y-coordinates for current divider
            int top = item.getBottom() + ((RecyclerView.LayoutParams)
                    item.getLayoutParams()).bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            // draw the divider with the calculated bounds
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
//public class ItemDivider extends RecyclerView.ItemDecoration {
//
//    private Drawable mDivider;
//
//    private static final int[] ATTRS = new int[]{
//            android.R.attr.listDivider
//    };
//
//
//    public ItemDivider(Context context) {
//        final TypedArray a = context.obtainStyledAttributes(ATTRS);
//        mDivider = a.getDrawable(0);
//        a.recycle();
//    }
//
//    @Override
//    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        drawVertical(c, parent);
//    }
//
//    public void drawVertical(Canvas c, RecyclerView parent) {
//        final int left = parent.getPaddingLeft();
//        final int right = parent.getWidth() - parent.getPaddingRight();
//
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//                    .getLayoutParams();
//            final int top = child.getBottom() + params.bottomMargin;
//            final int bottom = top + mDivider.getIntrinsicHeight();
//            mDivider.setBounds(left, top, right, bottom);
//            mDivider.draw(c);
//        }
//    }
//
//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
//    }
//}
