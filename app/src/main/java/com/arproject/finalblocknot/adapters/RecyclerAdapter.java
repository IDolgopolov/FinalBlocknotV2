package com.arproject.finalblocknot.adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arproject.finalblocknot.MainActivity;
import com.arproject.finalblocknot.OneRandomEvent;
import com.arproject.finalblocknot.R;
import com.arproject.finalblocknot.dialog.RandomEventsDialog;

import java.util.ArrayList;


public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static ArrayList<OneRandomEvent> eventsList;

    public RecyclerAdapter() {
                eventsList = MainActivity.db.getAllInformation();
     }

     public static class ViewHolder extends RecyclerView.ViewHolder {
            private RelativeLayout mItem;
            private TextView textView;
            private TextView dateView;
            private Button buttonDelete;
            private int ID;
            private String trueText;

        public ViewHolder(RelativeLayout item) {
            super(item);

            mItem = item;
            textView = (TextView) mItem.getChildAt(0);
            buttonDelete = (Button) mItem.getChildAt(1);
            dateView = (TextView) mItem.getChildAt(2);


            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.deleteRandomEventFromDB(ID);
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
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout item  = (RelativeLayout) View.inflate(parent.getContext(), R.layout.item_for_recycler_view, null);



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
        holder.ID = eventsList.get(position).ID;
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
