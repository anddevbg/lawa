package com.anddevbg.lawa.recyclerview;

/**
 * Created by adri.stanchev on 16/08/2015.
 */
public interface InterfaceTouchHelper {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
