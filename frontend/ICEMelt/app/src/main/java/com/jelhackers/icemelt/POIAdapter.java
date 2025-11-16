package com.jelhackers.icemelt;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class POIAdapter extends RecyclerView.Adapter<POIAdapter.POIView> {

    private ArrayList<POIObject> poiArrayList;
    private Context mcontext;

    OnPOIClick onPOIClickListener;
    public POIAdapter(ArrayList<POIObject> poiArrayList, Context mcontext, OnPOIClick listener){
        this.poiArrayList = poiArrayList;
        this.mcontext = mcontext;
        this.onPOIClickListener = listener;
    }

    @NonNull
    @Override
    public POIView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_card, parent, false);
        return new POIView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull POIView holder, int position) {
        POIObject poiData = poiArrayList.get(position);
        holder.poiText.setText(poiData.toString());

        holder.card.setOnClickListener(v -> {
            if(onPOIClickListener != null){
                onPOIClickListener.onPOIClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return poiArrayList.size();
    }

    public class POIView extends RecyclerView.ViewHolder{

        private TextView poiText;
        private CardView card;

        public POIView(@NonNull View itemView){
            super(itemView);
            poiText = itemView.findViewById(R.id.poi_message);
            card = itemView.findViewById(R.id.card_view);
        }

    }

    public interface OnPOIClick{
        /**
         * Called when a pantry ingredient item is clicked.
         *
         * @param position The position of the clicked ingredient item in the RecyclerView.
         */
        void onPOIClick(int position);
    }
}
