package com.arproject.finalblocknot.adapters;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.OneRandomEvent;
import com.arproject.finalblocknot.R;
import com.arproject.finalblocknot.dialog.RandomEventsDeleteDialog;

import java.util.ArrayList;

public class RecyclerAdapterPast extends RecyclerView.Adapter<RecyclerAdapterPast.ViewHolder> {
    private static ArrayList<OneRandomEvent> eventsList;
    private static int optionDialog;


    public RecyclerAdapterPast (ArrayList<OneRandomEvent> list, int option) {
        eventsList = list;
        optionDialog = option;
        Log.i("past_event", "generate");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mItem;
        private TextView textView;
        private TextView dateView;
        private ImageButton buttonDelete;
        private int ID;
        private String trueText;

        public ViewHolder(RelativeLayout item) {
            super(item);

            mItem = item;
            textView = (TextView) mItem.getChildAt(0);
            buttonDelete = (ImageButton) mItem.getChildAt(1);
            dateView = (TextView) mItem.getChildAt(2);


            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RandomEventsDeleteDialog dialog = new RandomEventsDeleteDialog();
                    Bundle args = new Bundle();
                    args.putInt("OPTION", optionDialog);
                    args.putString("DATE", dateView.getText().toString());
                    args.putString("TEXT", textView.getText().toString());
                    dialog.setArguments(args);
                    dialog.show(MainActivity.sFragmentManager, "PastDelete");

                }
            });

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.openDialogForEditingPastEvent(trueText, dateView.getText().toString());
                }
            });
        }
    }


    @Override
    public RecyclerAdapterPast.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout item  = (RelativeLayout) View.inflate(parent.getContext(), R.layout.item_for_recycler_view, null);



        RecyclerAdapterPast.ViewHolder vh = new RecyclerAdapterPast.ViewHolder(item);
        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerAdapterPast.ViewHolder holder, int position) {
        String text = eventsList.get(position).information;
        holder.trueText = text;
        if(text.length() > 50) {
            text = text.substring(0, 50) + "...";
        }
        holder.textView.setText(text);
        holder.ID = eventsList.get(position).ID;
        String date = eventsList.get(position).date;
        holder.dateView.setText(date);
    }


    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public void updateListEvents() {
        Log.i("past_event", "update list");
            eventsList = MainActivity.dbED.getPastInformation();

    }
}
