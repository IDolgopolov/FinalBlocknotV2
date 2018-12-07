package com.arproject.finalblocknot.adapters;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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


public class RecyclerAdapterRandom extends  RecyclerView.Adapter<RecyclerAdapterRandom.ViewHolder> {
    private static ArrayList<OneRandomEvent> eventsList;
    private static int optionDialog;


    public RecyclerAdapterRandom(ArrayList<OneRandomEvent> list, int option) {
                eventsList = list;
                optionDialog = option;
     }

     public static class ViewHolder extends RecyclerView.ViewHolder {
            private RelativeLayout mItem;
            private TextView textView;
            private TextView dateView;
            private ImageButton buttonDelete;
            private int ID;
            private String trueText;

        public ViewHolder(CardView item) {
            super(item);

            mItem = (RelativeLayout) item.getChildAt(0);
            textView = (TextView) mItem.getChildAt(0);
            buttonDelete = (ImageButton) mItem.getChildAt(1);
            dateView = (TextView) mItem.getChildAt(2);


            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        RandomEventsDeleteDialog dialog = new RandomEventsDeleteDialog();
                        Bundle args = new Bundle();
                        args.putInt("ID", ID);
                        args.putInt("OPTION", optionDialog);
                        dialog.setArguments(args);
                        dialog.show(MainActivity.sFragmentManager, "REDelete");

                }
            });

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.openDialogForEditingRandomEvent(trueText, ID);
                }
            });
        }
    }


    @Override
    public RecyclerAdapterRandom.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView item  = (CardView) View.inflate(parent.getContext(), R.layout.item_for_recycler_view, null);


        ViewHolder vh = new ViewHolder(item);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = eventsList.get(position).information;
        holder.trueText = text;
        if(text.length() > 50) {
            text = text.substring(0, 50) + "...";
        }
        holder.textView.setText(text);
        holder.ID = eventsList.get(position).pos;
        String date = eventsList.get(position).date;
        holder.dateView.setText(date);
    }


    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public void updateListEvents() {
            eventsList = MainActivity.db.getAllInformation();
    }



}
