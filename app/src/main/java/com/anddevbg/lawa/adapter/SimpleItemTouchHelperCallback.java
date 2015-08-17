package com.anddevbg.lawa.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by adri.stanchev on 16/08/2015.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final InterfaceTouchHelper mAdapter;
    public static final float ALPHA_FULL = 1.0f;

    public SimpleItemTouchHelperCallback(InterfaceTouchHelper adapter) {
        mAdapter = adapter;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);

    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

   /* @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(ALPHA_FULL);

        if (viewHolder instanceof ForecastViewHolder) {
            // Tell the view holder it's time to restore the idle state
            ForecastViewHolder itemViewHolder = (ForecastViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
    */
}
