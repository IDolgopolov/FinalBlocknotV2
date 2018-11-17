package com.arproject.finalblocknot.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arproject.finalblocknot.ItemREDecoration;
import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.R;
import com.arproject.finalblocknot.adapters.RecyclerAdapterRandom;
import com.arproject.finalblocknot.dialog.RandomEventsDeleteDialog;

public class RandomEventsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private static RecyclerAdapterRandom adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView = (RecyclerView) View.inflate(getContext(), R.layout.recycler_view, null);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new ItemREDecoration(12, 15));

        adapter = new RecyclerAdapterRandom(MainActivity.db.getAllInformation(), RandomEventsDeleteDialog.OPTION_RANDOM);
        mRecyclerView.setAdapter(adapter);


        return mRecyclerView;
    }

    public void updateListRandomEvents() {
        adapter.updateListEvents();
        adapter.notifyDataSetChanged();
    }






}
