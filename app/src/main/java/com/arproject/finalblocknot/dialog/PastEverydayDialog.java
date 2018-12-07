package com.arproject.finalblocknot.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.arproject.finalblocknot.ItemREDecoration;
import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.R;
import com.arproject.finalblocknot.adapters.RecyclerAdapterPast;

public class PastEverydayDialog extends DialogFragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private static RecyclerAdapterPast adapterEE;



    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LinearLayout layout = (LinearLayout) View.inflate(getContext(), R.layout.recycler_view_past, null);
        mRecyclerView = (RecyclerView) layout.getChildAt(0);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new ItemREDecoration(20, 5));
        adapterEE = new RecyclerAdapterPast(MainActivity.dbED.getPastInformation(), RandomEventsDeleteDialog.OPTION_EVERYDAY);
        mRecyclerView.setAdapter(adapterEE);

        builder.setView(layout);
        Dialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public static void updateList() {
        adapterEE.updateListEvents();
        adapterEE.notifyDataSetChanged();
    }
}
