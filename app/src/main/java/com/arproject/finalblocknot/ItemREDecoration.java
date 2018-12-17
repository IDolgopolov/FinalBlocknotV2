package com.arproject.finalblocknot;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemREDecoration extends RecyclerView.ItemDecoration {
    private final int itemSpaceBottom;
    private final int itemSpaceHorizontal;


    public ItemREDecoration(int spaceBottom, int spaceHorizontal) {
        itemSpaceBottom = spaceBottom;
        itemSpaceHorizontal = spaceHorizontal;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = itemSpaceBottom;
        outRect.right = itemSpaceHorizontal;
        outRect.left = itemSpaceHorizontal;


        if(parent.getChildAdapterPosition(view) == 0) {
            outRect.top = itemSpaceBottom - 1;
        }
    }
}
