//package com.jelhackers.icemelt;
//
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class POIAdapter extends RecyclerView.Adapter<POIAdapter.POIView> {
//
//    private ArrayList<POIObject> poiArrayList;
//    private Context mcontext;
//
//    public POIAdapter(ArrayList<POIObject> poiArrayList, Context mcontext){
//        this.poiArrayList = poiArrayList;
//        this.mcontext = mcontext;
//    }
//
//    @NonNull
//    @Override
//    public POIView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_card_message, parent, false);
//        return new POIView(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull POIView holder, int position) {
//        POIObject poiData = poiArrayList.get(position);
//        holder.poiText.setText(String.valueOf(poiData.getText()));
//    }
//
//    @Override
//    public int getItemCount() {
//        return poiArrayList.size();
//    }
//
//    public class POIView extends RecyclerView.ViewHolder{
//
//        private TextView poiText;
//        private CardView card;
//
//        public POIView(@NonNull View itemView){
//            super(itemView);
//            poiText = itemView.findViewById(R.id.poi_text);
//            card = itemView.findViewById(R.id.card_view);
//        }
//    }
//
//}
