package com.arproject.finalblocknot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.arproject.finalblocknot.ItemREDecoration;
import com.arproject.finalblocknot.R;
import com.arproject.finalblocknot.adapters.RecyclerAdapter;

public class RandomEventsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private static RecyclerAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView = (RecyclerView) View.inflate(getContext(), R.layout.recycler_view, null);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new ItemREDecoration(0, 10));

        adapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(adapter);


        return mRecyclerView;
    }

    public void updateListRandomEvents() {
        adapter.updateListEvents();
        adapter.notifyDataSetChanged();
    }






}
